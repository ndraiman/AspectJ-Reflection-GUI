package extras;

import java.util.ArrayList;
import java.util.List;


public class PointcutContainer {
	
	private String mName;
	private List<JoinpointContainer> mJoinpoints;
	private String mArgs;
	private String mRelation;
	
	
	public PointcutContainer(String name, String args, String relation) {
//		System.out.println("creating pointcut - " + name); //DEBUG
		mName = name;
		mArgs = args;
		mRelation = relation.equals("OR") ? "||" : "&&";
		mJoinpoints = new ArrayList<>();
	}
	
	
	


	@Override
	public String toString() {
		String s = "pointcut " + mName + "(" + mArgs + ") : " + mJoinpoints.get(0);
		
		for(int i = 1; i < mJoinpoints.size(); i++) {
			s += " " + mRelation + " " + mJoinpoints.get(i);
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


	public List<JoinpointContainer> getJoinpoints() {
		return mJoinpoints;
	}

	
	
	
	

}
