package categorize;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.FileUtil;

public class PatternsTool {
	private static String PRECONDITION_INDICATOR = "requires|pre|requires_redundantly|pre_redundantly";
	private static String POSTCONDITION_INDICATOR = "ensures|post|ensures_redundantly|post_redundantly";
	private static String INVARIANT_INDICATOR = "invariant|invariant_redundantly";
	private static String CONSTRAINT_INDICATOR = "constraint|constraint_redundantly";
	private String srcDir;
	// Used to read and group Class and method detected in
	public Pattern captureClassMethodInPrePos = Pattern
			.compile("([A-Z][\\w]*)[\\.]([\\w]+)");
	// Used to capture attributes from an method declaration
	public Pattern captureAttributes = Pattern
			.compile("\\s*[a-zA-Z]\\w*\\s+(\\w+)\\s*,?");
	/**
	 * Constructs an PatternsTool object with the directory of the src project paste
	 * @param dir directory of the src project paste
	 */
	public PatternsTool(String dir){
		this.srcDir = dir;
	}
	
	/**
	 * Method that returns the complete path to class, whose class name was received as parameter.
	 * @param className - the name of the class.
	 * @return - the full path to the file.
	 */
	private String getPathFromFile(String className){
		String name = className.replace('.', '/');
		name += ".java";
		return srcDir+name;
	}
	
	/**
	 * Checks if there is an attribute on the method declaration that is on
	 * precondition specification.
	 * @param classname name of the class searched
	 * @param methodname name of the method searched
	 * @param pathcode path of the file that contains the code where the search will be located
	 * @return true if some attribute on the method declaration are located on the precondition expression or false.
	 */
	public boolean isAtrInPrecondition(String classname, String methodname,	String pathcode) {
		Pattern capturePreAtr = conditionPattern(classname, methodname, PRECONDITION_INDICATOR);
		String lines = FileUtil.readFile(pathcode);
		Matcher condAtr = capturePreAtr.matcher(lines);
		condAtr.find();
		String precondition = condAtr.group(1);
		String attributeDeclaration = condAtr.group(2);
		Matcher searchAtr = captureAttributes.matcher(attributeDeclaration);
		while (searchAtr.find()) {
			if (precondition.contains(searchAtr.group(1)))
				return true;
		}
		return false;
	}
	/**
	 * Checks if there is an attribute on the method declaration that is on
	 * postcondition specification.
	 * @param classname name of the class searched
	 * @param methodname name of the method searched
	 * @param pathcode path of the file that contains the code where the search will be located
	 * @return true if some attribute on the method declaration are located on the postcondition expression or false.
	 */
	public boolean isAtrInPostcondition(String classname, String methodname, String pathcode) {
		Pattern capturePostAtr = conditionPattern(classname, methodname, POSTCONDITION_INDICATOR);
		String lines = FileUtil.readFile(pathcode);
		Matcher condAtr = capturePostAtr.matcher(lines);
		condAtr.find();
		String postcondition = condAtr.group(1);
		String attributeDeclaration = condAtr.group(2);
		Matcher searchAtr = captureAttributes.matcher(attributeDeclaration);
		while (searchAtr.find()) {
			if (postcondition.contains(searchAtr.group(1)))
				return true;
		}
		return false;
	}
	/**
	 * Checks if there is an variable from the class that are on the precondition
	 * specification expression.
	 * @param classname name of the class searched
	 * @param methodname name of the method searched
	 * @param pathcode path to where the .java of the class are located
	 * @return true if the variable are present of false
	 */
	public boolean isVariableInPrecondition(String classname, String methodname, String pathcode){
		Pattern capturePreCon = conditionPattern(classname, methodname, PRECONDITION_INDICATOR);
		String lines = FileUtil.readFile(pathcode);
		Matcher condAtr = capturePreCon.matcher(lines);
		condAtr.find();
		String precondition = condAtr.group(1);
		ArrayList<String> variables = FileUtil.getVariablesFromClass(classname);
		for (int i = 0; i < variables.size(); i++) {
			if(precondition.matches(variables.get(i)))
				return true;
		}
		return false;
	}
	/**
	 * Checks if there is an variable from the class that is on the postcondition
	 * specification expression.
	 * @param classname name of the class searched
	 * @param methodname name of the method searched
	 * @param pathcode path to where the .java of the class are located
	 * @return true if the variable are present of false
	 */
	public boolean isVariableInPostcondition(String classname, String methodname, String pathcode){
		Pattern capturePostCon = conditionPattern(classname, methodname, POSTCONDITION_INDICATOR);
		String lines = FileUtil.readFile(pathcode);
		Matcher condAtr = capturePostCon.matcher(lines);
		condAtr.find();
		String postcondition = condAtr.group(1);
		ArrayList<String> variables = FileUtil.getVariablesFromClass(classname);
		for (int i = 0; i < variables.size(); i++) {
			if(postcondition.matches(variables.get(i)))
				return true;
		}
		return false;
	}
	/**
	 * Creates a Pattern that can be used to capture a specification
	 * from a precondition, postcondition expression of a method
	 * from a determined class.
	 * @param classname Name of the class searched
	 * @param methodname Name of the method searched
	 * @param conditionsearched The keywords from precondition or postcondition.
	 * @return A Pattern to capture expressions from precondition or postcondition.
	 */
	private Pattern conditionPattern(String classname, String methodname, String conditionsearched) {
		Pattern captureCon = Pattern.compile(".*public\\s+class\\s+"
				+ classname + "\\s*\\{.*" + "//@\\s*(?:" + conditionsearched + ")([^;]*);.*"
				+ "(?:private|public|protected|\\s*)\\s+[\\w]+\\s+" + methodname
				+ "\\s*\\(([^()]*)\\)\\{", Pattern.DOTALL);
		return captureCon;
	}

	/**
	 * Method used to check if a likely cause of a nonconformance is weak precondition.
	 * @param className
	 * @param methodName
	 * @param sourceFolder
	 * @return
	 */
	public boolean checkWeakPrecondition(String className, String methodName,
			String sourceFolder) {
		return isRequiresTrue(className, methodName, sourceFolder) || isAttModifiedOnMethod(className, methodName, sourceFolder);
	}

	private boolean isAttModifiedOnMethod(String className, String methodName,
			String sourceFolder) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isRequiresTrue(String className, String methodName,
			String sourceFolder) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkNull(String className, String methodName,
			String sourceFolder) {
		// TODO Auto-generated method stub
		return false;
	}
	
}