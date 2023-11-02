package br.com.megasena.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnections {

    public synchronized static Connection connect(){
        try {
            String user = "compraze_sena_u";
            String pass = "M@rlon7832@4512";

            StringBuilder url = new StringBuilder();
            url.append("jdbc:mysql://192.175.124.120:3306/");
            url.append("compraze_sena_facil?");
            url.append("useSSL=false&useTimezone=true&serverTimezone=UTC&");
            url.append("useUnicode=true&");
            url.append("characterEncoding=UTF-8&");
            url.append("characterSetResults=utf8&");
            url.append("connectionCollation=utf8_general_ci&");
            url.append("allowMultiQueries=true");

            String driver = "com.mysql.cj.jdbc.Driver";

            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url.toString(), user, pass);
            return connection ;
        } catch (ClassNotFoundException | SQLException ex) {
            return null ;
        }
    }
}
