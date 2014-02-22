package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import categorize.Nonconformance;

public class CategorizationScreenAdvisorFrame extends JFrame {
	
	private List<Nonconformance> nc;
	private String[] namesNC;
	private JPanel contentPane;
	private JList listNonconformances;
	private JLabel lblMethodNameSetter;
	private JLabel lblClassNameSetter;
	private JLabel lblLikelyCauseSetter;
	private JLabel lblLikelyCauseExplanation;
	private JLabel lblPackageNameSetter;
	private JLabel lblPackageName;
	private JTextArea textAreaTestCases;
	private Highlighter highLit;
	private Highlighter.HighlightPainter painter;
	private JLabel lblNonconfomancesNumberSetter;
	

	/**
	 * Create the frame.
	 */
	 public CategorizationScreenAdvisorFrame(List<Nonconformance> nonconformance) {
		initializingStringForSelectionList(nonconformance);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNonconf = new JLabel("Nonconformances");
		lblNonconf.setBounds(53, 54, 214, 15);
		contentPane.add(lblNonconf);
		
		JLabel lblNewLabel = new JLabel("Location");
		lblNewLabel.setBounds(283, 54, 70, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblTestCases = new JLabel("Test Cases");
		lblTestCases.setBounds(34, 333, 134, 15);
		contentPane.add(lblTestCases);
		
		JButton btnSaveResults = new JButton("Save Results");
		btnSaveResults.setBounds(446, 353, 143, 25);
		contentPane.add(btnSaveResults);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeWindow();
			}
		});
		btnExit.setBounds(446, 407, 143, 25);
		contentPane.add(btnExit);
		
		JLabel lblClassName = new JLabel("Class Name");
		lblClassName.setBounds(213, 135, 106, 15);
		contentPane.add(lblClassName);
		
		JLabel lblMethodName = new JLabel("Method Name");
		lblMethodName.setBounds(213, 191, 106, 15);
		contentPane.add(lblMethodName);
		
		lblClassNameSetter = new JLabel("");
		lblClassNameSetter.setBounds(223, 165, 134, 15);
		contentPane.add(lblClassNameSetter);
		
		lblMethodNameSetter = new JLabel("");
		lblMethodNameSetter.setBounds(223, 221, 134, 15);
		contentPane.add(lblMethodNameSetter);
		
		JLabel lblCause = new JLabel("Cause");
		lblCause.setBounds(213, 245, 70, 15);
		contentPane.add(lblCause);
		
		lblLikelyCauseSetter = new JLabel("");
		lblLikelyCauseSetter.setBounds(223, 275, 143, 15);
		contentPane.add(lblLikelyCauseSetter);
		
		lblLikelyCauseExplanation = new JLabel("");
		lblLikelyCauseExplanation.setBounds(437, 218, 153, 15);
		contentPane.add(lblLikelyCauseExplanation);
		
		lblPackageName = new JLabel("Package");
		lblPackageName.setBounds(220, 81, 133, 15);
		contentPane.add(lblPackageName);
		
		lblPackageNameSetter = new JLabel("");
		lblPackageNameSetter.setBounds(219, 109, 134, 15);
		contentPane.add(lblPackageNameSetter);
		
		listNonconformances = new JList(namesNC);
		listNonconformances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listNonconformances.setBounds(24, 81, 178, 240);
		contentPane.add(listNonconformances);
		highLit = new DefaultHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
		listNonconformances.addListSelectionListener(
			new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					setChangesFromSelectionOnTheList();
				}
			}
		);
		
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setAlignmentX(LEFT_ALIGNMENT);
		scrollPane2.setAlignmentY(TOP_ALIGNMENT);
		scrollPane2.setBounds(24, 353, 401, 86);
		contentPane.add(scrollPane2);
		
		textAreaTestCases = new JTextArea();
		scrollPane2.setViewportView(textAreaTestCases);
		
		JLabel lblWereDetected = new JLabel("Were detected: ");
		lblWereDetected.setBounds(24, 27, 139, 15);
		contentPane.add(lblWereDetected);
		
		lblNonconfomancesNumberSetter = new JLabel(nc.size() + "");
		lblNonconfomancesNumberSetter.setBounds(144, 27, 70, 15);
		contentPane.add(lblNonconfomancesNumberSetter);
		
		JLabel lblNonconfomities = new JLabel("nonconformances.");
		lblNonconfomities.setBounds(175, 27, 184, 15);
		contentPane.add(lblNonconfomities);
		
		JPanel panel = new JPanel();
		panel.setBounds(423, 27, 192, 301);
		contentPane.add(panel);
		
	}

	private void setChangesFromSelectionOnTheList() {
			lblClassNameSetter.setText(nc.get(listNonconformances.getSelectedIndex()).getClassName());
			lblMethodNameSetter.setText(nc.get(listNonconformances.getSelectedIndex()).getMethodName());
			lblLikelyCauseSetter.setText(nc.get(listNonconformances.getSelectedIndex()).getCause());
			if(nc.get(listNonconformances.getSelectedIndex()).getPackageName() == ""){
				lblPackageNameSetter.setText("<default>");						
			}else{
				lblPackageNameSetter.setText(nc.get(listNonconformances.getSelectedIndex()).getPackageName());						
			}
			textAreaTestCases.setText(nc.get(listNonconformances.getSelectedIndex()).getLinesFromTestFile());
			textAreaTestCases.setEditable(false);
			textAreaTestCases.setHighlighter(highLit);
			int beginIndex = textAreaTestCases.getText().indexOf("\n", textAreaTestCases.getText().indexOf("\n")) + 1;
			beginIndex = textAreaTestCases.getText().indexOf("\n", beginIndex) + 1;
			int endIndex = textAreaTestCases.getText().indexOf(";", beginIndex) + 1;
			try {
				highLit.addHighlight(beginIndex, endIndex, painter);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
	}
	 
	private void initializingStringForSelectionList(List<Nonconformance> nonconformance) {
		nc = nonconformance;
		namesNC = new String[nc.size()];
		for (int i = 0; i < nc.size(); i++) {
			namesNC[i] = nc.get(i).getType() + " " + nc.get(i).getTest(); 
		}
	}

	protected void closeWindow() {
		this.setVisible(false);
	}
}
