package controller;

import gui.CategorizationScreenAdvisorFrame;
import gui.DetectionScreenAdvisorFrame;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import utils.ClassPathHacker;
import utils.Constants;
import utils.FileUtil;
import categorize.Categorize;
import categorize.Nonconformance;
import detect.Detect;
import detect.ResultProducer;
import detect.TestError;

public class Controller {
	
	private static Set<TestError> errors;
	private static Set<Nonconformance> nonconformities;
	private static String source;
	
	/**
	 * Prepare the for the detect fase of the program.
	 * @param compiler The compiler that will be used. 
	 * @param sourceFolder The source folder that the program will analyze.
	 * @param lib The lib folder in which the analysis depend of. 
	 * @param time The time (in seconds) to generate tests (with Randoop).
	 */
	public static void prepareToDetectPhase(int compiler, String sourceFolder, String lib, String time){
		 setSystemVariableClassPath(lib);
		 source = sourceFolder;
		 showDetectionScreen(compiler, lib, time);
	}

	/**
	 * Prepare tools and info to shown on Detection Screen.
	 * @param compiler The compiler that will be used. 
	 * @param lib The lib folder in which the analysis depend of. 
	 * @param time The time (in seconds) to generate tests (with Randoop).
	 * @return an ByteArrayOuputStream with the console log of Detection execution.
	 */
	private static ByteArrayOutputStream setToolsForDetectionScreen(int compiler, String lib, String time) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream old = System.out;
		System.setOut(ps);
		errors = fulfillDetectPhase(compiler, source, lib, time);
		System.out.flush();
	    System.setOut(old);
	    return baos;
	}

	/**
	 * Shows the Detection Screen, with console, and button showing.
	 * @param compiler The compiler that will be used. 
	 * @param lib The lib folder in which the analysis depend of. 
	 * @param time The time (in seconds) to generate tests (with Randoop).
	 */
	public static void showDetectionScreen(int compiler, String lib, String time) {
	    final ByteArrayOutputStream caos = setToolsForDetectionScreen(compiler, lib, time);
	    EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DetectionScreenAdvisorFrame frame = new DetectionScreenAdvisorFrame(caos);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Show the Categorization Screen with all of the fiels filled.
	 */
	public static void showCategorizationScreen() {
		final List<Nonconformance> nonconformance = fulfillCategorizePhase(errors, source);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CategorizationScreenAdvisorFrame frame = new CategorizationScreenAdvisorFrame(nonconformance);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Set an hack to add jars on SystemClassLoader.
	 * @param libFolder The name of the lib folder containing jars.
	 */
	private static void setSystemVariableClassPath(String libFolder) {
		boolean isLinux = System.getProperty("os.name").equals("Linux");
		String pathVar = FileUtil.getListPathPrinted(libFolder);
		for(String jar : pathVar.split((isLinux)?":":";")){
			try {
				ClassPathHacker.addFile(jar);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Call Detect for the detect phase.
	 * @param compiler Which compiler to use on detection.
	 * @param source The source folder to compile.
	 * @param lib The lib folder which compiler depends on.
	 * @param time The time(in seconds) where the test will be generated.
	 * @return an set of test errors info generated from the detection phase.
	 */
	private static Set<TestError> fulfillDetectPhase(int compiler, String source, String lib, String time){
		Detect d = new Detect(compiler);
		Set<TestError> errors = d.detect(source, lib, time);
		return errors;
	}
	
	/**
	 * Call Categorize for the categorization phase.
	 * @param errors The set of test erros to categorize.
	 * @param source The source folder where the erros where detected.
	 * @return an list of nonconformances already categorized.
	 */
	private static List<Nonconformance> fulfillCategorizePhase(Set<TestError> errors, String source) {
		Categorize c = new Categorize();
		List<Nonconformance> x = new ArrayList<Nonconformance>();
		nonconformities = c.categorize(errors, source);
		ResultProducer.generateResult(nonconformities);
		for(Nonconformance n : nonconformities)
			x.add(n);
		return x;
	}

	/**
	 * Copy the file results.xml generated from the categorization to another file specified.
	 * @param path The path where the file will be copied.
	 * @throws IOException When the path is invalid.
	 */
	public static void saveResultsInXML(String path) throws IOException {
		Path source = (new File(Constants.RESULTS)).toPath();
		Path target = (new File(path + Constants.FILE_SEPARATOR + "results.xml")).toPath();
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
	}

}
