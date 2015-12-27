package treeNodes;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClassNode extends DefaultMutableTreeNode {
	
	private Class<?> mClass;
	private String mClassName;
	private String mClassModifier;
	private String mPackageName;
	private List<ConstructorNode> mConstructors;
	private List<FieldNode> mFields;
	private List<MethodNode> mMethods;

	
	public ClassNode(Class<?> c) {
		
		mClass = c;
		mClassName = c.getName();
		mClassModifier = Modifier.toString(c.getModifiers());
		
		Package p = c.getPackage();
		mPackageName = p != null? p.toString() : null;
		
		mConstructors = new ArrayList<ConstructorNode>();
		mFields = new ArrayList<FieldNode>();
		mMethods = new ArrayList<MethodNode>();
		
	}
	
	@Override
	public String toString() {
		return mClassName + " (" + mClassModifier + ")"; 
	}
	
	public void addConstructor(ConstructorNode ctor) {
		mConstructors.add(ctor);
	}
	
	public void addField(FieldNode fld) {
		mFields.add(fld);
	}
	
	public void addMethod(MethodNode method) {
		mMethods.add(method);
	}
	
	/********* Getters **********/
	
	public Class<?> getClassObject() {
		return mClass;
	}

	public String getName() {
		return mClassName;
	}

	public String getModifier() {
		return mClassModifier;
	}
	
	public String getPackageName() {
		return mPackageName;
	}

	public List<ConstructorNode> getConstructors() {
		return mConstructors;
	}

	public List<FieldNode> getFields() {
		return mFields;
	}

	public List<MethodNode> getMethods() {
		return mMethods;
	}

	
	
	
	
	
	
	

}
