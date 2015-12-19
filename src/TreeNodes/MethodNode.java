package TreeNodes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.swing.tree.DefaultMutableTreeNode;




public class MethodNode extends DefaultMutableTreeNode {
	
	private Method mMethod;
	private String mMethodName;
	private String mMethodModifier;
	private Class<?>[] mMethodParams;
	
	public MethodNode(Method m) {
		
		mMethod = m;
		mMethodName = m.getName();
		mMethodParams = m.getParameterTypes();
		mMethodModifier = Modifier.toString(m.getModifiers());
		
	}

	@Override
	public String toString() {
		String s = mMethodModifier + " " + mMethodName + "(";
		
		for(int i = 0; i < mMethodParams.length; i++) {
			s += mMethodParams[i].getSimpleName();
			
			if(mMethodParams.length > 1 && i != mMethodParams.length-1)
				s += ", ";
		}
		s += ")";
		
		return s;
	}
	


	/********* Getters **********/
	
	public Method getMethodObject() {
		return mMethod;
	}

	public String getMethodName() {
		return mMethodName;
	}

	public String getMethodModifier() {
		return mMethodModifier;
	}

	public Class<?>[] getMethodParams() {
		return mMethodParams;
	}
	
	
	
	
	

}
