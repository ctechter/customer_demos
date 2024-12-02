package io.developer.confluent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    public static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream("client.properties")) {
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Could not load properties file", ex);
        }
        return props;
    }
}