package com.selj.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private final Properties properties = new Properties();

    public ConfigReader() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("Config file not exist");
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Error during config file loading", e);
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
