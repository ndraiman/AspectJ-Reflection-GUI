import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.JTree;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.JLabel;


public class TestWindow {

	private JFrame frame;

	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestWindow window = new TestWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(10);
		textArea.setColumns(10);
		panel.add(textArea);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Open FileDialog");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				loadFile();
				
			}
		});
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_1.add(btnNewButton);
		
		JLabel label = new JLabel("");
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("");
		panel_1.add(label_1);
		
		JButton btnNewButton_1 = new JButton("New button");
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		panel_1.add(btnNewButton_2);
	}
	
	
	
	void loadFile() {
		
		File myFilename;
		JFileChooser chooser = new JFileChooser();
		OpenFileFilter javaFilter = new OpenFileFilter("java","*.java File");
		chooser.addChoosableFileFilter(javaFilter);
		chooser.setFileFilter(javaFilter);
		int returnVal = chooser.showSaveDialog(frame);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		     myFilename = chooser.getSelectedFile();
		     //do something with the file
		     URL fileURL;
			try {
				
				fileURL = myFilename.toURI().toURL();
				URL[] url = new URL[]{fileURL};
			    ClassLoader cl = new URLClassLoader(url);
			     
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		}
	}

}
