package extras;

import java.io.IOException;

import org.aspectj.tools.ajc.Main;

public class AjcRunner {
//    public static void main(String[] args) throws Exception {
//        String[] ajcArgs = {
//            "-sourceroots", "c:\\my\\aspectj_project\\src",
//            "-outjar", "my_aspects.jar"
//        };
//        Main.main(ajcArgs);
//    }
    
    
	//TODO make AjcRunner not close program and print output to console
	
    public static void compileClass(String inpath, String sourceroots) {
    	System.out.println("compileClass - inpath = " + inpath + ", sourceroots = " + sourceroots);
    	
    	String[] ajcArgs = {"-1.8",
    			"-sourceroots", sourceroots,
    			"-inpath", inpath}; 
    	
    	try {
    		
			Main.main(ajcArgs);
			
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println(e);
		}
    }
    
    public static void compileJar(String inpath, String sourceroots, String outjar) {
    	System.out.println("compileJar - inpath = " + inpath + ", sourceroots = " + sourceroots);
    	
    	String[] ajcArgs = {"-1.8",
    			"-sourceroots", sourceroots,
    			"-inpath", inpath,
    			"-outjar", outjar};
    	
//    	String[] ajcArgs = {"-1.8",
//    			"-sourceroots", "C:\\Users\\ND88\\Desktop\\jar test\\test",
//    			"-inpath", "C:\\Users\\ND88\\Desktop\\jar test\\test\\TestJar.jar",
//    			"-outjar", "C:\\Users\\ND88\\Desktop\\kaki.jar"}; 
//    	
//    	
//    	new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				try {
//					Main.main(ajcArgs);
//				} catch (IOException e) {
//					System.out.println(e);
//				}
//				
//			}
//		}).start();
    	
    	try {
    		
			Main.main(ajcArgs);
			
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println(e);
		}
    }
}
