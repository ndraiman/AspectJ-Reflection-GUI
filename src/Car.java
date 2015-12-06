
public class Car {
	
	String location;
	boolean isActive;
	
	
	public Car() {
		location = "Jerusalem";
		isActive = true;
	}


	public String getLocation() {
		return location;
	}


	public void drive(String location) {
		this.location = location;
	}


	public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	
	

}
