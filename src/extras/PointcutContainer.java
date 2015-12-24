package extras;

import java.util.ArrayList;
import java.util.List;


public class PointcutContainer {
	
	private String mName;
	private List<JoinpointContainer> mJoinpoints;
	
	
	public PointcutContainer(String name) {
		System.out.println("creating pointcut - " + name);
		mName = name;
		mJoinpoints = new ArrayList<>();
	}
	
	
	


	@Override
	public String toString() {
		String s = "pointcut " + mName + "() : " + mJoinpoints.get(0);
		
		for(int i = 1; i < mJoinpoints.size(); i++) {
			s += " || " + mJoinpoints.get(i);
		}
		
		s += ";";
		
		return s;
	}

	public void addJoinpoint(JoinpointContainer j) {
		mJoinpoints.add(j);
	}
	
	
	public String getName() {
		return mName;
	}


	public List<JoinpointContainer> getmJoinpoints() {
		return mJoinpoints;
	}

	
	
	
	

}
