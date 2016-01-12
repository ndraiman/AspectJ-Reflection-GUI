package extras;

public class JoinpointContainer {
	
	private String mType;
	private String mParam;
	private boolean mNot;
	
	public JoinpointContainer(String type, String param, boolean not) {
		System.out.println("creating JoinpointContainer - " + type + "(" + param + ") - not = " + not);
		mType = type;
		mParam = param;
		mNot = not;
		
	}
	
	
	@Override
	public String toString() {
		String not = mNot ? "!" : "";
		
		return not + mType + "(" + mParam + ")";
	}

	public String getType() {
		return mType;
	}

	public String getParam() {
		return mParam;
	}
	
	
	
	
}
