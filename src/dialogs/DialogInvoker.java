package dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

	/*********************/
	/***** Variables *****/
	/*********************/
	private DialogListener mListener;
	private JFrame mParentFrame;
	private JDialog mDialog;
	private JTextField mPointcutNameField;
	
	private final int JOINPOINT_COLUMNS = 4;

	/*******************************/
	/***** Placeholder Strings *****/
	/*******************************/
	private final String PH_POINTCUT_NAME = "MyPointcut";
	private final String PH_FIELD = "field";
	private final String PH_METHOD = "retval Object.Method(arg)";
	private final String PH_CONSTRUCTOR = "constructor()";
	private final String PH_TYPE = "otherPointcut or Type";
	
	
	/***********************************/
	/***** Action Commands Strings *****/
	/***********************************/
	private final String CMD_SAVE = "save";
	private final String CMD_CANCEL = "cancel";
	private final String COMBO_BOX_TYPE = "joinpoint type";
	
	
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

	}


	/*******************************************************************************************/
	/********************************* Pointcut Dialog *****************************************/
	/*******************************************************************************************/

	public void pointcutDialog(int type, Object nodeContainer) {

		//TODO add relation option between pointcut options (not\and\or)
		mDialog = new JDialog(mParentFrame);

		Container contentPane = mDialog.getContentPane();
		contentPane.setLayout(new BorderLayout());

		mDialog.setTitle("PointCut");

		//pointcut name panel
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new GridLayout(1, 2));
		namePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mPointcutNameField = new JTextField(PH_POINTCUT_NAME, 15);
		namePanel.add(nameLabel);
		namePanel.add(mPointcutNameField);



		//joinpoint panel
		JPanel joinpointPanel = new JPanel();
		joinpointPanel.setLayout(new GridLayout(1, JOINPOINT_COLUMNS));
		joinpointPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JComboBox<String> typeList = new JComboBox<>(JOINPOINT_TYPES);
		typeList.setSelectedIndex(type);
		typeList.setBorder(new EmptyBorder(10, 10, 10, 10));

		JComboBox<String> joinpoints = getComboBox(type);
		joinpoints.setSelectedIndex(0);
		joinpoints.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField joinpointTextField = new JTextField(nodeContainer == null ? getPHString(type) : getActualString(nodeContainer), 15);

		joinpointPanel.add(typeList);
		joinpointPanel.add(joinpoints);
		joinpointPanel.add(joinpointTextField);
		joinpointPanel.add(new JPanel()); //add empty panel for 3rd col

		listJoinpointText.add(joinpointTextField);
		listJoinpoints.add(joinpoints);
		listJoinpointType.add(typeList);


		
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
		typeList.setActionCommand(COMBO_BOX_TYPE);

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
		JTextField joinpointTextField = new JTextField(getPHString(type), 15);

		JComboBox<String> typeList = new JComboBox<>(JOINPOINT_TYPES);
		typeList.setSelectedIndex(type);
		typeList.setBorder(new EmptyBorder(10, 10, 10, 10));

		ImageIcon closeIcon = new ImageIcon(DialogInvoker.class.getResource("/close.png"));
		JButton btnClose = new JButton(closeIcon);
		JPanel closePanel = new JPanel();
		closePanel.add(btnClose);


		int rows = ((GridLayout) panel.getLayout()).getRows();
		((GridLayout) panel.getLayout()).setRows(rows + 1);

		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JButton thisButton = (JButton) e.getSource();

				int btnIndex = listJoinpointButtonList.indexOf(thisButton);
				int index = ((btnIndex + 1) * JOINPOINT_COLUMNS) + (JOINPOINT_COLUMNS - 1);
				System.out.println(index);

				listJoinpointButtonList.remove(btnIndex);
				listJoinpointText.remove(btnIndex);
				listJoinpoints.remove(btnIndex);
				listJoinpointType.remove(btnIndex);
				panel.remove(index);
				panel.remove(index - 1);
				panel.remove(index - 2);
				panel.remove(index - 3);

				int rows = ((GridLayout) panel.getLayout()).getRows();
				((GridLayout) panel.getLayout()).setRows(rows - 1);

				panel.validate();
				panel.repaint();
				mDialog.pack();
			}
		});
		
		typeList.setActionCommand(COMBO_BOX_TYPE);
		typeList.addActionListener(listener);

		
		
		panel.add(typeList);
		panel.add(joinpoints);
		panel.add(joinpointTextField);
		panel.add(closePanel);

		listJoinpointButtonList.add(btnClose);
		listJoinpointText.add(joinpointTextField);
		listJoinpoints.add(joinpoints);
		listJoinpointType.add(typeList);

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

		mDialog.setBounds(100, 100, 450, 300);
		mDialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout(0, 0));



		//add TextArea
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);

		RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		contentPanel.add(scrollPane, BorderLayout.CENTER);



		//add Buttons
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton btnSave = new JButton("Save");
		buttonPane.add(btnSave);
		mDialog.getRootPane().setDefaultButton(btnSave);


		JButton btnCancel = new JButton("Cancel");
		buttonPane.add(btnCancel);

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				mListener.saveAdvice(textArea.getText());
				mDialog.dispose();

			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mDialog.dispose();				
			}
		});



		//add pointcuts
		pointcutPanel.setLayout(new GridLayout(1, 2));
		pointcutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		Vector<String> createdPointcuts = new Vector<>();
		for(int i = 0; i < pointcuts.size(); i++) {
			createdPointcuts.add(pointcuts.get(i).getName().toString());
		}
		JComboBox<String> listPointcuts = new JComboBox<>(createdPointcuts);
		JComboBox<String> adviceOptions = new JComboBox<>(ADVICE_OPTIONS);
		pointcutPanel.add(adviceOptions);
		pointcutPanel.add(listPointcuts);



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
	
	
	private String getActualString(Object nodeContainer) {
		
//		if(nodeContainer instanceof FieldNode) {
//			
//			return nodeContainer.toString();
//			
//		} else if (nodeContainer instanceof MethodNode) {
//			
//			
//		} else if (nodeContainer instanceof ConstructorNode) {
//			
//			
//		}
//		
//		return "";
		
		//TODO change string to be Class.Method or Class.Field etc
		return nodeContainer.toString();
	}


	/*******************************************************************************************/
	/******************************** Button Listener ******************************************/
	/*******************************************************************************************/


	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch(e.getActionCommand()) {

			case CMD_SAVE:

				PointcutContainer pointcut = new PointcutContainer(mPointcutNameField.getText());

				for(int i = 0; i < listJoinpointText.size(); i++) {
					JoinpointContainer j = new JoinpointContainer(
							listJoinpoints.get(i).getSelectedItem().toString(),
							listJoinpointText.get(i).getText());

					pointcut.addJoinpoint(j);
				}

				mListener.savePointcut(pointcut);
				mDialog.dispose();
				resetLists();

				break;


			case CMD_CANCEL:

				mDialog.dispose();
				resetLists();

				break;


			case COMBO_BOX_TYPE:
				
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
