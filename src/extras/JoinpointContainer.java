package extras;

public class JoinpointContainer {
	
	private String mType;
	private String mParam;
	
	public JoinpointContainer(String type, String param) {
		
		mType = type;
		mParam = param;
		
	}
	
	
	@Override
	public String toString() {
		return mType + "(" + mParam + ")";
	}

	public String getType() {
		return mType;
	}

	public String getParam() {
		return mParam;
	}
	
	
	
	
}
