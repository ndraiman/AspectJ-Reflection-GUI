package extras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AjcRunner {
    
    
    public static void compileClass(String inpath, String sourceroots) {
//    	System.out.println("compileClass - inpath = " + inpath + ", sourceroots = " + sourceroots); //DEBUG
    	
    	
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
			e1.printStackTrace();
		}
    	
    	
    }
    
    public static void compileJar(String inpath, String sourceroots, String outjar) {
//    	System.out.println("compileJar - inpath = " + inpath + ", sourceroots = " + sourceroots); //DEBUG
    	
    	
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
            System.out.println("AJC standard output:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
             
            // read any errors from the attempted command
            System.out.println("AJC standard error output:\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            
	} catch (IOException e1) {
		e1.printStackTrace();
	}
    	
    }
}
