/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author sp1d
 */
public class Settings {

    private static Properties properties;
    private static final String FILENAME = "/WEB-INF/global.properties";
    private static final ServletContext servletContext = null;

    public static void init(ServletContext context) throws IOException {
        if (properties == null) {
            properties = new Properties();
//            InputStream settingsStream = Settings.class.getClassLoader().getResourceAsStream(FILENAME);
            InputStream settingsStream = context.getResourceAsStream(FILENAME);
            if (settingsStream == null) {
                throw new IOException("Cant find properties in file " + FILENAME);
            }
            properties.load(settingsStream);         
        }        
    }

    public static Properties getSettings() {
        return properties;
    }

}
