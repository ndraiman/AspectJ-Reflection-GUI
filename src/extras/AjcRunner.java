package extras;

import org.aspectj.tools.ajc.Main;

public class AjcRunner {
    public static void main(String[] args) throws Exception {
        String[] ajcArgs = {
            "-sourceroots", "c:\\my\\aspectj_project\\src",
            "-outjar", "my_aspects.jar"
        };
        Main.main(ajcArgs);
    }
}
