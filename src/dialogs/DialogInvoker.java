package dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.CatchParameterBinding;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import extras.PointcutContainer;
import main.TestWindow;

public class DialogInvoker {
	
	private static final String[] POINTCUT_OPTIONS = {"call", "execution", "get", "set", "initialization",
		"preinitialization", "staticinitialization", "handler", "adviceexecution", "within", "withincode",
		"cflow", "cflowbelow", "this", "target", "args", "PointcutId", "if", "!", "&&", "||", "( )"};
	
	private static final String[] POINTCUT_MEMBERS = {"get", "set"};
	private static final String[] POINTCUT_METHODS = {"call", "execution", "withincode"};
	private static final String[] POINTCUT_CONSTRUCTORS = {"call", "execution", "initialization",
		"preinitialization", "withincode"};
	
	
	private static final String[] ADVICE_OPTIONS = {"before", "after", "around"};
	
	//TODO add pointcut list for "Pointcut" and "Type"
	
	public static final int TYPE_MEMBER = 1;
	public static final int TYPE_METHOD = 2;
	public static final int TYPE_CONSTRUCTOR = 3;
	
	private static final int JOINPOINT_COLUMNS = 3;
	
	/*******************************************************************************************/
	/*******************************************************************************************/
	/*******************************************************************************************/
	
	public static void pointcutDialog(JFrame owner, DialogListener listener, int type) {
		//TODO add relation option between pointcut options (not\and\or)
		JDialog dialog = new JDialog(owner);
		
		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		dialog.setTitle("PointCut");
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new GridLayout(1, 2));
		namePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel joinpointPanel = new JPanel();
		joinpointPanel.setLayout(new GridLayout(1, JOINPOINT_COLUMNS));
		joinpointPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField nameTextField = new JTextField("myPointcut()", 15);
		
		JComboBox pointcutList = getComboBox(type);
		pointcutList.setSelectedIndex(0);
		pointcutList.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField catchTextField = new JTextField("* Object.Method(Var)", 15);
		
		namePanel.add(nameLabel);
		namePanel.add(nameTextField);
		joinpointPanel.add(pointcutList);
		joinpointPanel.add(catchTextField);
		joinpointPanel.add(new JPanel()); //add empty panel for 3rd col
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton btnOk = new JButton("Save");
		JButton btnCancel = new JButton("Cancel");
		JButton btnAddField = new JButton("Add new joinpoint");

		buttonPanel.add(btnAddField);
		buttonPanel.add(btnOk);
		buttonPanel.add(btnCancel);
		
		btnAddField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewJoinpoint(dialog, joinpointPanel, type);
				
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO create pointcut container
				PointcutContainer p = new PointcutContainer(null); //TODO PLACEHOLDER
				listener.savePointcut(p);
				dialog.dispose();
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				
			}
		});
		
		
		contentPane.add(namePanel, BorderLayout.NORTH);
		contentPane.add(joinpointPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		
		dialog.pack();
		dialog.setVisible(true);
	}
	
	/***********************************************/
	/***********************************************/
	/***********************************************/
	
	private static void addNewJoinpoint(JDialog dialog, JPanel panel, int type) {
		
		System.out.println("New Field...");
		
		JComboBox<String> pointcutList = getComboBox(type);
		pointcutList.setSelectedIndex(0);
		pointcutList.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField catchTextField = new JTextField("* Object.Method(Var)", 15);
		
		ImageIcon closeIcon = new ImageIcon(DialogInvoker.class.getResource("/close.png"));
		JButton btnClose = new JButton(closeIcon);
		JPanel closePanel = new JPanel();
		closePanel.add(btnClose);
		
		
		int rows = ((GridLayout) panel.getLayout()).getRows();
		((GridLayout) panel.getLayout()).setRows(rows + 1);
		
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO fix row removal
				int c = ((GridLayout) panel.getLayout()).getColumns();
				int index = rows * JOINPOINT_COLUMNS + c - 1;
				panel.remove(index);
				panel.remove(index - 1);
				panel.remove(index - 2);
				((GridLayout) panel.getLayout()).setRows(rows);
				panel.validate();
				panel.repaint();
				dialog.pack();
			}
		});
		
		
		panel.add(pointcutList);
		panel.add(catchTextField);
		panel.add(closePanel);
		
		panel.validate();
		panel.repaint();
		dialog.setResizable(false);
		dialog.pack();
	}
	
	
	/***********************************************/
	/***********************************************/
	/***********************************************/
	
	private static JComboBox getComboBox(int type) {
		
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
	
	
	public static void adviceDialog(JFrame owner, DialogListener listener, List<PointcutContainer> pointcuts) {
		
		JDialog dialog = new JDialog(owner);
		dialog.setTitle("Advice");
		
		final JPanel contentPanel = new JPanel();
		final RSyntaxTextArea textArea = new RSyntaxTextArea();
		JPanel buttonPane = new JPanel();
		JPanel pointcutPanel = new JPanel();
		
		dialog.setBounds(100, 100, 450, 300);
		dialog.getContentPane().setLayout(new BorderLayout());
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
		dialog.getRootPane().setDefaultButton(btnOk);
			
			
		JButton btnCacnel = new JButton("Cancel");
		buttonPane.add(btnCacnel);
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.saveAdvice(textArea.getText());
				dialog.dispose();
			}
		});
		
		btnCacnel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();				
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
		Container contentPane = dialog.getContentPane();
		contentPane.add(pointcutPanel, BorderLayout.NORTH);	
		contentPane.add(contentPanel, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}
	
	
}
