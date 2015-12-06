
public aspect TestAspect {
	
	pointcut callDrive() : call(* Car.drive(String)) || execution(* Car.drive(String));
	
	before() : callDrive() {
		System.out.println("Starting to Drive");
	}
	
	after() : callDrive() {
		System.out.println("Arrived at Destination");
	}

}
