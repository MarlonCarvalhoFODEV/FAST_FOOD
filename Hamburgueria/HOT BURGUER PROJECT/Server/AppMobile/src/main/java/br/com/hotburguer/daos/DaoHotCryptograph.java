package br.com.hotburguer.daos;

import apis.encrypt.RSA;
import apis.pagarme.system.CryptographSys;
import br.com.comprazencasa.database.ConnectionDB_APP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoHotCryptograph {

    public RSA.Keys getCryptographDefault() {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM hot_cryptograph WHERE id=1 LIMIT 1;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                RSA.Keys c ;
                if(rs.next()){
                    c = new RSA.Keys();
                    c.setPrivateKey(RSA.getDecodedPrivateKey(rs.getString("private_key")));
                    c.setPublicKey(RSA.getDecodedPublicKey(rs.getString("public_key")));

                    con.close();
                    pst.close();

                    return c;
                }

                con.close();
                pst.close();

                return null ;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    if(con != null)con.close();
                    if(pst != null)pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public RSA.Keys getCryptographPayment() {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM hot_cryptograph WHERE id=3 LIMIT 1;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                RSA.Keys c ;
                if(rs.next()){
                    c = new RSA.Keys();
                    c.setPrivateKey(RSA.getDecodedPrivateKey(rs.getString("private_key")));
                    c.setPublicKey(RSA.getDecodedPublicKey(rs.getString("public_key")));

                    con.close();
                    pst.close();

                    return c;
                }

                con.close();
                pst.close();

                return null ;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    if(con != null)con.close();
                    if(pst != null)pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
