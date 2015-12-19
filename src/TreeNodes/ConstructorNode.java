package TreeNodes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import javax.swing.tree.DefaultMutableTreeNode;

public class ConstructorNode extends DefaultMutableTreeNode {
	
	private Constructor<?> mConstructor;
	private String mCtorName;
	private String mCtorModifiers;
	private Class<?>[] mCtorParams;
	
	
	public ConstructorNode(Constructor<?> ctor) {
		
		mConstructor = ctor;
		mCtorName = ctor.getName();
		mCtorModifiers = Modifier.toString(ctor.getModifiers());
		mCtorParams = ctor.getParameterTypes();
		
	}


	@Override
	public String toString() {
		
		String s = mCtorModifiers + " " + mCtorName + "(";
		
		for(int i = 0; i < mCtorParams.length; i++) {
			s += mCtorParams[i].getSimpleName();
			
			if(mCtorParams.length > 1 && i != mCtorParams.length-1)
				s += ", ";
		}
		s += ")";

		return s;
	}
	
	/********* Getters **********/


	public Constructor<?> getConstructorObject() {
		return mConstructor;
	}


	public String getCtorName() {
		return mCtorName;
	}


	public String getCtorModifiers() {
		return mCtorModifiers;
	}


	public Class<?>[] getCtorParams() {
		return mCtorParams;
	}
	
	
	
	
	
	

}
