package testPackage;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TestAspect2 {
	
	@Pointcut("execution(* Car.drive(String))")
	void calldrive() {}
	
	@Before("calldrive()")
	public void drive() {
		System.out.println("YOYO");
	}

}
