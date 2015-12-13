import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class TestAspect {
	
	@Pointcut("execution(* TestTarget.test(..))")
	void test() {} 
	
	@Before("test()")
	public void advice(JoinPoint joinPoint) {
	        System.out.printf("TestAspect.advice() called on '%s'%n", joinPoint);
	
	}
	

}
