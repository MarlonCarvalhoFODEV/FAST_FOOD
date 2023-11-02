package br.com.comprazencasa.database;

import apis.encrypt.RSA;
import apis.pagarme.system.CryptographSys;
import apis.pagarme.system.PaymentSys;
import br.com.hotburguer.daos.DBConnections;
import br.com.hotburguer.itens.LAddress;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoSystemConfiguration {

    public boolean insertCriptographRSA(PrivateKey priKey, PublicKey pubKey) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "INSERT INTO app_system_config (private_key, public_key) VALUES (?,?);";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, RSA.getEncodedToBase64(priKey));
                pst.setString(2, RSA.getEncodedToBase64(pubKey));

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffecteds > 0);
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
                    if (con != null) con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public PaymentSys getPaymentConfig() {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM app_config_payment WHERE id=1 LIMIT 1;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                PaymentSys pSys;
                if (rs.next()) {
                    pSys = new PaymentSys();
                    pSys.setCommission(rs.getInt("commission"));

                    pSys.setSystemId(rs.getString("id_recipient"));
                    pSys.getRecipient().setTransfer(rs.getBoolean("transfer_enabled"));
                    pSys.getRecipient().setDayTransfer(rs.getInt("transfer_day"));
                    pSys.getRecipient().setTransferIntervalType(rs.getString("transfer_interval_type"));
                    pSys.getRecipient().setAnticipateVolPercentage(rs.getInt("antipation_vol_percentage"));
                    pSys.getRecipient().setAutomaticTransfer(rs.getBoolean("auto_transfer"));
                    pSys.getRecipient().setAutomaticAnticipation(rs.getBoolean("auto_anticipation"));
                    pSys.getRecipient().setAutomaticAnticipationDays(rs.getString("auto_anticipation_days"));
                    pSys.getRecipient().setAutomaticAnticipationType(rs.getString("auto_anticipation_type"));

                    con.close();
                    pst.close();

                    return pSys;
                }

                con.close();
                pst.close();

                return null;
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
                    if (con != null) con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public PaymentSys getSystemConfig() {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM app_config_payment WHERE id=1 LIMIT 1;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                PaymentSys pSys;
                if (rs.next()) {
                    pSys = new PaymentSys();
                    pSys.setCommission(rs.getInt("commission"));

                    con.close();
                    pst.close();

                    return pSys;
                }

                con.close();
                pst.close();

                return null;
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
                    if (con != null) con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public CryptographSys getCryptograph() {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM app_config_cryptography WHERE id=1 LIMIT 1 ;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                CryptographSys c = null;
                if (rs.next()) {
                    c = new CryptographSys();

                    c.getRsa().setPrivateKey(RSA.getDecodedPrivateKey(rs.getString("private_key")));
                    c.getRsa().setPublicKey(RSA.getDecodedPublicKey(rs.getString("public_key")));

                }

                con.close();
                pst.close();


                return c;
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
                    if (con != null) con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public LAddress getCurrentLocationBusiness() {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_configurations WHERE id=1;  ");

                pst = con.prepareStatement(sqlConstruct.toString());
                ResultSet rs = pst.executeQuery();

                LAddress ad = null;
                if (rs.next()) {
                    ad = new LAddress();
                    ad.setId(rs.getInt("id"));
                    ad.setContry(rs.getString("country"));
                    ad.setState(rs.getString("state"));
                    ad.setCity(rs.getString("city"));
                    ad.setNeighborhood(rs.getString("neighborhood"));
                    ad.setStreet(rs.getString("street"));
                    ad.setNumber(rs.getInt("number"));
                    ad.setCEP(rs.getString("cep"));
                    ad.setLatitude(rs.getDouble("latitude"));
                    ad.setLongitude(rs.getDouble("longitude"));
                    ad.setComplement(rs.getString("complement"));
                }
                con.close();
                pst.close();

                return ad;
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);

                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return null;
    }

    public float getLastVersionHotBurguer() {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM hot_update_controller WHERE id=3 ORDER BY id DESC LIMIT 1;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    float version = rs.getFloat("version");

                    con.close();
                    pst.close();

                    return version;
                }

                con.close();
                pst.close();

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
                    if (con != null) con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public float getLastVersionDeliveryMan() {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        String sql = "SELECT * FROM hot_update_controller WHERE id=4 ORDER BY id DESC LIMIT 1;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    float version = rs.getFloat("version");

                    con.close();
                    pst.close();

                    return version;
                }

                con.close();
                pst.close();

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
                    if (con != null) con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
