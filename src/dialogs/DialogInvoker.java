package dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import treeNodes.ConstructorNode;
import treeNodes.FieldNode;
import treeNodes.MethodNode;
import extras.JoinpointContainer;
import extras.PointcutContainer;

public class DialogInvoker {

	//	private final String[] POINTCUT_OPTIONS = {"call", "execution", "get", "set", "initialization",
	//			"preinitialization", "staticinitialization", "handler", "adviceexecution", "within", "withincode",
	//			"cflow", "cflowbelow", "this", "target", "args", "PointcutId", "if", "!", "&&", "||", "( )"};
	
	/**********************************/
	/***** ComboBox String Arrays *****/
	/**********************************/
	private final String[] POINTCUT_FIELDS = {"get", "set"};
	private final String[] POINTCUT_METHODS = {"call", "execution", "withincode"};
	private final String[] POINTCUT_CONSTRUCTORS = {"call", "execution", "initialization",
			"preinitialization", "withincode"};
	private final String[] POINTCUT_TYPES = {"staticinitialization", "handler", "adviceexecution",
			"this", "target", "args", "cflow", "cflowbelow", "within", "if"};


	private final String[] JOINPOINT_TYPES = {"Field", "Method", "Constructor", "Pointcut/Type"};

	private final String[] ADVICE_OPTIONS = {"before", "after", "around"};

	/**************************************/
	/***** Joinpoint Type Identifiers *****/
	/**************************************/
	public static final int TYPE_FIELD = 0;
	public static final int TYPE_METHOD = 1;
	public static final int TYPE_CONSTRUCTOR = 2;
	public static final int TYPE_POINTCUT_OR_TYPES = 3;

	
	
	/****************************/
	/***** Components Lists *****/
	/****************************/
	private List<JButton> listJoinpointButtonList;
	private List<JTextField> listJoinpointText;
	private List<JComboBox<String>> listJoinpoints;
	private List<JComboBox<String>> listJoinpointType;
	private List<JCheckBox> listJoinpointNot;

	/*********************/
	/***** Variables *****/
	/*********************/
	private DialogListener mListener;
	private JFrame mParentFrame;
	private JDialog mDialog;
	private JTextField mPointcutNameField;
	private JTextField mPointcutArgs;
	private JComboBox<String> mJoinpointRelations;
	
	private final int JOINPOINT_COLUMNS = 5;
	private final int JOINPOINT_TXTFLD_SIZE = 25;
	private final int POINTCUT_TXTFLD_SIZE = 15;
	private final int ADVICE_TXTFLD_SIZE = 10;
	
	private int joinpoint_rows = 0;
	private boolean isAdviceAround = false;
	private JTextField mAdviceRetType;

	/*******************************/
	/***** Placeholder Strings *****/
	/*******************************/
	private final String PH_POINTCUT_NAME = "MyPointcut";
	private final String PH_FIELD = "field";
	private final String PH_METHOD = "retType Object.Method(args)";
	private final String PH_CONSTRUCTOR = "constructor(args)";
	private final String PH_TYPE = "otherPointcut or Type";
	
	
	/***********************************/
	/***** Action Commands Strings *****/
	/***********************************/
	private final String CMD_SAVE = "save";
	private final String CMD_CANCEL = "cancel";
	private final String JOINPOINT_TYPE = "joinpoint type";
	
	
	/*************************/
	/***** Button Labels *****/
	/*************************/
	private final String LBL_SAVE = "Save";
	private final String LBL_CANCEL = "Cancel";
	private final String LBL_NEW_JOINPOINT = "Add new joinpoint";


	/*******************************************************************************************/
	/*********************************** Constructor *******************************************/
	/*******************************************************************************************/


	public DialogInvoker(JFrame owner, DialogListener listener) {

		mListener = listener;
		mParentFrame = owner;
		listJoinpointButtonList = new ArrayList<>();
		listJoinpointText = new ArrayList<>();
		listJoinpoints = new ArrayList<>();
		listJoinpointType = new ArrayList<>();
		listJoinpointNot = new ArrayList<>();

	}


