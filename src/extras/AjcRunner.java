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
    
    
    public static void compileWithAspects(String inpath, String sourceroots) {
    	System.out.println("AjcRunner - inpath = " + inpath + ", sourceroots = " + sourceroots);
    	
    	String[] ajcArgs = {"-1.8",
    			"-sourceroots", sourceroots,
    			"-inpath", inpath}; 
    	
    	try {
    		
			Main.main(ajcArgs);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
