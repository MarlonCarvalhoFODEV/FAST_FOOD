package br.com.megasena.dao;

import apis.encrypt.RSA;
import br.com.hotburguer.daos.DaoHotCryptograph;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoCryptography {

    public RSA.Keys getCryptographDefault() {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM system_cryptography WHERE id=1 LIMIT 1;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                RSA.Keys c ;
                if(rs.next()){
                    c = new RSA.Keys();
                    c.setPrivateKey(RSA.getDecodedPrivateKey(rs.getString("private")));
                    c.setPublicKey(RSA.getDecodedPublicKey(rs.getString("public")));

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

    public JSONObject decrypt(String request) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        PrivateKey pKey =getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        return c ;
    }

    public String encrypt(Object object) throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        PrivateKey pKey =getCryptographDefault().getPrivateKey();

        return new RSA(null, pKey).encryptToJson(object);
    }
}