	/*******************************************************************************************/
	/********************************* Pointcut Dialog *****************************************/
	/*******************************************************************************************/

	public void pointcutDialog(int type, Object nodeContainer) {

		//TODO add relation option between pointcut options (and\or)
		mDialog = new JDialog(mParentFrame);
		mDialog.setLocationRelativeTo(mParentFrame);

		Container contentPane = mDialog.getContentPane();
		contentPane.setLayout(new BorderLayout());

		mDialog.setTitle("PointCut");

		//pointcut name panel
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		namePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel lblName = new JLabel("Name: ");
		lblName.setBorder(new EmptyBorder(10, 10, 10, 10));
		mPointcutNameField = new JTextField(PH_POINTCUT_NAME, POINTCUT_TXTFLD_SIZE);
		
		JLabel lblArgs = new JLabel("Args: ");
		mPointcutArgs = new JTextField(POINTCUT_TXTFLD_SIZE);
		
		JLabel lblRelation = new JLabel("Joinpoint Relation: ");
		mJoinpointRelations = new JComboBox<>(new String[] {"AND", "OR"});
		
		
		namePanel.add(lblName);
		namePanel.add(mPointcutNameField);
		namePanel.add(lblArgs);
		namePanel.add(mPointcutArgs);
		namePanel.add(lblRelation);
		namePanel.add(mJoinpointRelations);



		//joinpoint panel
		JPanel joinpointPanel = new JPanel();
		joinpointPanel.setLayout(new GridBagLayout());
		joinpointPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JComboBox<String> typeList = new JComboBox<>(JOINPOINT_TYPES);
		typeList.setSelectedIndex(type);
		typeList.setBorder(new EmptyBorder(10, 10, 10, 10));

		JComboBox<String> joinpoints = getComboBox(type);
		joinpoints.setSelectedIndex(0);
		joinpoints.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField joinpointTextField = new JTextField(nodeContainer == null ? getPHString(type) : getActualString(nodeContainer),
				JOINPOINT_TXTFLD_SIZE);

		
		
		//Setup components in layout
		GridBagConstraints c = new GridBagConstraints();
		JCheckBox chkNot = new JCheckBox("!", false);
		c.gridx = 1;
		c.gridy = joinpoint_rows;
		joinpointPanel.add(chkNot, c);
		c.gridx = 2;
		c.gridy = joinpoint_rows;
		joinpointPanel.add(typeList, c);
		c.gridx = 3;
		c.gridy = joinpoint_rows;
		joinpointPanel.add(joinpoints, c);
		c.gridx = 7;
		c.gridy = joinpoint_rows;
		joinpointPanel.add(new JPanel(), c); //add empty panel for 3rd col
		c.gridx = 4;
		c.gridy = joinpoint_rows;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		joinpointPanel.add(joinpointTextField,c);

		
		
		listJoinpointText.add(joinpointTextField);
		listJoinpoints.add(joinpoints);
		listJoinpointType.add(typeList);
		listJoinpointNot.add(chkNot);


		
		//button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton btnSave = new JButton(LBL_SAVE);
		JButton btnCancel = new JButton(LBL_CANCEL);
		JButton btnAddField = new JButton(LBL_NEW_JOINPOINT);

		buttonPanel.add(btnAddField);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnCancel);



		//Action Listener
		btnSave.setActionCommand(CMD_SAVE);
		btnCancel.setActionCommand(CMD_CANCEL);
		typeList.setActionCommand(JOINPOINT_TYPE);

		MyActionListener actionListener = new MyActionListener();
		btnSave.addActionListener(actionListener);
		btnCancel.addActionListener(actionListener);
		typeList.addActionListener(actionListener);

