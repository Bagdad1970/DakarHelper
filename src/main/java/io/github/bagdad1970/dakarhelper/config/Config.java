package io.github.bagdad1970.dakarhelper.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Config {

    private static Properties properties = new Properties();

    static {
        try (InputStream fileConfig = Config.class.getClassLoader().getResourceAsStream("email.key")) {
            properties.load(fileConfig);
        }
        catch (IOException exp) {
            System.out.println(exp.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
