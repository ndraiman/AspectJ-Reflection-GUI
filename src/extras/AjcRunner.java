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
    	
    	try {
    		
			Main.main(ajcArgs);
			
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println(e);
		}
    }
}
