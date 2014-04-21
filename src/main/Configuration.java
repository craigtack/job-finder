package main;

import org.ini4j.Ini;
import utility.INI;

public class Configuration {
    private static Ini config;

    public Configuration(String fileName) {
        config = INI.readIni(fileName);
    }

    public static Ini getConfig() {
        return config;
    }
}
