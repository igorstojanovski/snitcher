package org.igorski;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SnitcherProperties {

    private static Properties properties;

    public static String getValue(String key) {
        if(properties == null) {
            readProperties();
        }
        return properties.getProperty(key);
    }

    private static void readProperties() {
        properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("snitcher.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
