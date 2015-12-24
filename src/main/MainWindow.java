package main;
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
import java.io.FileNotFoundException;
import java.io.IOException;
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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

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

	private JFrame frame;
	private JTree tree;
	
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
		return frame;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setTitle("AspectJ GUI");

		//Open Frame in middle of screen
		//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

		/**********************/
		/***** Tree Panel *****/
		/**********************/
		JPanel treePanel = new JPanel();
		treePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		treePanel.setLayout(new BorderLayout(0, 0));

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Loaded Classes");
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		//		tree.addTreeSelectionListener(this); //Remove listener implementation if this is removed
		
		//Mouse Listener
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

		/************************/
		/***** Button Panel *****/
		/************************/
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnLoadFile = new JButton(LBL_LOAD_FILE);
		btnLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
				((DefaultMutableTreeNode) model.getRoot()).removeAllChildren();
				loadFile();
				model.reload();
			}
		});
		
		
		JButton btnPointcut = new JButton(LBL_POINTCUT);
		btnPointcut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogInvoker(frame, MainWindow.this).pointcutDialog(0, null);				
			}
		});
		

		JButton btnAdvice = new JButton(LBL_ADVICE);
		btnAdvice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//				new DialogCodeEditor(TestWindow.this).setVisible(true);
				new DialogInvoker(frame, MainWindow.this).adviceDialog(mPointcuts);
				//TODO dialog should show list of created pointcuts
				//TODO save advice

			}
		});

		JButton btnCompile = new JButton(LBL_COMPILE);
		btnCompile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO compile with aspects
				compileWithAspects();
			}
		});
		
		
		
		buttonPanel.add(btnLoadFile);
		buttonPanel.add(btnPointcut);
		buttonPanel.add(btnAdvice);
		buttonPanel.add(btnCompile);

		frame.getContentPane().add(treePanel, BorderLayout.CENTER);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);



		//DEBUG LoadClassDetails
		loadClassDetails(root, Car.class, null);
		((DefaultTreeModel) tree.getModel()).reload();
	}


	/************************************************************************************************************/
	/************************************** Handle Tree Clicks **************************************************/
	/************************************************************************************************************/

	void treeDoubleClick(int row) {

		System.out.println("double clicked row " + row);
		//		tree.getSelectionPath().getPathComponent(row);
		DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if(!selected.isLeaf())
			return;

		System.out.println("Im a leaf!"); //DEBUG
		String name = "";
		int type = -1;
		
		if(selected instanceof FieldNode) {

			name = ((FieldNode) selected).getFldName();
			type = DialogInvoker.TYPE_FIELD;
			
		} else if (selected instanceof MethodNode) {
			
			name = ((MethodNode) selected).getMethodName();
			type = DialogInvoker.TYPE_METHOD;
			
		} else if (selected instanceof ConstructorNode) {
			
			name = ((ConstructorNode) selected).getCtorName();
			type = DialogInvoker.TYPE_CONSTRUCTOR;
			
		} else {
			return;
		}

		int option = JOptionPane.showConfirmDialog(frame, "Create pointcut for: " + name + "?",
				"Create Pointcut", JOptionPane.YES_NO_OPTION);

		if(option == 0) {
			new DialogInvoker(frame, MainWindow.this).pointcutDialog(type, selected);
			//TODO save pointcut
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
		DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(classNode.getPackageName());
		classNode.add(packageNode);
		
		//Load Superclass
		DefaultMutableTreeNode superclassNode = new DefaultMutableTreeNode("Superclass: " + c.getSuperclass().getName());
		classNode.add(superclassNode);

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
		//		chooser.setFileFilter(jarFilter);
		chooser.setFileFilter(classFilter); //default filter
		chooser.setAcceptAllFileFilterUsed(false);


		int returnVal = chooser.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			myFile = chooser.getSelectedFile();
			mInPath = myFile.getAbsolutePath();

			readFile(myFile);

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

			DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

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

		try {

			Class<?> c = cl.loadClass(className);
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();			

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

		/***** DEBUG *****/
		//		public aspect TestAspect {
		//			
		//			pointcut test() : execution(* TestTarget.test*(..));
		//
		//		    before() : test() {
		//		        System.out.printf("TestAspect.advice() called on '%s'%n", thisJoinPoint);
		//		    }
		//		}

		System.out.println("CompileWithAspects\n\n");

		StringBuilder builder = new StringBuilder();
		builder.append("public aspect TestAspect {\n\n");
		builder.append("\tpointcut test() : execution(* TestTarget.test*(..));\n\n");
		builder.append("\tbefore() : test() {\n");
		builder.append("\t\tSystem.out.printf(\"TestAspect.advice() called on '%s'%n\", thisJoinPoint);\n");
		builder.append("\t}\n");
		builder.append("}");

		String s = builder.toString();

		System.out.println(s);
		
		//TODO create the aspect
		//TODO create a save dialog for the aspect
		//TODO make sure it is saved in a directory with write permissions (let user handle it)

		java.nio.file.Path savePath = Paths.get("/aspecttest.aj"); //TODO change to real path - form save dialog
		byte[] data = s.getBytes();
		

		try {
			
			Files.write(savePath, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
			
		} catch (IOException e) {
			System.err.println(e);
		}
		
		
		String sourceroots = savePath.getParent().toAbsolutePath().toString();
		String inpath = Paths.get(mInPath).getParent().toAbsolutePath().toString();

		
		AjcRunner.compileWithAspects(inpath, sourceroots);

	}

}
