package Dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class DialogInvoker {
	
	
	public static void invokePointcutDialog(Frame owner) {
		
		JDialog dialog = new JDialog(owner);
//		dialog.setBounds(100, 100, 300, 150);
		
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
		JTextField nameTextField = new JTextField("test()", 15);
		JLabel catchLabel = new JLabel("Catch: ");
		JTextArea catchTextArea = new JTextArea("call(* Object.Method(Var))", 10, 15);
		catchTextArea.setBorder(new EtchedBorder());
		
		namePanel.add(nameLabel);
		namePanel.add(nameTextField);
		catchPanel.add(catchLabel);
		catchPanel.add(catchTextArea);
		
		
//		inputPanel.setLayout(layout);
//		inputPanel.add(nameLabel);
//		inputPanel.add(nameTextField);
//		inputPanel.add(catchLabel);
//		inputPanel.add(catchTextField);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton btnOk = new JButton("Save");
		JButton btnCancel = new JButton("Cancel");
		buttonPanel.add(btnOk);
		buttonPanel.add(btnCancel);
		
		contentPane.add(namePanel, BorderLayout.NORTH);
		contentPane.add(catchPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		
		dialog.pack();
		dialog.setVisible(true);
	}

}
