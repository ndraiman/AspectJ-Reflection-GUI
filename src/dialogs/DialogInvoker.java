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

import extras.PointcutContainer;

public class DialogInvoker {

	private final String[] POINTCUT_OPTIONS = {"call", "execution", "get", "set", "initialization",
			"preinitialization", "staticinitialization", "handler", "adviceexecution", "within", "withincode",
			"cflow", "cflowbelow", "this", "target", "args", "PointcutId", "if", "!", "&&", "||", "( )"};

	private final String[] POINTCUT_MEMBERS = {"get", "set"};
	private final String[] POINTCUT_METHODS = {"call", "execution", "withincode"};
	private final String[] POINTCUT_CONSTRUCTORS = {"call", "execution", "initialization",
			"preinitialization", "withincode"};


	private final String[] ADVICE_OPTIONS = {"before", "after", "around"};

	//TODO add pointcut list for "Pointcut" and "Type"

	public static final int TYPE_MEMBER = 1;
	public static final int TYPE_METHOD = 2;
	public static final int TYPE_CONSTRUCTOR = 3;

	private final int JOINPOINT_COLUMNS = 3;

	private List<JButton> listJoinpointButtonList; // = new ArrayList<>();
	private List<JTextField> listJoinpointText; // = new ArrayList<>();
	private List<JComboBox> listJoinpointType; // = new ArrayList<>();

	private DialogListener mListener;
	private JFrame mParentFrame;
	private JDialog mDialog;
	private JTextField mPointcutNameField;
	
	
	private String mPHPointcutName = "MyPointcut";
	private String mPHVar = "var";
	private String mPHMethod = "retval Object.Method(arg)";


	/*******************************************************************************************/
	/*******************************************************************************************/
	/*******************************************************************************************/


	public DialogInvoker(JFrame owner, DialogListener listener) {

		mListener = listener;
		mParentFrame = owner;
		listJoinpointButtonList = new ArrayList<>();
		listJoinpointText = new ArrayList<>();
		listJoinpointType = new ArrayList<>();

	}


	/*******************************************************************************************/
	/*******************************************************************************************/
	/*******************************************************************************************/

	public void pointcutDialog(int type) {

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
		mPointcutNameField = new JTextField(mPHPointcutName, 15);
		namePanel.add(nameLabel);
		namePanel.add(mPointcutNameField);


		//joinpoint panel
		JPanel joinpointPanel = new JPanel();
		joinpointPanel.setLayout(new GridLayout(1, JOINPOINT_COLUMNS));
		joinpointPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JComboBox pointcutList = getComboBox(type);
		pointcutList.setSelectedIndex(0);
		pointcutList.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField joinpointTextField = new JTextField(mPHMethod, 15);

		joinpointPanel.add(pointcutList);
		joinpointPanel.add(joinpointTextField);
		joinpointPanel.add(new JPanel()); //add empty panel for 3rd col

		listJoinpointText.add(joinpointTextField);
		listJoinpointType.add(pointcutList);

		//button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton btnOk = new JButton("Save");
		JButton btnCancel = new JButton("Cancel");
		JButton btnAddField = new JButton("Add new joinpoint");

		buttonPanel.add(btnAddField);
		buttonPanel.add(btnOk);
		buttonPanel.add(btnCancel);

		btnOk.setActionCommand("OK");
		btnCancel.setActionCommand("Cancel");

		MyButtonListener btnListener = new MyButtonListener();
		btnOk.addActionListener(btnListener);
		btnCancel.addActionListener(btnListener);

		btnAddField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addNewJoinpoint(joinpointPanel, type);

			}
		});



		contentPane.add(namePanel, BorderLayout.NORTH);
		contentPane.add(joinpointPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		mDialog.setResizable(false);
		mDialog.pack();
		mDialog.setVisible(true);
	}

	/***********************************************/
	/***********************************************/
	/***********************************************/

	private void addNewJoinpoint(JPanel panel, int type) {

		System.out.println("New Field...");

		JComboBox<String> pointcutList = getComboBox(type);
		pointcutList.setSelectedIndex(0);
		pointcutList.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField joinpointTextField = new JTextField(mPHMethod, 15);

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
				listJoinpointType.remove(btnIndex);
				panel.remove(index);
				panel.remove(index - 1);
				panel.remove(index - 2);

				int rows = ((GridLayout) panel.getLayout()).getRows();
				((GridLayout) panel.getLayout()).setRows(rows - 1);

				panel.validate();
				panel.repaint();
				mDialog.pack();
			}
		});


		panel.add(pointcutList);
		panel.add(joinpointTextField);
		panel.add(closePanel);

		listJoinpointButtonList.add(btnClose);
		listJoinpointText.add(joinpointTextField);
		listJoinpointType.add(pointcutList);

		System.out.println(listJoinpointButtonList.size());

		panel.validate();
		panel.repaint();
		mDialog.pack();
	}


	/***********************************************/
	/***********************************************/
	/***********************************************/

	private JComboBox getComboBox(int type) {

		switch(type) {

		case TYPE_MEMBER:
			return new JComboBox(POINTCUT_MEMBERS);

		case TYPE_METHOD:
			return new JComboBox(POINTCUT_METHODS);

		case TYPE_CONSTRUCTOR:
			return new JComboBox(POINTCUT_CONSTRUCTORS);

		default:
			return new JComboBox(POINTCUT_OPTIONS);

		}
	}


	/*******************************************************************************************/
	/*******************************************************************************************/
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

		JButton btnOk = new JButton("Save");
		buttonPane.add(btnOk);
		mDialog.getRootPane().setDefaultButton(btnOk);


		JButton btnCancel = new JButton("Cancel");
		buttonPane.add(btnCancel);

		btnOk.addActionListener(new ActionListener() {

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
			createdPointcuts.add(pointcuts.get(i).toString());
		}
		JComboBox listPointcuts = new JComboBox(createdPointcuts);
		JComboBox adviceOptions = new JComboBox(ADVICE_OPTIONS);
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
	/*******************************************************************************************/
	/*******************************************************************************************/


	private void resetLists() {


		listJoinpointButtonList = new ArrayList<>();
		listJoinpointText = new ArrayList<>();
		listJoinpointType = new ArrayList<>();

	}


	/*******************************************************************************************/
	/*******************************************************************************************/
	/*******************************************************************************************/


	private class MyButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch(e.getActionCommand()) {

			case "OK":

				PointcutContainer pointcut = new PointcutContainer(mPointcutNameField.getText());
				System.out.println(pointcut.getName());


				//TODO create pointcut container
				PointcutContainer p = new PointcutContainer(null); //TODO PLACEHOLDER
				mListener.savePointcut(p);
				mDialog.dispose();
				listJoinpointButtonList = new ArrayList<>();
				listJoinpointText = new ArrayList<>();
				listJoinpointType = new ArrayList<>();

				break;


			case "Cancel":

				mDialog.dispose();
				resetLists();

				break;

			}
		}
	}


}
