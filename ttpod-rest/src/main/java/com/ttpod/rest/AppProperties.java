package com.ttpod.rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public abstract class AppProperties {

    static Properties p ;
    static {
        try {
            p = PropertiesLoaderUtils.loadProperties(
                    new ClassPathResource("application.properties")
            );
        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
            System.err.println("ClassPathResource  application.properties NOT found ! ");

            p = new Properties();
        }
    }
	public static String get(String key) {
		return p.getProperty(key);
	}
    public static String get(String key,String defaultValue) {
        return p.getProperty(key,defaultValue);
    }
}
