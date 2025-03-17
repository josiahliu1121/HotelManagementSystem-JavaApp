package com.config;

import java.io.*;
import java.util.Properties;

public class AppConfig {
    private static final String CONFIG_FILE = "config.properties";
    private final Properties properties;

    public AppConfig() {
        properties = new Properties();
        loadProperties();
    }

    // Load properties from file
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Error: config.properties file not found!");
                return;
            }
            properties.load(input);
        } catch (Exception e) {
            System.err.println("Error loading properties file: " + e.getMessage());
        }
    }

    // Get property value
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}

