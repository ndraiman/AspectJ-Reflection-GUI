package extras;

public class AdviceContainer {
	
	private String mAdvice;
	
	public AdviceContainer(String advice) {
		mAdvice = advice;
	}
	
	
	public String getAdvice() {
		return mAdvice;
	}
	

	@Override
	public String toString() {
		return getAdvice();
	}

}
