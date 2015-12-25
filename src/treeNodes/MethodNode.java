package treeNodes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.swing.tree.DefaultMutableTreeNode;




public class MethodNode extends DefaultMutableTreeNode {
	
	private Method mMethod;
	private String mMethodName;
	private String mMethodModifier;
	private String mMethodRetType;
	private Class<?>[] mMethodArgs;
	
	public MethodNode(Method m) {
		
		mMethod = m;
		mMethodName = m.getName();
		mMethodArgs = m.getParameterTypes();
		mMethodModifier = Modifier.toString(m.getModifiers());
		mMethodRetType = m.getReturnType().getSimpleName();
		
	}

	@Override
	public String toString() {
		String s = mMethodModifier + " " + mMethodRetType + " " + mMethodName + "(";
		
		for(int i = 0; i < mMethodArgs.length; i++) {
			s += mMethodArgs[i].getSimpleName();
			
			if(mMethodArgs.length > 1 && i != mMethodArgs.length-1)
				s += ", ";
		}
		s += ")";
		
		return s;
	}
	


	/********* Getters **********/
	
	public Method getMethodObject() {
		return mMethod;
	}

	public String getName() {
		return mMethodName;
	}

	public String getModifier() {
		return mMethodModifier;
	}
	
	public String getRetType() {
		return mMethodRetType;
	}

	public Class<?>[] getArgs() {
		return mMethodArgs;
	}
	
	
	
	
	
	

}
