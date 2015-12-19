import java.io.Serializable;


public class Car implements Serializable{
	
	private String location;
	public boolean isActive;
	protected double milesDriven;
	
	
	public Car() {
		location = "Jerusalem";
		isActive = true;
		milesDriven = 0;
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
