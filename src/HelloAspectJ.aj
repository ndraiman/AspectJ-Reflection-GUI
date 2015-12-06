
public aspect HelloAspectJ {
 
    // Define a Pointcut is
    // collection of JoinPoint call sayHello of class HelloAspectJDemo.
    pointcut callSayHello(): call(* HelloAspectJDemo.sayHello());
 
    before() : callSayHello() {
        System.out.println("Before call sayHello");
    }
 
    after() : callSayHello()  {
        System.out.println("After call sayHello");
    }
 
     
}