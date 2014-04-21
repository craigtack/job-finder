package utility;

import org.ini4j.Ini;

import java.io.File;

public class INI {
    private INI(){}

    public static Ini readIni(String fileName) {
        Ini ini = null;
        try {
            ini = new org.ini4j.Ini(new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ini;
    }
}
