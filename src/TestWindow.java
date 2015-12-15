import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;


public class TestWindow {

	private JFrame frame;
	private JTree tree;

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
	
	/*********************************************************/
	/*********************************************************/
	/*********************************************************/

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel treePanel = new JPanel();
		treePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(treePanel, BorderLayout.CENTER);
		treePanel.setLayout(new BorderLayout(0, 0));
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Loaded Classes");
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		MouseListener mouse = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int selectedRow = tree.getRowForLocation(e.getX(), e.getY());
				if(selectedRow != -1 && e.getClickCount() == 2) {
					treeDoubleClick(selectedRow);
				}
				
			}
		};
		
		tree.addMouseListener(mouse);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		treePanel.add(scrollPane, BorderLayout.CENTER);
		

		
		JPanel buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnLoadFile = new JButton("Load Class/Jar");
		btnLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
				((DefaultMutableTreeNode) model.getRoot()).removeAllChildren();
				loadFile();
				model.reload();
			}
		});
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.add(btnLoadFile);
		
		JButton btnNewButton_1 = new JButton("Test Button 1");
		buttonPanel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Test Button 2");
		buttonPanel.add(btnNewButton_2);
	}
	
	
	/*********************************************************/
	/*********************************************************/
	/*********************************************************/
	
	void treeDoubleClick(int row) {
		
//		tree.getSelectionPath().getPathComponent(row);
		tree.getLastSelectedPathComponent();
	}
	
	
	/*********************************************************/
	/*********************************************************/
	/*********************************************************/
	
	void loadFile() {
		
		File myFile;
		JFileChooser chooser = new JFileChooser();
		OpenFileFilter jarFilter = new OpenFileFilter("jar","*.jar File");
		OpenFileFilter classFilter = new OpenFileFilter("class","*.class File");
		chooser.addChoosableFileFilter(jarFilter);
		chooser.addChoosableFileFilter(classFilter);
		chooser.setFileFilter(jarFilter);
		int returnVal = chooser.showSaveDialog(frame);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    myFile = chooser.getSelectedFile();
		    
		    readFile(myFile);
		    
		} else {
			//Print Error
		}
	}
	
	
	/*********************************************************/
	/*********************************************************/
	/*********************************************************/
	
	void readFile(File myFile) {
			
			try {
				
				URL myJarUrl = new URL("jar","","file:" + myFile.getAbsolutePath() + "!/");
				URL myFileUrl = new URL("file:///" + myFile.getParent() + "/");
				System.out.println(myFileUrl);
			
				URLClassLoader sysLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
				
				Class<URLClassLoader> sysClass = URLClassLoader.class;
				Method sysMethod = sysClass.getDeclaredMethod("addURL",new Class[] {URL.class});
				sysMethod.setAccessible(true);
				
				URLClassLoader cl = null;
				Class<?> loadedClass = null;
				
				if(myFile.getName().endsWith(".jar")) {
					
					System.out.println("Jar Loader"); //DEBUG
					sysMethod.invoke(sysLoader, new Object[]{myJarUrl});
					cl = URLClassLoader.newInstance(new URL[] {myJarUrl});
					
				} else if (myFile.getName().endsWith(".class")) {
					
					System.out.println("Class Loader"); //DEBUG
					sysMethod.invoke(sysLoader, new Object[]{myFileUrl});
					cl = URLClassLoader.newInstance(new URL[] {myFileUrl});
					
				
				
					//trying to load class file
					String filenameWithoutExt = myFile.getName().substring(0, myFile.getName().lastIndexOf('.'));
					Class<?> c = cl.loadClass(filenameWithoutExt);
					
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
					root.add(new DefaultMutableTreeNode(c.getName()));
				
				}
				
				
				
				
				
				//trying to load jar file
				System.out.println("jar path = " + myFile.getAbsolutePath()); //DEBUG
				
				List<String> classNames = new ArrayList<String>();
				ZipInputStream zip = new ZipInputStream(new FileInputStream(myFile.getAbsolutePath()));
				
				for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				    if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
				        // This ZipEntry represents a class. Now, what class does it represent?
				        String className = entry.getName().replace('/', '.'); // including ".class"
				        classNames.add(className.substring(0, className.length() - ".class".length()));
				    }
				}
				
				//DEBUG
				List<Class<?>> classes = new ArrayList<Class<?>>();
				for(String s : classNames) {
					classes.add(cl.loadClass(s));
				}
				
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
				
				for(Class<?> c : classes) {
					root.add(new DefaultMutableTreeNode(c.getName()));
				}
				
		
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	/*********************************************************/
	/*********************************************************/
	/*********************************************************/

}
