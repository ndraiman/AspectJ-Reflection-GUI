

public aspect TestAspect {
	
	pointcut test() : execution(* TestTarget.test*(..));

    before() : test() {
        System.out.printf("TestAspect.advice() called on '%s'%n", thisJoinPoint);
    }
}