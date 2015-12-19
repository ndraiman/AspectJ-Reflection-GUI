package TreeNodes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClassNode extends DefaultMutableTreeNode {
	
	private String mClassName;
	private String mClassModifier;
	private Constructor<?>[] mConstructors;
	private Field[] mVariables;
	private Method[] mMethods;
	//TODO save nodes instead?

}
