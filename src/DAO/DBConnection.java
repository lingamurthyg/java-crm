/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Austin Wong
 */
public class DBConnection {
    
    //<editor-fold desc="variables">
    
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";

    // Database connection parameters from environment variables
    private static final String dbHost = System.getenv().getOrDefault("DB_HOST", "3.227.166.251");
    private static final String dbPort = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String dbName = System.getenv().getOrDefault("DB_NAME", "U07k1T");

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + "//" + dbHost + ":" + dbPort + "/" + dbName;

    // Driver and Connection Interface Reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    // Username and Password from environment variables
    private static final String username = System.getenv().getOrDefault("DB_USERNAME", "U07k1T");
    private static final String password = System.getenv().getOrDefault("DB_PASSWORD", "53689053296");
    
    //</editor-fold>
    
    public static Connection startConnection(){
        
        try{
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE,null,e);
        }
        
        return conn;
        
    }
    
    public static Connection getConnection(){
        return conn;
    }
    
    public static void closeConnection(){
        
        try{
            conn.close();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
}
