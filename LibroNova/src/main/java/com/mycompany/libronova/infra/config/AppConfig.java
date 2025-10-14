/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.infra.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Coder
 */
public class AppConfig {
    
    private final Properties props = new Properties();

    public AppConfig() {
        
        try (InputStream in = getClass().getResourceAsStream("/application.properties")) {
            
            if (in == null) {
                throw new IllegalStateException("application.properties not found");
            }
            
            props.load(in);
            
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration", e);
        }
    }
    
    public String get(String key){return props.getProperty(key);}
    
}
