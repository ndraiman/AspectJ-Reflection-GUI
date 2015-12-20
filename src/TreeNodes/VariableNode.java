package TreeNodes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.tree.DefaultMutableTreeNode;

public class VariableNode extends DefaultMutableTreeNode {
	
	private Field mVariable;
	private String mVarName;
	private String mVarType;
	private String mVarModifier;
	
	
	public VariableNode(Field var) {
		
		mVariable = var;
		mVarName = var.getName();
		mVarType = var.getType().getSimpleName();
		mVarModifier = Modifier.toString(var.getModifiers());
		
	}


	@Override
	public String toString() {
		return mVarModifier + " " + mVarType + " " + mVarName;
	}
	
	
	/********* Getters **********/


	public Field getVariable() {
		return mVariable;
	}


	public String getVarName() {
		return mVarName;
	}


	public String getVarType() {
		return mVarType;
	}


	public String getVarModifier() {
		return mVarModifier;
	}
	
	
	
	
	

}
