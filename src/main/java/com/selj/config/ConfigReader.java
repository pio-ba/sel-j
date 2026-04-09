package com.selj.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                System.err.println("File is empty");
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Error during config.properties loading", e);
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
