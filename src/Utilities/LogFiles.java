/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Sets up Error Log and User Log
 * @author Austin Wong
 */
public class LogFiles {
    
    private static final Logger log = Logger.getLogger(LogFiles.class.getName());

    // Sets up error log
    public static void setupLogger() {

        try {
            ConsoleHandler ch = new ConsoleHandler();
            SimpleFormatter sf = new SimpleFormatter();
            ch.setFormatter(sf);
            log.addHandler(ch);
        } catch (SecurityException e) {
            Logger.getLogger(LogFiles.class.getName()).log(Level.SEVERE, null, e);
        }

        log.setLevel(Level.CONFIG);

    }
    
    // Logs user activity to stdout for container log aggregation
    public static void logUserActivity() {

        String msg = "USER_ACTIVITY: USER "+User.getCurrentUser().getUserName()+" has logged in at " + ZonedDateTime.now();
        System.out.println(msg);
        log.log(Level.INFO, msg);
    }
 
// Log Hierarchy
// SEVERE (highest)
// WARNING
// INFO
// CONFIG
// FINE
// FINER
// FINEST
}
