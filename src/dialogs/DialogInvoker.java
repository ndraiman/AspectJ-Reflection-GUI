package dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import main.TestWindow;

public class DialogInvoker {
	
	private static final String[] pointcutOptions = {"call", "execution", "get", "set", "initialization",
		"preinitialization", "staticinitialization", "handler", "adviceexecution", "within", "withincode",
		"cflow", "cflowbelow", "this", "target", "args", "PointcutId", "if", "!", "&&", "||", "( )"};
	
	private static final String[] pointcutMembers = {"get", "set"};
	private static final String[] pointcutMethods = {"call", "execution", "withincode"};
	private static final String[] pointcutConstructors = {"call", "execution", "initialization",
		"preinitialization", "withincode"};
	
	//TODO add pointcut list for "Pointcut" and "Type"
	
	public static final int TYPE_MEMBER = 1;
	public static final int TYPE_METHOD = 2;
	public static final int TYPE_CONSTRUCTOR = 3;
	
	
	public static void invokePointcutDialog(Frame owner, int type) {
		//TODO add relation option between pointcut options (if\or)
		JDialog dialog = new JDialog(owner);
		
		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		dialog.setTitle("PointCut");
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new GridLayout(1, 2));
		namePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JPanel catchPanel = new JPanel();
		catchPanel.setLayout(new GridLayout(1, 2));
		catchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField nameTextField = new JTextField("test()", 15);
		JComboBox pointcutList;
		
		switch(type) {
			
		case TYPE_MEMBER:
			 pointcutList = new JComboBox(pointcutMembers);
			 break;
			 
		case TYPE_METHOD:
			pointcutList = new JComboBox(pointcutMethods);
			break;
		
		case TYPE_CONSTRUCTOR:
			pointcutList = new JComboBox(pointcutConstructors);
			break;
			
		default:
			pointcutList = new JComboBox(pointcutOptions);
			break;
		
		}
		
		
		pointcutList.setSelectedIndex(0);
		pointcutList.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField catchTextField = new JTextField("* Object.Method(Var)", 15);
		
		namePanel.add(nameLabel);
		namePanel.add(nameTextField);
		catchPanel.add(pointcutList);
		catchPanel.add(catchTextField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton btnOk = new JButton("Save");
		JButton btnCancel = new JButton("Cancel");
		JButton btnAddField = new JButton("Add new field");

		buttonPanel.add(btnAddField);
		buttonPanel.add(btnOk);
		buttonPanel.add(btnCancel);
		
		btnAddField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewPointcut(dialog, catchPanel);
				
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO save Pointcut
				System.out.println("saving pointcut");
				
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				
			}
		});
		
		contentPane.add(namePanel, BorderLayout.NORTH);
		contentPane.add(catchPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		
		dialog.pack();
		dialog.setVisible(true);
	}
	
	/**********************************************/
	
	private static void addNewPointcut(JDialog dialog, JPanel panel) {
		
		System.out.println("New Field...");
		
		JComboBox<String> pointcutList = new JComboBox<>(pointcutOptions);
		pointcutList.setSelectedIndex(0);
		pointcutList.setBorder(new EmptyBorder(10, 10, 10, 10));
		JTextField catchTextField = new JTextField("* Object.Method(Var)", 15);
		
		String pathToImage = "Test/res/close.png";
		ImageIcon closeIcon = new ImageIcon(DialogInvoker.class.getResource("/close.png"));
		//TODO scale down icon image
		JButton btnClose = new JButton(closeIcon);
		
		
		int rows = ((GridLayout) panel.getLayout()).getRows();
		((GridLayout) panel.getLayout()).setRows(rows + 1);
		
		panel.add(pointcutList);
		panel.add(catchTextField);
		panel.add(btnClose);
		
		panel.validate();
		panel.repaint();
		dialog.pack();
	}

}
