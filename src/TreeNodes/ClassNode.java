package TreeNodes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClassNode extends DefaultMutableTreeNode {
	
	private Class<?> mClass;
	private String mClassName;
	private String mClassModifier;
	private List<ConstructorNode> mConstructors;
	private List<VariableNode> mVariables;
	private List<MethodNode> mMethods;
	
	public ClassNode(Class<?> c) {
		
		mClass = c;
		mClassName = c.getName();
		mClassModifier = Modifier.toString(c.getModifiers());
		mConstructors = new ArrayList<ConstructorNode>();
		mVariables = new ArrayList<VariableNode>();
		mMethods = new ArrayList<MethodNode>();
		
	}
	
	@Override
	public String toString() {
		return mClassName + " (" + mClassModifier + ")"; 
	}
	
	public void addConstructor(ConstructorNode ctor) {
		mConstructors.add(ctor);
	}
	
	public void addVariable(VariableNode var) {
		mVariables.add(var);
	}
	
	public void addMethod(MethodNode method) {
		mMethods.add(method);
	}
	
	/********* Getters **********/
	
	public Class<?> getClassObject() {
		return mClass;
	}

	public String getClassName() {
		return mClassName;
	}

	public String getClassModifier() {
		return mClassModifier;
	}

	public List<ConstructorNode> getConstructors() {
		return mConstructors;
	}

	public List<VariableNode> getVariables() {
		return mVariables;
	}

	public List<MethodNode> getMethods() {
		return mMethods;
	}

	
	
	
	
	
	
	

}
