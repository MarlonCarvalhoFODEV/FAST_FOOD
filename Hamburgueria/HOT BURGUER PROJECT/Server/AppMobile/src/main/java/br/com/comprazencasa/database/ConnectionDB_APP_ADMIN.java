/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class ConnectionDB_APP_ADMIN {

    public synchronized static Connection connect(){
        try {
            String user = "compraze_app_adm";
            String pass = "M@rlon%H777¨¨hgffg4";
            String database = "jdbc:mysql://192.175.124.120:3306/compraze_application?useSSL=false&useTimezone=true&serverTimezone=UTC&characterEncoding=utf-8";
            String driver = "com.mysql.cj.jdbc.Driver";

            Class.forName(driver);
            return DriverManager.getConnection(database, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectionDB_APP_ADMIN.class.getName()).log(Level.SEVERE, null, ex);
            return null ;
        }
    }

    private static void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionDB_APP_ADMIN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
