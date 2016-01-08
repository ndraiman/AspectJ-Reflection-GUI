package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import console.CustomOutputStream;
import console.MessageConsole;
import testPackage.Car;
import treeNodes.ClassNode;
import treeNodes.ConstructorNode;
import treeNodes.FieldNode;
import treeNodes.MethodNode;
import dialogs.DialogInvoker;
import dialogs.DialogListener;
import extras.AdviceContainer;
import extras.AjcRunner;
import extras.OpenFileFilter;
import extras.PointcutContainer;


public class MainWindow implements DialogListener {

	private List<PointcutContainer> mPointcuts;
	private List<AdviceContainer> mAdvices;
	private String mInPath;

	private JFrame mFrame;
	private JTree mTree;
	
	private boolean isJar = false;
	
	private final String INSTRUCTIONS = "Double click a Constructor/Method/Field\ninside a class to quickly create a Pointcut for it";
	
	/*************************/
	/***** Button Labels *****/
	/*************************/
	private final String LBL_LOAD_FILE = "Load Class/Jar";
	private final String LBL_POINTCUT = "Add Pointcut";
	private final String LBL_ADVICE = "Add Advice";
	private final String LBL_COMPILE = "Compile";
	
	
	/************************************************************************************************************/
	/************************************************************************************************************/
	/************************************************************************************************************/


	public JFrame getFrame() {
		return mFrame;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		
		mPointcuts = new ArrayList<>();
		mAdvices = new ArrayList<>();

		initialize();
	}

	/************************************************************************************************************/
	/**************************************** Initialize Window *************************************************/
	/************************************************************************************************************/

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		mFrame = new JFrame();
		mFrame.setBounds(100, 100, 450, 600);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		mFrame.setTitle("AspectJ GUI");

		//Open Frame in middle of screen
		//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

		
		/*****************************/
		/***** Instruction Panel *****/
		/*****************************/
		JPanel instructionPanel = new JPanel();
		JTextPane instructions = new JTextPane();
		instructions.setText(INSTRUCTIONS);
		instructions.setEditable(false);
		instructionPanel.add(instructions);
		
		//Make text align center 
		StyledDocument doc = instructions.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		/**********************/
		/***** Tree Panel *****/
		/**********************/
		JPanel treePanel = new JPanel();
		treePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		treePanel.setLayout(new BorderLayout(0, 0));

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Loaded Classes");
		mTree = new JTree(root);
		mTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		//		tree.addTreeSelectionListener(this); //Remove listener implementation if this is removed
		
