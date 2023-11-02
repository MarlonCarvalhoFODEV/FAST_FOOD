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
public class ConnectionDB_Country {



    public synchronized static Connection connect(){
        try {

            String user = "compraze_app";
            String pass = "M@rlon7832@4512";

            StringBuilder url = new StringBuilder();
            url.append("jdbc:mysql://192.175.124.120:3306/");
            url.append("compraze_application?");
            //url.append("?useSSL=false&useTimezone=true&serverTimezone=UTC&");
            url.append("useUnicode=true&");
            url.append("characterEncoding=UTF-8&");
            url.append("characterSetResults=utf8&");
            url.append("connectionCollation=utf8_general_ci");

            String driver = "com.mysql.cj.jdbc.Driver";

            Class.forName(driver);
            return DriverManager.getConnection(url.toString(), user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectionDB_Country.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null ;
    }

    private static void sleeping(int timeMills){
        try {
            Thread.sleep(timeMills);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionDB_Country.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
