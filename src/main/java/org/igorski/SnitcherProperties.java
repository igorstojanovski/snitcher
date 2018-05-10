package org.igorski;


import java.io.IOException;
import java.util.Properties;

/**
 * Utility class that reads properties from the snitcher.properties file in the resource folder.
 */
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
