package treeNodes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.tree.DefaultMutableTreeNode;

public class FieldNode extends DefaultMutableTreeNode {
	
	private Field mField;
	private String mFldName;
	private String mFldType;
	private String mFldModifier;
	
	
	public FieldNode(Field fld) {
		
		mField = fld;
		mFldName = fld.getName();
		mFldType = fld.getType().getSimpleName();
		mFldModifier = Modifier.toString(fld.getModifiers());
		
	}


	@Override
	public String toString() {
		return mFldModifier + " " + mFldType + " " + mFldName;
	}
	
	
	/********* Getters **********/


	public Field getFieldObject() {
		return mField;
	}


	public String getName() {
		return mFldName;
	}


	public String getType() {
		return mFldType;
	}


	public String getModifier() {
		return mFldModifier;
	}
	
	
	
	
	

}