		//Mouse Listener
		MouseListener mouse = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int selectedRow = mTree.getRowForLocation(e.getX(), e.getY());
				if(selectedRow != -1 && e.getClickCount() == 2) {
					treeDoubleClick(selectedRow);
				}

			}
		};

		mTree.addMouseListener(mouse);

		JScrollPane scrollPane = new JScrollPane(mTree);
		treePanel.add(scrollPane, BorderLayout.CENTER);

		/************************/
		/***** Button Panel *****/
		/************************/
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnLoadFile = new JButton(LBL_LOAD_FILE);
		btnLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				loadFile();

			}
		});
		
		
		JButton btnPointcut = new JButton(LBL_POINTCUT);
		btnPointcut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogInvoker(mFrame, MainWindow.this).pointcutDialog(0, null);				
			}
		});
		

		JButton btnAdvice = new JButton(LBL_ADVICE);
		btnAdvice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				new DialogInvoker(mFrame, MainWindow.this).adviceDialog(mPointcuts);

			}
		});

		JButton btnCompile = new JButton(LBL_COMPILE);
		btnCompile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				compileWithAspects();
			}
		});
		
		
		
		buttonPanel.add(btnLoadFile);
		buttonPanel.add(btnPointcut);
		buttonPanel.add(btnAdvice);
		buttonPanel.add(btnCompile);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(instructionPanel, BorderLayout.NORTH);
		contentPanel.add(treePanel, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		
//		frame.getContentPane().add(instructionPanel, BorderLayout.NORTH);
//		frame.getContentPane().add(treePanel, BorderLayout.CENTER);
//		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
//		JPanel consolePanel = initConsolePanel();
//
//		mFrame.getContentPane().add(consolePanel, BorderLayout.SOUTH);
		mFrame.getContentPane().add(contentPanel, BorderLayout.CENTER);

		//DEBUG LoadClassDetails
		loadClassDetails(root, Car.class, null);
		((DefaultTreeModel) mTree.getModel()).reload();
	}

	
	
	/************************************************************************************************************/
	/***************************************** Console Panel ****************************************************/
	/************************************************************************************************************/
	
	private JPanel initConsolePanel() {
		
		JPanel consolePanel = new JPanel();
		consolePanel.setLayout(new BorderLayout());
		
		JLabel lblConsole = new JLabel("Console Output: ");
		
//		JTextArea console = new JTextArea(10, 20);
//		console.setEditable(false);
//		console.setBackground(Color.WHITE);
//		console.setLineWrap(true);
		
		//Set update policy to always scroll to bottom of console
//		DefaultCaret caret = (DefaultCaret)console.getCaret();
//		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JTextPane console = new JTextPane();
		console.setBackground(Color.WHITE);
		
		JScrollPane consoleScrollPanel = new JScrollPane (console, 
				   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		consoleScrollPanel.setPreferredSize(new Dimension(100, 200));		
		
		consolePanel.add(consoleScrollPanel, BorderLayout.CENTER);
		consolePanel.add(lblConsole, BorderLayout.NORTH);
		
		MessageConsole mc = new MessageConsole(console);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		
//		PrintStream printStream = new PrintStream(new CustomOutputStream(console)); 
//		System.setOut(printStream);
//		System.setErr(printStream);
		
		
		return consolePanel;
	}

	/************************************************************************************************************/
	/************************************** Handle Tree Clicks **************************************************/
	/************************************************************************************************************/

	void treeDoubleClick(int row) {

		System.out.println("double clicked row " + row);
		//		tree.getSelectionPath().getPathComponent(row);
		DefaultMutableTreeNode selected = (DefaultMutableTreeNode) mTree.getLastSelectedPathComponent();

		if(!selected.isLeaf())
			return;

		System.out.println("Im a leaf!"); //DEBUG
		String name = "";
		int type = -1;
		
		if(selected instanceof FieldNode) {

			name = ((FieldNode) selected).getName();
			type = DialogInvoker.TYPE_FIELD;
			
		} else if (selected instanceof MethodNode) {
			
			name = ((MethodNode) selected).getName();
			type = DialogInvoker.TYPE_METHOD;
			
		} else if (selected instanceof ConstructorNode) {
			
			name = ((ConstructorNode) selected).getCtorName();
			type = DialogInvoker.TYPE_CONSTRUCTOR;
			
		} else {
			return;
		}

		int option = JOptionPane.showConfirmDialog(mFrame, "Create pointcut for: " + name + "?",
				"Create Pointcut", JOptionPane.YES_NO_OPTION);

		if(option == 0) {
			
			new DialogInvoker(mFrame, MainWindow.this).pointcutDialog(type, selected);
			
			//TODO add option to add to an existing pointcut!
		}


	}


	/************************************************************************************************************/
	/***************************** Load Classes' Fields/Methods/Constructors ************************************/
	/************************************************************************************************************/

	//TODO at the moment it loads only un-inherited fields/methods etc
	private void loadClassDetails(DefaultMutableTreeNode parent, Class<?> c, URLClassLoader cl) {
		
		
		ClassNode classNode = new ClassNode(c);
		parent.add(classNode);
		
		//Load Package name
		String classPackage = classNode.getPackageName(); 
		if(classPackage != null) {
			DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(classPackage);
			classNode.add(packageNode);
		}
		
		//Load Superclass
		Class<?> superclass = c.getSuperclass();
		if(superclass != null) {
			DefaultMutableTreeNode superclassNode = new DefaultMutableTreeNode("Superclass: " + superclass.getName());
			classNode.add(superclassNode);
		}

		//Load Constructors
		Constructor<?>[] constructors = c.getDeclaredConstructors();
		DefaultMutableTreeNode ctors;
		if(constructors.length != 0)
			ctors = new DefaultMutableTreeNode("Constructors");
		else
			ctors = new DefaultMutableTreeNode("<-- No Constructors -->");

		for(Constructor<?> ctor : constructors) {

			ConstructorNode ctorNode = new ConstructorNode(ctor);
			classNode.addConstructor(ctorNode);
			ctors.add(ctorNode);
		}
		classNode.add(ctors);


		//Load Fields
		Field[] members = c.getDeclaredFields();
		DefaultMutableTreeNode flds;
		if(members.length != 0)
			flds = new DefaultMutableTreeNode("Fields");
		else
			flds = new DefaultMutableTreeNode("<-- No Fields -->");

		for(Field v : members) {
			FieldNode fldNode = new FieldNode(v);
			classNode.addField(fldNode);
			flds.add(fldNode);
		}
		classNode.add(flds);


		//Load Methods
		Method[] methods = c.getDeclaredMethods();
		DefaultMutableTreeNode funcs;
		if(methods.length != 0)
			funcs = new DefaultMutableTreeNode("Methods");
		else
			funcs = new DefaultMutableTreeNode("<-- No Methods -->");

		for(Method m : methods) {

			MethodNode methodNode = new MethodNode(m);
			classNode.addMethod(methodNode);
			funcs.add(methodNode);
		}
		classNode.add(funcs);


		/**********************************************************************/
		/**********************************************************************/


		//Class Modifier
		System.out.println("Class Modifier: " + Modifier.toString(c.getModifiers()));


		//get Superclass - can be called recursively to get inheritance path
		Class<?> ancestor = c.getSuperclass();
		if(ancestor != null)
			System.out.println("Super Class = " + ancestor.getName());


		//Load Annotations
		Annotation[] ann = c.getAnnotations();
		for(Annotation a : ann) {
			System.out.println("Annotation: " + a.toString());
		}

		//Load Implemented Interfaces
		Type[] intfs = c.getGenericInterfaces();
		for(Type intf : intfs) {
			System.out.println("Implemented Interface: " + intf.toString());
		}


		//TODO Load TypeParameters 
		TypeVariable<?>[] tv = c.getTypeParameters();
		for(TypeVariable<?> t : tv) {
			System.out.println("Type Parameter: " + t.getName());
		}



		//load subclasses - calls this method recursively
		//		DefaultMutableTreeNode subClassesNode = new DefaultMutableTreeNode();
		//		classNode.add(subClassesNode);
		//
		//		Class<?>[] subclasses = c.getDeclaredClasses();
		//		for(Class<?> c0 : subclasses) {
		//			loadClassDetails(subClassesNode, c0, cl);
		//		}


	}


	/************************************************************************************************************/
	/*************************************** File Loading Methods ***********************************************/
	/************************************************************************************************************/

	
	/*************************************/
	/***** Invoke FileChooser Dialog *****/
	/*************************************/
	void loadFile() {

		File myFile;
		JFileChooser chooser = new JFileChooser();

		OpenFileFilter jarFilter = new OpenFileFilter("jar","*.jar File");
		OpenFileFilter classFilter = new OpenFileFilter("class","*.class File");
		chooser.addChoosableFileFilter(jarFilter);
		chooser.addChoosableFileFilter(classFilter);
		chooser.setFileFilter(jarFilter);
//		chooser.setFileFilter(classFilter); //default filter
		chooser.setAcceptAllFileFilterUsed(false);


		int returnVal = chooser.showOpenDialog(mFrame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			//reset JTree
			DefaultTreeModel model = ((DefaultTreeModel) mTree.getModel());
			((DefaultMutableTreeNode) model.getRoot()).removeAllChildren();
			
			myFile = chooser.getSelectedFile();
			mInPath = myFile.getAbsolutePath();

			readFile(myFile);
			
			//expand root node
			model.reload();

		} else {
			System.out.println("filechooser canceled");
			//Print Error
		}
	}
	
	
	/****************************/
	/***** Read loaded file *****/
	/****************************/
	private void readFile(File myFile) {

		try {

			URL myJarUrl = new URL("jar","","file:" + myFile.getAbsolutePath() + "!/");
			URL myFileUrl = new URL("file:///" + myFile.getParent() + "/");
			System.out.println(myFileUrl); //DEBUG

			URLClassLoader sysLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();

			Class<URLClassLoader> sysClass = URLClassLoader.class;
			Method sysMethod = sysClass.getDeclaredMethod("addURL",new Class[] {URL.class});
			sysMethod.setAccessible(true);

			URLClassLoader cl = null;

			if(myFile.getName().endsWith(".jar")) {

				System.out.println("Jar Loader"); //DEBUG
				sysMethod.invoke(sysLoader, new Object[]{myJarUrl});
				cl = URLClassLoader.newInstance(new URL[] {myJarUrl});

				loadJarFile(myFile.getAbsolutePath(), cl);



			} else if (myFile.getName().endsWith(".class")) {

				System.out.println("Class Loader"); //DEBUG
				sysMethod.invoke(sysLoader, new Object[]{myFileUrl});
				cl = URLClassLoader.newInstance(new URL[] {myFileUrl});

				String filenameWithoutExt = myFile.getName().substring(0, myFile.getName().lastIndexOf('.'));

				loadClassFile(filenameWithoutExt, cl);

			}




		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/************************************************************************************************************/
	/************************************* Load File Helper Methods *********************************************/
	/************************************************************************************************************/

	/********************/
	/***** Jar File *****/
	/********************/
	private void loadJarFile(String absolutePath, URLClassLoader cl) {
		
		isJar = true;
		
		try {

			//trying to load jar file
			System.out.println("jar path = " + absolutePath); //DEBUG

			List<String> classNames = new ArrayList<String>();
			ZipInputStream zip = new ZipInputStream(new FileInputStream(absolutePath));

			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					// This ZipEntry represents a class. Now, what class does it represent?
					String className = entry.getName().replace('/', '.'); // including ".class"
					classNames.add(className.substring(0, className.length() - ".class".length()));
				}
			}

			//add jar contents to jtree
			List<Class<?>> classes = new ArrayList<Class<?>>();
			for(String s : classNames) {
				System.out.println("Loading class - " + s); //DEBUG
				classes.add(cl.loadClass(s));
			}

			DefaultMutableTreeNode root = (DefaultMutableTreeNode) mTree.getModel().getRoot();

			for(Class<?> c : classes) {
				//				root.add(new DefaultMutableTreeNode(c.getName()));
				loadClassDetails(root, c, cl);
			}

			zip.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	
	/**********************/
	/***** Class File *****/
	/**********************/
	private void loadClassFile(String className, URLClassLoader cl) {

		//trying to load class file
		isJar = false;

		try {

			Class<?> c = cl.loadClass(className);
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) mTree.getModel().getRoot();			

			loadClassDetails(root, c, cl);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	/************************************************************************************************************/
	/***************************************** Listener Methods *************************************************/
	/************************************************************************************************************/

	@Override
	public void saveAdvice(String input) {
		System.out.println("Advice saved! \n" + input);
		AdviceContainer a = new AdviceContainer(input);
		mAdvices.add(a);
	}

	@Override
	public void savePointcut(PointcutContainer p) {
		System.out.println("Pointcut Saved!");
		mPointcuts.add(p);
	}

	/************************************************************************************************************/
	/***************************************** Compile Aspects **************************************************/
	/************************************************************************************************************/

	private void compileWithAspects() {

		
		/**********************************************/
		/***** get aspect name - provided by user *****/
		/**********************************************/
		String aspectName = JOptionPane.showInputDialog(mFrame,
				"Save Aspect as",
				"Aspect Name?", 
				JOptionPane.QUESTION_MESSAGE);
		
		if(aspectName == null || aspectName.isEmpty())
			return;
		
		/******************************/
		/***** create aspect file *****/
		/******************************/
		String aspectPath = "aspects/" + aspectName + ".aj";
		
		System.out.println("Aspect Name = " + aspectName);
		System.out.println("Path = " + Paths.get(aspectPath).toAbsolutePath());
		
		
		StringBuilder aspectBuilder = new StringBuilder();
		aspectBuilder.append("public aspect " + aspectName + " { \n");
		
		for(PointcutContainer p : mPointcuts) {
			aspectBuilder.append(p + "\n");
		}
		aspectBuilder.append("\n");
		for(AdviceContainer a : mAdvices){
			aspectBuilder.append(a + "\n");
		}
		aspectBuilder.append("} \n");
		
		System.out.println(aspectBuilder.toString());
		
		/*********************************/
		/***** Saving aspect to file *****/
		/*********************************/
		File f = new File(aspectPath);
		boolean dirCreated = f.getParentFile().mkdir(); //TODO remove boolean and print
		System.out.println("dircreated = " + dirCreated);
		
		
		
		Path savePath = Paths.get(aspectPath).toAbsolutePath();
		
		System.out.println("dir Path = " + savePath.getParent().toAbsolutePath().toString());
		//Clean directory
		File dir = new File(savePath.getParent().toAbsolutePath().toString());
		if(dir != null) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if(!file.delete())
					System.out.println("failed to delete file");
			}
		}
		
		byte[] data = aspectBuilder.toString().getBytes();
		
		try {
			
			Files.write(savePath,
					data, 
					StandardOpenOption.CREATE, 
					StandardOpenOption.WRITE, 
					StandardOpenOption.TRUNCATE_EXISTING);
			
			if(!isJar) {
				//FIXME maybe disable this incase of JAR
				//Copy class\Jar file to program dir
				Path src_path = Paths.get(mInPath);
				Path dest_path = Paths.get(savePath.getParent().toString() + mInPath.substring(mInPath.lastIndexOf("\\"), mInPath.length()));
	//			System.out.println("mInpath = " + mInPath + ", p1 = " + src_path + ", p2 = " + dest_path); //TODO delete
				Files.copy(src_path, dest_path, StandardCopyOption.REPLACE_EXISTING);
			}
			
		} catch (IOException e) {
			System.err.println(e);
		}
		
		/*****************************************/
		/***** compile jar/class with aspect *****/
		/*****************************************/
		
		//TODO compile file
		String sourceroots = Paths.get(aspectPath).getParent().toAbsolutePath().toString();
//		String inpath;
		String inpath = sourceroots;
		
		
		if(isJar){
			
			inpath = Paths.get(mInPath).toString();
			String outjar = saveJar();
			System.out.println(outjar);
			if(outjar == null) 
				return;
			AjcRunner.compileJar(inpath, sourceroots, outjar);
			
			
		} else {
			
//			inpath = Paths.get(mInPath).getParent().toAbsolutePath().toString();
			AjcRunner.compileClass(inpath, sourceroots);
			
		}
		

	}
	
	
	
	/***************************/
	/***** Save Jar Dialog *****/
	/***************************/
	private String saveJar() {
		
		File myFile;
		JFileChooser chooser = new JFileChooser();

		OpenFileFilter jarFilter = new OpenFileFilter("jar","*.jar File");
		chooser.addChoosableFileFilter(jarFilter);
		chooser.setFileFilter(jarFilter);
		chooser.setAcceptAllFileFilterUsed(false);


		int returnVal = chooser.showSaveDialog(mFrame);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
						
			myFile = chooser.getSelectedFile();
			return myFile.getAbsolutePath() + ".jar";
			
		}
		
		return null;
	}	
}