		btnAddField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addNewJoinpoint(joinpointPanel, type, actionListener);

			}
		});



		//add panels to dialog
		contentPane.add(namePanel, BorderLayout.NORTH);
		contentPane.add(joinpointPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		mDialog.setResizable(false);
		mDialog.pack();
		mDialog.setVisible(true);
	}

	/***********************************************/
	/*************** Add Joinpoint *****************/
	/***********************************************/

	private void addNewJoinpoint(JPanel panel, int type, ActionListener listener) {

		System.out.println("New Field...");

		JComboBox<String> joinpoints = getComboBox(type);
		joinpoints.setSelectedIndex(0);
		joinpoints.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField joinpointTextField = new JTextField(getPHString(type), JOINPOINT_TXTFLD_SIZE);

		JComboBox<String> typeList = new JComboBox<>(JOINPOINT_TYPES);
		typeList.setSelectedIndex(type);
		typeList.setBorder(new EmptyBorder(10, 10, 10, 10));

		ImageIcon closeIcon = new ImageIcon(DialogInvoker.class.getResource("/close.png"));
		JButton btnClose = new JButton(closeIcon);
		JPanel closePanel = new JPanel();
		closePanel.add(btnClose);



		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JButton thisButton = (JButton) e.getSource();

				int btnIndex = listJoinpointButtonList.indexOf(thisButton);
				int index = ((btnIndex + 1) * JOINPOINT_COLUMNS) + (JOINPOINT_COLUMNS - 1);
				System.out.println("Panel index = " + index);
				System.out.println("btnIndex = " + btnIndex);

				listJoinpointButtonList.remove(btnIndex);
				listJoinpointText.remove(btnIndex + 1);
				listJoinpoints.remove(btnIndex + 1);
				listJoinpointType.remove(btnIndex + 1);
				panel.remove(index);
				panel.remove(index - 1);
				panel.remove(index - 2);
				panel.remove(index - 3);
				panel.remove(index - 4);
				joinpoint_rows--;

				
				panel.validate();
				panel.repaint();
				mDialog.pack();
			}
		});
		
		typeList.setActionCommand(JOINPOINT_TYPE);
		typeList.addActionListener(listener);

		
		
		//Setup components in layout
		joinpoint_rows++;
		GridBagConstraints c = new GridBagConstraints();
		JCheckBox chkNot = new JCheckBox("!", false);
		c.gridx = 1;
		c.gridy = joinpoint_rows;
		panel.add(chkNot, c);
		c.gridx = 2;
		c.gridy = joinpoint_rows;
		panel.add(typeList, c);
		c.gridx = 3;
		c.gridy = joinpoint_rows;
		panel.add(joinpoints, c);
		c.gridx = 7;
		c.gridy = joinpoint_rows;
		panel.add(closePanel, c);
		c.gridx = 4;
		c.gridy = joinpoint_rows;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		panel.add(joinpointTextField,c);

		
		
		listJoinpointButtonList.add(btnClose);
		listJoinpointText.add(joinpointTextField);
		listJoinpoints.add(joinpoints);
		listJoinpointType.add(typeList);
		listJoinpointNot.add(chkNot);

		System.out.println(listJoinpointButtonList.size());

		panel.validate();
		panel.repaint();
		mDialog.pack();
	}


	/*******************************************************************************************/
	/********************************** Advice Dialog ******************************************/
	/*******************************************************************************************/


	public void adviceDialog(List<PointcutContainer> pointcuts) {

		mDialog = new JDialog(mParentFrame);
		mDialog.setTitle("Advice");

		final JPanel contentPanel = new JPanel();
		final RSyntaxTextArea textArea = new RSyntaxTextArea();
		JPanel buttonPane = new JPanel();
		JPanel pointcutPanel = new JPanel();

		mDialog.setBounds(100, 100, 450, 400);
		mDialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout(0, 0));



		//TextArea Panel
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);

		RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		contentPanel.add(scrollPane, BorderLayout.CENTER);



		//Buttons Panel
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));		

		JButton btnSave = new JButton("Save");
		JButton btnCancel = new JButton("Cancel");
		mDialog.getRootPane().setDefaultButton(btnSave);

		buttonPane.add(btnSave);
		buttonPane.add(btnCancel);

		
		//Pointcuts panel
		pointcutPanel.setLayout(new GridBagLayout());
		pointcutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		Vector<String> createdPointcuts = new Vector<>();
		for(int i = 0; i < pointcuts.size(); i++) {
			createdPointcuts.add(pointcuts.get(i).getName());
		}
		JComboBox<String> listPointcuts = new JComboBox<>(createdPointcuts);
		JComboBox<String> adviceOptions = new JComboBox<>(ADVICE_OPTIONS);
		
		listPointcuts.setBorder(new EmptyBorder(10, 10, 10, 10));
		adviceOptions.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		pointcutPanel.add(adviceOptions, c);
		c.gridx = 2;
		c.ipadx = 100;
		pointcutPanel.add(listPointcuts, c);

		
		adviceOptions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JComboBox<String> advice = (JComboBox<String>) e.getSource();
				String selected = (String) advice.getSelectedItem();
				String around = ADVICE_OPTIONS[2];
				GridBagConstraints c = new GridBagConstraints();
				
				if(selected.equals(around)) {
					
					isAdviceAround = true;
					mAdviceRetType = new JTextField("retType", ADVICE_TXTFLD_SIZE);
					c.gridx = 0;
					pointcutPanel.add(mAdviceRetType, c);
					
				} else if(isAdviceAround) {
					
					isAdviceAround = false;
					pointcutPanel.remove(mAdviceRetType);
					mAdviceRetType = null;
				}
				
				
				pointcutPanel.validate();
				pointcutPanel.repaint();
			}
		});
		
		
		
		//Button Action Listeners
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String retType = "";
				if(isAdviceAround) {
					retType = mAdviceRetType.getText() + " ";
				}
				
				String selectedAdvice = ADVICE_OPTIONS[adviceOptions.getSelectedIndex()];
				String selectedPointcut = createdPointcuts.get(listPointcuts.getSelectedIndex());
				String adviceBody = textArea.getText();
				
				String wholeAdvice = retType + selectedAdvice + "() : " + selectedPointcut + "() { \n"
						+ adviceBody + " \n"
						+ "} \n";
				
				mListener.saveAdvice(wholeAdvice);
				mDialog.dispose();

			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mDialog.dispose();				
			}
		});
		
		


		//add panels to dialog
		Container contentPane = mDialog.getContentPane();
		contentPane.add(pointcutPanel, BorderLayout.NORTH);	
		contentPane.add(contentPanel, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		mDialog.setVisible(true);
	}


	/*******************************************************************************************/
	/********************************* Helper Methods ******************************************/
	/*******************************************************************************************/

	//TODO check if necessary
	private void resetLists() {


		listJoinpointButtonList = new ArrayList<>();
		listJoinpointText = new ArrayList<>();
		listJoinpoints = new ArrayList<>();
		listJoinpointType = new ArrayList<>();
		listJoinpointNot = new ArrayList<>();
		
	}


	/*************************/
	/***** ComboBox Type *****/
	/*************************/

	private JComboBox<String> getComboBox(int type) {

		switch(type) {

		case TYPE_FIELD:
			return new JComboBox<>(POINTCUT_FIELDS);

		case TYPE_METHOD:
			return new JComboBox<>(POINTCUT_METHODS);

		case TYPE_CONSTRUCTOR:
			return new JComboBox<>(POINTCUT_CONSTRUCTORS);

		case TYPE_POINTCUT_OR_TYPES:
			return new JComboBox<>(POINTCUT_TYPES);

		}

		return null;
	}
	
	/**************************/
	/***** ComboBox Model *****/
	/**************************/
	private DefaultComboBoxModel<String> getComboBoxModel(int type) {
		
		switch(type) {

		case TYPE_FIELD:
			return new DefaultComboBoxModel<>(POINTCUT_FIELDS);

		case TYPE_METHOD:
			return new DefaultComboBoxModel<>(POINTCUT_METHODS);

		case TYPE_CONSTRUCTOR:
			return new DefaultComboBoxModel<>(POINTCUT_CONSTRUCTORS);

		case TYPE_POINTCUT_OR_TYPES:
			return new DefaultComboBoxModel<>(POINTCUT_TYPES);

		}

		return null;
		
	}
	
	/****************************************/
	/***** TextField Placeholder String *****/
	/****************************************/
	private String getPHString(int type) {
		
		switch(type) {

		case TYPE_FIELD:
			return PH_FIELD;

		case TYPE_METHOD:
			return PH_METHOD;

		case TYPE_CONSTRUCTOR:
			return PH_CONSTRUCTOR;

		case TYPE_POINTCUT_OR_TYPES:
			return PH_TYPE;

		}

		return null;
		
	}
	
	
	private String getActualString(Object node) {
		
		String s = "";
		
		if(node instanceof FieldNode) {
			
			FieldNode field = (FieldNode) node;
			s = field.getFieldObject().getDeclaringClass().getName() + "." + field.getName();
						
		} else if (node instanceof MethodNode) {
			
			MethodNode method = ((MethodNode) node);
			s = method.getRetType() + " " + method.getMethodObject().getDeclaringClass().getName()
					+ "." + method.getName() + "(";
			
			Class<?>[] methodArgs = method.getArgs();
			for(int i = 0; i < methodArgs.length; i++) {
				s += methodArgs[i].getSimpleName();
				
				if(methodArgs.length > 1 && i != methodArgs.length-1)
					s += ", ";
			}
			s += ")";
			
		} else if (node instanceof ConstructorNode) {
			
			ConstructorNode ctor = ((ConstructorNode) node);
			s =  ctor.getCtorName() + "(";
			
			Class<?>[] ctorArgs = ctor.getCtorArgs();
			for(int i = 0; i < ctorArgs.length; i++) {
				s += ctorArgs[i].getSimpleName();
				
				if(ctorArgs.length > 1 && i != ctorArgs.length-1)
					s += ", ";
			}
			s += ")";
			
		}
		System.out.println(s);
		return s;
		
		//TODO change string to be Class.Method or Class.Field etc
		//TODO DONE! - check if ok
		
		
//		return node.toString();
	}


	/*******************************************************************************************/
	/******************************** Button Listener ******************************************/
	/*******************************************************************************************/


	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch(e.getActionCommand()) {

			case CMD_SAVE:

				PointcutContainer pointcut = new PointcutContainer(
						mPointcutNameField.getText(),
						mPointcutArgs.getText(),
						mJoinpointRelations.getSelectedItem().toString());

				for(int i = 0; i < listJoinpointText.size(); i++) {
					JoinpointContainer j = new JoinpointContainer(
							listJoinpoints.get(i).getSelectedItem().toString(),
							listJoinpointText.get(i).getText(),
							listJoinpointNot.get(i).isSelected());

					pointcut.addJoinpoint(j);
				}
				
				System.out.println(pointcut);

				mListener.savePointcut(pointcut);
				mDialog.dispose();
				resetLists();

				break;


			case CMD_CANCEL:

				mDialog.dispose();
				resetLists();

				break;


			case JOINPOINT_TYPE:
				
				int index = listJoinpointType.indexOf((JComboBox<?>) e.getSource());
				JComboBox<String> j = listJoinpoints.get(index);
				JTextField t = listJoinpointText.get(index);
				int type = ((JComboBox<?>) e.getSource()).getSelectedIndex();
				t.setText(getPHString(type));
				j.setModel(getComboBoxModel(type));

				//TODO should textfield change? what if the user already have input in text field????
				break;
				
		

			}
		}
	}


}
