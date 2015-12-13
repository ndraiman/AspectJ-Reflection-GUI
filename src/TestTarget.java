public class TestTarget {

    public static void main(String[] args) {
        System.out.println("----------------------->--------- Start test -----------<---------------------");
        new TestTarget().test();
        System.out.println("----------------------->--------- End test -----------<---------------------");
        
//        new Car().drive("Haifa");
    }

    public void test() {
        System.out.println("TestTarget.test()");
    }
}
