public aspect TestAspect {
	
	pointcut test() : execution(* TestTarget.test*(...));

    before() : test()
    public void advice(JoinPoint joinPoint) {
        System.out.printf("TestAspect.advice() called on '%s'%n", joinPoint);
    }
}