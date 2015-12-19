package TreeNodes;
import javax.swing.tree.DefaultMutableTreeNode;




public class MethodNode extends DefaultMutableTreeNode {
	
	private String mMethodName;
	private String mMethodModifier;
	private Class<?>[] mMethodParams;
	
	public MethodNode(String modifier, String name, Class<?>[] params) {
		
		mMethodName = name;
		mMethodParams = params;
		mMethodModifier = modifier;
		
	}

	@Override
	public String toString() {
		String s = mMethodModifier + " " + mMethodName + "(";
		
		for(int i = 0; i < mMethodParams.length; i++) {
			s += mMethodParams[i].getSimpleName();
			
			if(i != 0 && i != mMethodParams.length-1)
				s += ", ";
		}
		s += ")";
		
		return s;
	}

	public String getmMethodName() {
		return mMethodName;
	}

	public String getmMethodModifier() {
		return mMethodModifier;
	}

	public Class<?>[] getmMethodParams() {
		return mMethodParams;
	}
	
	
	
	
	

}
