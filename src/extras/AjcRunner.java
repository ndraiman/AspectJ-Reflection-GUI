package extras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.aspectj.tools.ajc.Main;

public class AjcRunner {
    
    
	//TODO make AjcRunner not close program and print output to console
	
    public static void compileClass(String inpath, String sourceroots) {
    	System.out.println("compileClass - inpath = " + inpath + ", sourceroots = " + sourceroots);
    	
//    	String[] ajcArgs = {"-1.8",
//    			"-sourceroots", sourceroots,
//    			"-inpath", inpath}; 
    	
    	String s = null;
    	
    	try {
    		
			Process p = Runtime.getRuntime().exec("cmd /c ajc -1.8 -sourceroots \"" + sourceroots + "\" -inpath \"" + inpath + "\"");

			
			BufferedReader stdInput = new BufferedReader(new
	                 InputStreamReader(p.getInputStream()));
	 
	            BufferedReader stdError = new BufferedReader(new
	                 InputStreamReader(p.getErrorStream()));
	 
	            // read the output from the command
	            System.out.println("Here is the standard output of the command:\n");
	            while ((s = stdInput.readLine()) != null) {
	                System.out.println(s);
	            }
	             
	            // read any errors from the attempted command
	            System.out.println("Here is the standard error of the command (if any):\n");
	            while ((s = stdError.readLine()) != null) {
	                System.out.println(s);
	            }
	            
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	
//    	try {
//
//			Main.main(ajcArgs);
//			
//		} catch (IOException e) {
////			e.printStackTrace();
//			System.out.println(e);
//		}
    }
    
    public static void compileJar(String inpath, String sourceroots, String outjar) {
    	System.out.println("compileJar - inpath = " + inpath + ", sourceroots = " + sourceroots);
    	
//    	String[] ajcArgs = {"-1.8",
//    			"-sourceroots", sourceroots,
//    			"-inpath", inpath,
//    			"-outjar", outjar};
    	
    	String s = null;
    	
    	try {
		
		Process p = Runtime.getRuntime().exec("cmd /c ajc -1.8 -sourceroots \"" + sourceroots 
				+ "\" -inpath \"" + inpath 
				+ "\" -outjar \"" + outjar + "\"");

		
		BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
 
            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));
 
            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
             
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    	
//    	try {
//    		
//			Main.main(ajcArgs);
//			
//		} catch (IOException e) {
////			e.printStackTrace();
//			System.out.println(e);
//		}
    }
}
