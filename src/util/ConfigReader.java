package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private ConfigReader() {
    }

    public static String getApiKey() {
        Properties properties = new Properties();

        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find the config.properties file at the project root.");
            }

            properties.load(inputStream);
            return properties.getProperty("api.key");
        } catch (IOException e) {
            throw new RuntimeException("Error reading the configuration file: " + e.getMessage(), e);
        }
    }
}