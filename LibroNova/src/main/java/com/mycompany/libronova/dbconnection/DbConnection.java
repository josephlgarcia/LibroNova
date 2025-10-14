/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dbconnection;

import com.mycompany.libronova.infra.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Coder
 */
public class DbConnection {
    
    private final AppConfig cfg;

    public DbConnection(AppConfig cfg) {
        this.cfg = cfg;
    }
    
    public Connection open() throws SQLException {
        
        String URL = cfg.get("db.url");
        String USER = cfg.get("db.user");
        String PASSWORD = cfg.get("db.password");
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }  
    
}
