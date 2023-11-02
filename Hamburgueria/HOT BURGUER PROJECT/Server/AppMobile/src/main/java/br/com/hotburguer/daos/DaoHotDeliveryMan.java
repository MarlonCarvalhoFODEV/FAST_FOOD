package br.com.hotburguer.daos;

import apis.encrypt.RSA;
import apis.onesignal.OneSignalResponse;
import apis.pagarme.userbuyer.BAccountBank;
import br.com.comprazencasa.database.DaoAddress;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.hotburguer.itens.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoHotDeliveryMan {

    private static final int RS_SUCCEFFULY = -1;
    private static final int RS_DUPLICATE_CPF = 1;
    private static final int RS_DUPLICATE_EMAIL = 2;
    private static final int RS_DUPLICATE_DEVICE = 3;

    public HotDeliveryMan login(HotDeliveryMan deliveryMan) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users_delivery hot_user ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access ip ");
                sqlConstruct.append("ON ip.id_user_delivery=hot_user.id ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_account_banks bank ");
                sqlConstruct.append("ON bank.id_user_delivery=hot_user.id ");
                sqlConstruct.append("WHERE email=? AND id_device=?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, deliveryMan.getEmail());
                pst.setString(2, deliveryMan.getIdDevice());

                ResultSet rs = pst.executeQuery();

                HotDeliveryMan account = null;
                if (rs.next()) {
                    account = new HotDeliveryMan();
                    account.setId(rs.getInt("id"));
                    account.setIdRecipient(rs.getString("id_recipient"));
                    account.setHashKey(rs.getString("id_hash_key"));
                    account.setName(rs.getString("full_name"));
                    //account.getDocument().setCPF(rs.getString("cpf"));
                    account.getContact().getPhone().setFullNumber(rs.getString("cel"));
                    account.setStatus(rs.getString("status"));
                    account.setStatusOperation(rs.getBoolean("status_operation"));
                    //account.setDateOfBirth(rs.getString("date_of_birth"));
                    account.setIpAccess(rs.getString("public_ip"));

                    BAccountBank accountBank = new BAccountBank();
                    accountBank.setName(rs.getString("bank.name"));
                    accountBank.setAgency(rs.getString("bank.agency"));
                    accountBank.setAccount(rs.getString("bank.account"));
                    accountBank.setBankCode(rs.getString("bank.bank_code"));
                    accountBank.setType(BAccountBank.Type.getType(rs.getString("bank.type")));

                    account.setBankAccount(accountBank);

                }

                con.close();
                pst.close();

                if (account != null && deliveryMan.getIpAccess() != null &&
                        !deliveryMan.getIpAccess().equals(account.getIpAccess())) {
                    deliveryMan = new HotDeliveryMan();

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().
                }

                return account;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();

                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

        return deliveryMan;
    }

    public HotDeliveryMan update(HotDeliveryMan deliveryMan) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;


        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_users_delivery deliveryman ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access ip ");
                sqlConstruct.append("ON ip.id_user_delivery = deliveryman.id ");
                sqlConstruct.append("SET id_recipient=IF(id_recipient IS NULL,?,id_recipient), ");
                sqlConstruct.append("full_name=IF(full_name IS NULL,?,full_name), ");
                sqlConstruct.append("cpf=IF(cpf IS NULL,?,cpf), date_of_birth=IF(date_of_birth IS NULL,?,date_of_birth), cel=IF(cel IS NULL,?,cel) ");
                sqlConstruct.append("WHERE deliveryman.id=? AND deliveryman.id_device=?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, deliveryMan.getIdRecipient());
                pst.setString(2, deliveryMan.getName());
                pst.setString(3, deliveryMan.getDocument().get());
                pst.setString(4, deliveryMan.getDateOfBirth());
                pst.setString(5, deliveryMan.getContact().getPhone().getFullNumber());
                pst.setInt(6, deliveryMan.getId());
                pst.setString(7, deliveryMan.getIdDevice());
                //pst.setString(8, deliveryMan.getIpAccess());
                try {
                    pst.executeUpdate();

                    ResultSet rs = pst.getGeneratedKeys();

                    if (rs.next()) {
                        deliveryMan.setId(rs.getInt(1));
                        deliveryMan.setResult(RS_SUCCEFFULY);
                    }

                    con.close();
                    pst.close();

                    return deliveryMan;
                } catch (SQLIntegrityConstraintViolationException e) {
                    deliveryMan.setResult(RS_DUPLICATE_CPF);
                    return deliveryMan;
                }
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();

                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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

    public HotDeliveryMan newEmail(HotDeliveryMan deliveryMan) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO hot_users_delivery (full_name, email, id_device, id_hash_key,social_login) ");
                sqlConstruct.append("SELECT * FROM (SELECT ?, ?, ?, ?, ?) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT email FROM hot_users_delivery WHERE email = ? ");
                sqlConstruct.append(") LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, deliveryMan.getName());
                pst.setString(2, deliveryMan.getEmail());
                pst.setString(3, deliveryMan.getIdDevice());
                pst.setString(4, deliveryMan.getHashKey());
                pst.setString(5, deliveryMan.getSocialLogin());
                pst.setString(6, deliveryMan.getEmail());

                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();

                if (rs.next()) {
                    deliveryMan.setId(rs.getInt(1));

                    insertPublicIP(deliveryMan.getId(), deliveryMan.getIpAccess());
                }

                con.close();
                pst.close();

                return deliveryMan;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return deliveryMan;
    }

    public boolean createBankAccount(int idDeliveryman, BAccountBank bank) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO hot_users_delivery_account_banks (id_user_delivery, id_bank_account, name, agency, account, bank_code, ");
                sqlConstruct.append("type, is_default) SELECT * FROM (SELECT ?, ?, ?, ?, ?, ?, ?, ?) AS temp ");
                sqlConstruct.append("WHERE NOT EXISTS (SELECT * FROM hot_users_delivery_account_banks WHERE id_user_delivery=? ");
                sqlConstruct.append("AND name=? AND agency=? AND account=? AND bank_code=?) LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idDeliveryman);
                pst.setInt(2, bank.getId());
                pst.setString(3, bank.getName());
                pst.setString(4, bank.getAgency() + bank.getAgencyDv());
                pst.setString(5, bank.getAccount() + bank.getAccountDv());
                pst.setString(6, bank.getBankCode());
                pst.setString(7, bank.getTypeName());
                pst.setBoolean(8, bank.isDefaultAccount());
                pst.setInt(9, idDeliveryman);
                pst.setString(10, bank.getName());
                pst.setString(11, bank.getAgency() + bank.getAgencyDv());
                pst.setString(12, bank.getAccount());
                pst.setString(13, bank.getBankCode());
                pst.execute();

                con.close();
                pst.close();

                return true;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return false;
    }

    public boolean checkBankAccountExist(int idDeliveryman, BAccountBank bank) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users_delivery_account_banks bank ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery del ");
                sqlConstruct.append("ON del.id=bank.id_user_delivery ");
                sqlConstruct.append("WHERE del.id=?  ");
                sqlConstruct.append("AND name=? AND agency=? AND account=? AND bank_code=?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idDeliveryman);
                pst.setString(2, bank.getLegalName());
                pst.setString(3, bank.getAgency() + bank.getAgencyDv());
                pst.setString(4, bank.getAccount() + bank.getAccountDv());
                pst.setString(5, bank.getBankCode());

                ResultSet rs = pst.executeQuery();
                boolean exist = false;
                if (rs.next()) {
                    exist = true;
                }
                con.close();
                pst.close();

                return exist;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return false;
    }

    private boolean insertPublicIP(int idDeliveryman, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO hot_users_delivery_ip_access (id_user_delivery, public_ip) ");
                sqlConstruct.append("SELECT * FROM (SELECT ?, ?) AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT public_ip FROM hot_users_delivery_ip_access WHERE public_ip = ? ");
                sqlConstruct.append(") LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idDeliveryman);
                pst.setString(2, ip);
                pst.setString(3, ip);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffecteds > 0);
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return false;
    }

    public boolean updatePhotos(int idUser, byte[] profile, byte[] frontIdentity, byte[] verseIdentity, String idDevice, String ipAccess) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_users_delivery deliveryman ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access ip ");
                sqlConstruct.append("ON ip.id_user_delivery = deliveryman.id ");
                sqlConstruct.append("SET photo_profile=IF(photo_profile IS NULL, ?, photo_profile), ");
                sqlConstruct.append("photo_identity_front=IF(photo_identity_front IS NULL, ?, photo_identity_front), ");
                sqlConstruct.append("photo_identity_verse=IF(photo_identity_verse IS NULL, ?, photo_identity_verse) ");
                sqlConstruct.append("WHERE deliveryman.id=? AND deliveryman.id_device=?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setBytes(1, profile);
                pst.setBytes(2, frontIdentity);
                pst.setBytes(3, verseIdentity);
                pst.setInt(4, idUser);
                pst.setString(5, idDevice);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffecteds > 0);
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return false;
    }

    public RSA.Keys getCryptography() {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_cryptography WHERE id=0; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);

                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();

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

    public byte[] getPhotoProfile(int idUser) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT photo_profile FROM hot_users_delivery WHERE id=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] profile = rs.getBytes("photo_profile");

                    con.close();
                    pst.close();

                    return profile;
                }

                con.close();
                pst.close();

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

    public byte[] getPhotoFrontIdentity(int idUser) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT photo_identity_front FROM hot_users_delivery WHERE id=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] profile = rs.getBytes("photo_identity_front");

                    con.close();
                    pst.close();

                    return profile;
                }

                con.close();
                pst.close();

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

    public byte[] getPhotoVerseIdentity(int idUser) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT photo_identity_verse FROM hot_users_delivery WHERE id=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] profile = rs.getBytes("photo_identity_verse");

                    con.close();
                    pst.close();

                    return profile;
                }

                con.close();
                pst.close();

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

    public PhotoCompat getPhotos(int idUser) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users_delivery WHERE id=?;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] profile = rs.getBytes("photo_profile");
                    byte[] frontIdentity = rs.getBytes("photo_identity_front");
                    byte[] verseIdentity = rs.getBytes("photo_identity_verse");

                    PhotoCompat photos = new PhotoCompat();
                    photos.setProfile(profile);
                    photos.setFrontPhoto(frontIdentity);
                    photos.setVersePhoto(verseIdentity);

                    con.close();
                    pst.close();

                    return photos;
                }

                con.close();
                pst.close();

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

    public String getStatus(int idUser, String idDevice, String idRecipient, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users_delivery hot_user ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access ip ");
                sqlConstruct.append("ON ip.id_user_delivery=hot_user.id ");
                sqlConstruct.append("WHERE id=? AND id_device=? AND id_recipient=?;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);

                ResultSet rs = pst.executeQuery();

                HotDeliveryMan account = null;
                if (rs.next()) {
                    account = new HotDeliveryMan();
                    account.setId(rs.getInt("id"));
                    account.setIdRecipient(rs.getString("id_recipient"));
                    account.setHashKey(rs.getString("id_hash_key"));
                    account.setName(rs.getString("full_name"));
                    account.getDocument().setCPF(rs.getString("cpf"));
                    account.getContact().getPhone().setFullNumber(rs.getString("cel"));
                    account.setStatus(rs.getString("status"));
                    account.setDateOfBirth(rs.getString("date_of_birth"));
                    account.setIpAccess(rs.getString("public_ip"));
                }

                con.close();
                pst.close();

                if (account != null && ip != null &&
                        !ip.equals(account.getIpAccess()) && !idDevice.equals(account.getIdDevice())) {

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().

                    //return null ;
                }

                return account != null ? account.getStatus() : null;
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

    public HotPanelHome getPerformancesDayly(int idUser, String idDevice, String idRecipient, String publicIp) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT 'Hoje' day,count(*) delivereds, sum(fee_delivery) fee, date_format(his.date, '%y-%m-%d') date ");
                sqlConstruct.append(", deliveryman.id_device , ip.public_ip ip FROM hot_users_delivery_historic his ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery deliveryman ");
                sqlConstruct.append("ON deliveryman.id=his.id_user_delivery ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access ip ");
                sqlConstruct.append("ON ip.id_user_delivery=deliveryman.id ");
                sqlConstruct.append("WHERE deliveryman.id=? AND deliveryman.id_device=? ");
                sqlConstruct.append("AND id_recipient=? AND ");
                sqlConstruct.append("(date_format(his.date,'%y-%m-%d'))=(date_format(now(),'%y-%m-%d')); ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);

                ResultSet rs = pst.executeQuery();

                HotPanelHome panel = null;
                if (rs.next()) {
                    panel = new HotPanelHome();
                    panel.setDelivered(rs.getInt("delivereds"));
                    panel.setTitle(rs.getString("day"));
                    panel.setAmount(rs.getDouble("fee"));
                    panel.setPublicIP(rs.getString("ip"));
                    panel.setIdDevice(rs.getString("id_device"));
                }

                con.close();
                pst.close();

                if (publicIp != null && !publicIp.equals(panel.getPublicIP())
                        && idDevice != null && !idDevice.equals(panel.getIdDevice())) {

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().

                    //return null ;
                }

                return panel;
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

    public HotPanelHome getPerformancesWeekly(int idUser, String idDevice, String idRecipient, String publicIp) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT 'Essa Semana' day,count(*) delivereds, sum(fee_delivery) fee, date_format(his.date, '%y-%m-%d') date ");
                sqlConstruct.append(", deliveryman.id_device , ip.public_ip ip FROM hot_users_delivery_historic his ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery deliveryman ");
                sqlConstruct.append("ON deliveryman.id=his.id_user_delivery ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access ip ");
                sqlConstruct.append("ON ip.id_user_delivery=deliveryman.id ");
                sqlConstruct.append("WHERE deliveryman.id=? AND deliveryman.id_device=? ");
                sqlConstruct.append("AND id_recipient=? AND ");
                sqlConstruct.append("WEEK((date_format(his.date,'%y-%m-%d')))=WEEK((date_format(now(),'%y-%m-%d'))); ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);

                ResultSet rs = pst.executeQuery();

                HotPanelHome panel = null;
                if (rs.next()) {
                    panel = new HotPanelHome();
                    panel.setDelivered(rs.getInt("delivereds"));
                    panel.setTitle(rs.getString("day"));
                    panel.setAmount(rs.getDouble("fee"));
                    panel.setPublicIP(rs.getString("ip"));
                    panel.setIdDevice(rs.getString("id_device"));
                }

                con.close();
                pst.close();

                if (publicIp != null && !publicIp.equals(panel.getPublicIP())
                        && idDevice != null && !idDevice.equals(panel.getIdDevice())) {

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().

                    //return null ;
                }

                return panel;
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

    public HotPanelHome getPerformancesMonthly(int idUser, String idDevice, String idRecipient, String publicIp) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT 'Esse Mês' day,count(*) delivereds, sum(fee_delivery) fee, date_format(his.date, '%y-%m-%d') date ");
                sqlConstruct.append(", deliveryman.id_device, ip.public_ip ip FROM hot_users_delivery_historic his ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery deliveryman ");
                sqlConstruct.append("ON deliveryman.id=his.id_user_delivery ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access ip ");
                sqlConstruct.append("ON ip.id_user_delivery=deliveryman.id ");
                sqlConstruct.append("WHERE deliveryman.id=? AND deliveryman.id_device=? ");
                sqlConstruct.append("AND id_recipient=? AND ");
                sqlConstruct.append("MONTH((date_format(his.date,'%y-%m-%d')))=MONTH((date_format(now(),'%y-%m-%d'))); ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.addBatch("SET NAMES utf8;");
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);

                ResultSet rs = pst.executeQuery();

                HotPanelHome panel = null;
                if (rs.next()) {
                    panel = new HotPanelHome();
                    panel.setDelivered(rs.getInt("delivereds"));
                    panel.setTitle(rs.getString("day"));
                    panel.setAmount(rs.getDouble("fee"));
                    panel.setPublicIP(rs.getString("ip"));
                    panel.setIdDevice(rs.getString("id_device"));
                }

                con.close();
                pst.close();

                if (publicIp != null && !publicIp.equals(panel.getPublicIP())
                        && idDevice != null && !idDevice.equals(panel.getIdDevice())) {

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().

                    //return null ;
                }

                return panel;
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

    public boolean updateStatusOperation(int idUser, String idDevice, String idRecipient, String publicIp, boolean status) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_users_delivery SET status_operation=?  ");
                sqlConstruct.append("WHERE id=? AND id_device=? AND id_recipient=?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setBoolean(1, status);
                pst.setInt(2, idUser);
                pst.setString(3, idDevice);
                pst.setString(4, idRecipient);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();


                return (linesAffecteds > 0 ? status : !status);
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
        return false;
    }

    public boolean acceptRunning(int idUser, int idOrder, boolean acceptRunning) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_orders SET id_deliveryman=?  ");
                sqlConstruct.append("WHERE id=? AND status='for_delivery' AND status_payment='PAID'; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idUser);
                pst.setInt(2, idOrder);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();


                return (linesAffecteds > 0 ? true : false);
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
        return false;
    }

    public boolean acceptRunning(int idUser, List<Integer> idOrders) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_orders SET id_deliveryman=?  ");
                sqlConstruct.append("WHERE id=? AND status='for_delivery' AND (status_payment='PAID' OR status_payment='AUTHORIZED') ");
                sqlConstruct.append("AND date_format(date_created, '%y-%m-%d')=date_format(now(),'%y-%m-%d') AND ");
                sqlConstruct.append("id_deliveryman IS NULL; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);

                for (Integer idOrder : idOrders) {
                    pst.setInt(1, idUser);
                    pst.setInt(2, idOrder);
                    pst.addBatch();

                }

                int[] linesAffecteds = pst.executeBatch();
                boolean result = false;

                for (int i = 0; i < linesAffecteds.length; i++) {
                    result = linesAffecteds[i] == 0 ? false : true;
                    if (!result)
                        break;
                }

                con.close();
                pst.close();

                return result;
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
        return true;
    }

    public ArrayList<LAddress> getAddressRunning(int idUser, String idDevice, String publicIP) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        LAddress collectionAddress = new DaoSystemConfiguration().getCurrentLocationBusiness();

        ArrayList<LAddress> addresses = new ArrayList<>();
        addresses.add(collectionAddress);

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ord.id,country, state, city, neighborhood, street, number, complement,latitude, longitude, access.public_ip ");
                sqlConstruct.append(", complement,latitude, longitude FROM hot_orders ord  ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery deliveryman ON deliveryman.id=ord.id_deliveryman ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access access ");
                sqlConstruct.append("ON access.id_user_delivery=deliveryman.id ");
                sqlConstruct.append(" WHERE id_deliveryman=? AND deliveryman.id_device=? ");
                sqlConstruct.append("AND (ord.status_payment='PAID' OR ord.status_payment='AUTHORIZED') AND ord.status='for_delivery' ");
                sqlConstruct.append("AND date_format(ord.date_created, '%y-%m-%d')=date_format(now(),'%y-%m-%d') ");
                sqlConstruct.append("GROUP BY ord.id;");


                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);

                ResultSet rs = pst.executeQuery();

                String publiIPRegistered = null;
                LAddress a = null;
                while (rs.next()) {
                    a = new LAddress();
                    a.setId(rs.getInt("ord.id"));
                    a.setContry(rs.getString("country"));
                    a.setState(rs.getString("state"));
                    a.setCity(rs.getString("city"));
                    a.setNeighborhood(rs.getString("neighborhood"));
                    a.setStreet(rs.getString("street"));
                    a.setNumber(rs.getInt("number"));
                    a.setComplement(rs.getString("complement"));
                    a.setLatitude(rs.getDouble("latitude"));
                    a.setLongitude(rs.getDouble("longitude"));

                    publiIPRegistered = rs.getString("public_ip");
                    addresses.add(a);
                }

                con.close();
                pst.close();

                if (idDevice != null && publiIPRegistered != null && publiIPRegistered.equals(idDevice)) {
                    if (publicIP != null && !publicIP.equals(publiIPRegistered))
                        insertPublicIP(idUser, publicIP);

                } else {
                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().

                    // return new ArrayList<>() ;
                }


                return addresses;
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

    public ArrayList<HotDelivery> getRunning(int idUser, String idDevice, String publicIP, JSONArray ids) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        LAddress colectPoint = new DaoSystemConfiguration().getCurrentLocationBusiness();

        ArrayList<HotDelivery> deliverys = new ArrayList<>();
        deliverys.add(new HotDelivery("Hot Burguer", colectPoint));

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ord.id, user.name,country, state, city, neighborhood, street, number, complement,latitude, longitude, access.public_ip ");
                sqlConstruct.append(", complement,latitude, longitude FROM hot_orders ord  ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery deliveryman ON deliveryman.id=ord.id_deliveryman ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access access ");
                sqlConstruct.append("ON access.id_user_delivery=deliveryman.id ");
                sqlConstruct.append("LEFT JOIN hot_users user ");
                sqlConstruct.append("ON user.id=ord.id_user ");
                sqlConstruct.append("WHERE id_deliveryman=? AND deliveryman.id_device=? ");
                sqlConstruct.append("AND (ord.status_payment='PAID' OR ord.status_payment='AUTHORIZED') AND ord.status='for_delivery' ");
                sqlConstruct.append("AND date_format(ord.date_created, '%y-%m-%d')=date_format(now(),'%y-%m-%d') ");
                sqlConstruct.append("AND ord.id IN (");
                for (int i = 0; i < ids.length(); i++) {
                    if (i == 0)
                        sqlConstruct.append("?");
                    else
                        sqlConstruct.append(",?");
                }

                sqlConstruct.append(") GROUP BY ord.id;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);

                int lastParam = 3;
                for (int i = 0; i < ids.length(); i++) {
                    pst.setInt(lastParam, ids.getInt(i));
                    lastParam++;
                }

                ResultSet rs = pst.executeQuery();

                String publiIPRegistered = null;
                LAddress a = null;
                while (rs.next()) {
                    a = new LAddress();
                    a.setId(rs.getInt("ord.id"));
                    a.setContry(rs.getString("country"));
                    a.setState(rs.getString("state"));
                    a.setCity(rs.getString("city"));
                    a.setNeighborhood(rs.getString("neighborhood"));
                    a.setStreet(rs.getString("street"));
                    a.setNumber(rs.getInt("number"));
                    a.setComplement(rs.getString("complement"));
                    a.setLatitude(rs.getDouble("latitude"));
                    a.setLongitude(rs.getDouble("longitude"));

                    publiIPRegistered = rs.getString("public_ip");
                    deliverys.add(new HotDelivery(rs.getString("user.name"), a));
                }

                con.close();
                pst.close();

                if (idDevice != null && publiIPRegistered != null && publiIPRegistered.equals(idDevice)) {
                    if (publicIP != null && !publicIP.equals(publiIPRegistered))
                        insertPublicIP(idUser, publicIP);

                } else {
                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().

                    // return new ArrayList<>() ;
                }


                return deliverys;
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

    public ArrayList<HotHistoricRegister> getHistoricDelivery(int idUser, String idDevice, String idRecipient, int idLastItem, String publicIp) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<HotHistoricRegister> itens = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append(" SELECT his.id, sum(fee_delivery)amount, count(*)count_delivery,  ");
                sqlConstruct.append("DAYNAME(his.date) day_name, MONTHNAME(his.date) month, EXTRACT(YEAR FROM his.date) year, ");
                sqlConstruct.append("DATE_FORMAT(his.date,'%d-%m-%Y') date FROM hot_users_delivery_historic his ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery userd ");
                sqlConstruct.append("ON userd.id=his.id_user_delivery ");
                sqlConstruct.append("WHERE userd.id=? AND userd.id_device=? ");
                sqlConstruct.append("AND userd.id_recipient=? ");
                sqlConstruct.append("AND his.id > ? GROUP BY date_format(his.date, '%y-%m-%d'); ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.addBatch("SET lc_time_names = 'pt_PT';");
                pst.executeBatch();

                pst.setInt(1, idUser);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);
                pst.setInt(4, idLastItem);

                ResultSet rs = pst.executeQuery();

                int countMaxResponse = 18;
                int counter = 0;

                HotHistoricRegister reg = null;
                while (rs.next()) {
                    reg = new HotHistoricRegister();
                    reg.setAmount(rs.getDouble("amount"));
                    reg.setCountDelivery(rs.getInt("count_delivery"));
                    reg.setDate(rs.getString("date"));
                    reg.setDayName(rs.getString("day_name"));
                    reg.setMonth(rs.getString("month"));
                    reg.setId(rs.getInt("id"));

                    counter++;

                    if (counter <= countMaxResponse) {
                        itens.add(reg);
                    } else {
                        itens.get(itens.size() - 1).setHaveMore(true);
                        break;
                    }
                }
                con.close();
                pst.close();

                return itens;
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
        return itens;
    }

    public void notifyCollectPoint() {
        LAddress collectionAddress = new DaoSystemConfiguration().getCurrentLocationBusiness();

    }

    public OneSignalResponse notifyDeliveryMan(String title, String subTitle, String message, int idDeliveryMan, JSONObject data) {
        return new DaoHotNotification()
                .notifyDeliveryMan(title, subTitle, message, idDeliveryMan, data);
    }

    public OneSignalResponse notifyDeliveryMan(String title, String subTitle, String message, ArrayList<String> idsDeliveryMan, JSONObject data) {
        return new DaoHotNotification()
                .notifyDeliveryMan(title, subTitle, message, idsDeliveryMan, data);
    }

    public OneSignalResponse notifyClient(String title, String subTitle, String message, ArrayList<String> idClients, JSONObject data) {
        return new DaoHotNotification()
                .notifyClient(title, subTitle, message, idClients, data);
    }

    public boolean checkDataForNotification(int idDeliveryman, String idDevice, String idRecipient, int idOrder) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery del ");
                sqlConstruct.append("ON ord.id_deliveryman=del.id ");
                sqlConstruct.append("WHERE del.id=? AND del.id_device=? ");
                sqlConstruct.append("AND del.id_recipient=? AND ord.id=? ");
                sqlConstruct.append("AND date_format(date_created, '%y-%m-%d')=date_format(now(), '%y-%m-%d'); ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.addBatch("SET lc_time_names = 'pt_PT';");
                pst.executeBatch();

                pst.setInt(1, idDeliveryman);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);
                pst.setInt(4, idOrder);

                ResultSet rs = pst.executeQuery();

                boolean check = false;
                if (rs.next()) {
                    check = true;
                }
                con.close();
                pst.close();

                return check;
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

        return false;
    }

    public boolean checkDataForNotification(int idDeliveryman, String idDevice, String idRecipient, int[] idOrders) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery del ");
                sqlConstruct.append("ON ord.id_deliveryman=del.id ");
                sqlConstruct.append("WHERE del.id=? AND del.id_device=? ");
                sqlConstruct.append("AND del.id_recipient=? AND ord.id=? ");
                sqlConstruct.append("WHERE userd.id=? AND userd.id_device=? ");
                sqlConstruct.append("AND date_format(date_created, '%y-%m-%d')=date_format(now(), '%y-%m-%d'); ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.addBatch("SET lc_time_names = 'pt_PT';");
                pst.executeBatch();

                pst.setInt(1, idDeliveryman);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);
                //pst.setInt(4, idOrder);

                ResultSet rs = pst.executeQuery();

                boolean check = false;
                if (rs.next()) {
                    check = true;
                }
                con.close();
                pst.close();

                return check;
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

        return false;
    }

    public int getCountRunnings(int idDeliveryman, String idDevice, String idRecipient, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *,count(*) count FROM hot_users_delivery_historic his ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery del ");
                sqlConstruct.append("ON del.id=his.id_user_delivery ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery_ip_access acss ");
                sqlConstruct.append("ON acss.id_user_delivery=del.id ");
                sqlConstruct.append("WHERE del.id=? AND del.id_device=? AND del.id_recipient=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idDeliveryman);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);
                //pst.setInt(4, idOrder);

                ResultSet rs = pst.executeQuery();

                int count = 0;
                String rsIP = null;
                String rsIdDevice = null;
                if (rs.next()) {
                    count = rs.getInt("count");
                    rsIP = rs.getString("public_ip");
                    rsIdDevice = rs.getString("del.id_device");
                }
                con.close();
                pst.close();

                if (rsIP != null && !rsIP.equals(ip) && !rsIdDevice.equals(idDevice)) {

                } else if (rsIP != null && !rsIP.equals(ip) && rsIdDevice.equals(idDevice)) {
                    insertPublicIP(idDeliveryman, ip);
                }

                return count;
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

        return 0;
    }

    public boolean saveDeliveryToHistory(int idDeliveryman, int idOrder, double feeDelivery, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO hot_users_delivery_historic (id_user_delivery, id_order, fee_delivery) ");
                sqlConstruct.append("SELECT * FROM (SELECT ?, ?, ?) AS tmp ");
                sqlConstruct.append("WHERE EXISTS ( SELECT id FROM hot_orders WHERE id_deliveryman=? AND id=? ");
                sqlConstruct.append("AND status='finalized' AND status_payment='PAID' ) LIMIT 1;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idDeliveryman);
                pst.setInt(2, idOrder);
                pst.setDouble(3, feeDelivery);
                pst.setInt(4, idDeliveryman);
                pst.setInt(5, idOrder);
                //pst.setInt(4, idOrder);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return (linesAffecteds > 0);
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

        return false;
    }

    public ArrayList<Integer> selectDeliveryManForDelivery(int idDeliveryman, int countOrdersAvailables) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<Integer> ids = new ArrayList<>();
        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT del.id, del.full_name, del.id_device, del.id_recipient ,(SELECT date FROM hot_users_delivery_historic ");
                sqlConstruct.append("WHERE id_user_delivery=del.id ORDER BY date DESC LIMIT 1) last_delivery FROM hot_users_delivery_historic his ");
                sqlConstruct.append("RIGHT JOIN hot_users_delivery del ON del.id=his.id_user_delivery LEFT JOIN hot_orders ord ON ord.id_deliveryman IS NULL ");
                sqlConstruct.append("WHERE del.id != ? AND del.status='active' AND del.status_operation=1 AND id_recipient IS NOT NULL ");
                sqlConstruct.append("AND (ord.id_deliveryman IS NULL AND (ord.status='for_delivery' OR ord.status='waiting') ");
                sqlConstruct.append("OR ord.id_deliveryman > 0 AND ord.status='delivered' OR ord.status='finalized') ");
                sqlConstruct.append("AND (his.id_user_delivery=del.id OR his.id_user_delivery IS NULL) ");
                sqlConstruct.append("AND date_format(ord.date_created,'%y-%m-%d') = date_format(now(),'%y-%m-%d') ");
                //sqlConstruct.append("AND TIMESTAMPDIFF(MINUTE, ord.date_created, now()) < 45 ");
                sqlConstruct.append("GROUP BY del.id ORDER BY last_delivery ASC LIMIT ?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idDeliveryman);
                pst.setInt(2, countOrdersAvailables);

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    /*account = new HotDeliveryMan();
                    account.setId(rs.getInt("id"));
                    account.setIdRecipient(rs.getString("id_recipient"));
                    account.setHashKey(rs.getString("id_hash_key"));
                    account.setName(rs.getString("full_name"));
                    account.getDocument().setCPF(rs.getString("cpf"));
                    account.getContact().getPhone().setFullNumber(rs.getString("cel"));
                    account.setStatus(rs.getString("status"));
                    account.setStatusOperation(rs.getBoolean("status_operation"));
                    account.setDateOfBirth(rs.getString("date_of_birth"));
                    account.setIpAccess(rs.getString("public_ip"));*/
                    //account.getBankAccount().

                    ids.add(rs.getInt("del.id"));
                }

                con.close();
                pst.close();


                return ids;
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

        return ids;
    }

    public synchronized OneSignalResponse notifyDeliveryManNeoRunning(int type, int idDeliveryman, JSONArray idOrders) {

        DaoHotManager manager = new DaoHotManager();
        HotConfigurations configs = manager.getHotConfiguration();
        OneSignalResponse result = null;

        switch (type) {
            case DaoHotNotification.TypeNotification.Deliveryman.NEW_RUNNING: {
                JSONObject data = new JSONObject();
                data.put("type", 1);
                data.put("id_orders", idOrders);
                data.put("amount", (configs.getDeliveryMan().getFeeDelivery() * idOrders.length()));
                data.put("count_delivery", idOrders.length());
                data.put("point1_km", "0.0km");

                DaoHotNotification notification = new DaoHotNotification();
                result = notification.notifyDeliveryMan(
                        "Nova Corrida",
                        "Recebível - R$ " + data.getDouble("amount"),
                        "Recebível - R$ " + data.getDouble("amount"),
                        idDeliveryman, data);
            }
            case DaoHotNotification.TypeNotification.Deliveryman.MESSAGE: {
                JSONObject data = new JSONObject();
                data.put("type", 2);
                data.put("id_orders", idOrders);
                data.put("amount", (configs.getDeliveryMan().getFeeDelivery() * idOrders.length()));
                data.put("count_delivery", idOrders.length());
                data.put("point1_km", "0.5km");
            }
        }

        if (result == null || result != null && result.getResponseCode() != 200) {

            Timer timer = new Timer();
            OneSignalResponse finalResult = result;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    notifyDeliveryManNeoRunning(type, idDeliveryman, idOrders);
                    if (finalResult.getResponseCode() == 200)
                        timer.cancel();
                }
            }, 0, 60000);
        }
        return result;
    }

    public JSONArray getHoldingDeliverymans(int idManager, String idDevice, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        int totalRegisters = 19;

        JSONArray array = new JSONArray();


        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ords.id ord_id, del.id del_id,del.full_name ");
                sqlConstruct.append("FROM hot_users_delivery del LEFT JOIN hot_orders ords ");
                sqlConstruct.append("ON ords.id_deliveryman = del.id LEFT JOIN hot_managers man ON man.id > 0 ");
                sqlConstruct.append("WHERE ords.status='deliveryman_holding' AND ords.status_payment='AUTHORIZED' ");
                sqlConstruct.append("AND date_format(ords.date_created, '%y-%m-%d') = date_format(now(), '%y-%m-%d') ");
                sqlConstruct.append("AND ords.id_deliveryman > 0 AND man.id=? AND man.id_device=? ");
                sqlConstruct.append("AND del.status='active'; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idManager);
                pst.setString(2, idDevice);

                ResultSet rs = pst.executeQuery();

                int actualIdDeliveryman = 0;
                int counter = 0;

                StringBuilder sb = null;
                StringBuilder dels = new StringBuilder();

                JSONObject del = null;
                while (rs.next()) {

                    counter++;

                    if (counter < totalRegisters) {

                        int idDeliveryman = rs.getInt("del_id");
                        String idOrder = rs.getString("ord_id");
                        String deliverymanName = rs.getString("full_name");

                        del = new JSONObject();
                        del.put("id", idDeliveryman);
                        del.put("name", deliverymanName);

                        if (sb != null && !sb.toString().contains(idOrder)
                                && actualIdDeliveryman == idDeliveryman) {
                            sb.append(" • ");
                            sb.append(idOrder);

                            del.put("id_orders", sb.substring(0, sb.length() - 3));
                        } else {
                            actualIdDeliveryman = idDeliveryman;

                            if (sb != null)
                                del.put("id_orders", sb.substring(0, sb.length() - 3));

                            sb = new StringBuilder();
                            sb.append(idOrder);
                            del.put("id_orders", sb.substring(0, sb.length() - 3));
                        }

                        if (!dels.toString().contains(String.valueOf(idDeliveryman))) {
                            array.put(del);
                            dels.append(del.get("id"));
                        } else {
                            array.getJSONObject(array.length() - 1).put("id_orders", sb.toString());
                        }

                    } else {
                        array.getJSONObject(array.length() - 1).put("have_more", false);
                    }
                }

                con.close();
                pst.close();

          /*      if (account != null && deliveryMan.getIpAccess() != null &&
                        !deliveryMan.getIpAccess().equals(account.getIpAccess())) {
                    deliveryMan = new HotDeliveryMan();

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().
                }*/

            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();

                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return array;
    }

    public JSONArray getActivesDeliverymans(int idManager, String idDevice, int idLastDeliveryman, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        int totalRegisters = 19;

        JSONArray array = new JSONArray();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT del.id del_id,del.full_name ");
                sqlConstruct.append("FROM hot_users_delivery del LEFT JOIN hot_managers man ON man.id > 0 ");
                sqlConstruct.append("WHERE man.id=? AND man.id_device=? ");
                sqlConstruct.append("AND del.status='active' AND del.status_operation=1 AND del.id>?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idManager);
                pst.setString(2, idDevice);
                pst.setInt(3, idLastDeliveryman);

                ResultSet rs = pst.executeQuery();

                int actualIdDeliveryman = 0;
                int counter = 0;

                StringBuilder sb = null;
                StringBuilder dels = new StringBuilder();

                JSONObject del = null;
                while (rs.next()) {

                    counter++;

                    if (counter < totalRegisters) {

                        int idDeliveryman = rs.getInt("del_id");
                        //String idOrder = rs.getString("ord_id");
                        String deliverymanName = rs.getString("full_name");

                        del = new JSONObject();
                        del.put("id", idDeliveryman);
                        del.put("name", deliverymanName);

                        /*if (sb != null && !sb.toString().contains(idOrder)
                                && actualIdDeliveryman == idDeliveryman) {
                            sb.append(" • ");
                            sb.append(idOrder);

                            del.put("id_orders", sb.substring(0, sb.length() - 3));
                        } else {
                            actualIdDeliveryman = idDeliveryman;

                            if (sb != null)
                                del.put("id_orders", sb.substring(0, sb.length() - 3));

                            sb = new StringBuilder();
                            sb.append(idOrder);
                            del.put("id_orders", sb.substring(0, sb.length() - 3));
                        }*/

                       /* if (!dels.toString().contains(String.valueOf(idDeliveryman))) {
                            array.put(del);
                            dels.append(del.get("id"));
                        } else {
                            array.getJSONObject(array.length() - 1).put("id_orders", sb.toString());
                        }*/

                        array.put(del);
                    } else {
                        array.getJSONObject(array.length() - 1).put("have_more", false);
                    }
                }

                con.close();
                pst.close();

          /*      if (account != null && deliveryMan.getIpAccess() != null &&
                        !deliveryMan.getIpAccess().equals(account.getIpAccess())) {
                    deliveryMan = new HotDeliveryMan();

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().
                }*/

            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();

                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return array;
    }

    public JSONArray getQueueDeliverymans(int idManager, String idDevice,int idLastDeliveryman, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        int totalRegisters = 19;

        JSONArray array = new JSONArray();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT del.id del_id,del.full_name, del.email, del.cel ");
                sqlConstruct.append("FROM hot_users_delivery del ");
                sqlConstruct.append("LEFT JOIN hot_managers man ON man.id > 0 ");
                sqlConstruct.append("WHERE man.id=? AND man.id_device=? ");
                sqlConstruct.append("AND del.status='waiting' AND del.status_operation=0 AND del.id > ? ");
                sqlConstruct.append("AND del.id_recipient IS NOT NULL AND del.photo_profile IS NOT NULL ");
                sqlConstruct.append("AND del.photo_identity_front IS NOT NULL AND del.cpf IS NOT NULL; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idManager);
                pst.setString(2, idDevice);
                pst.setInt(3, idLastDeliveryman);

                ResultSet rs = pst.executeQuery();

                int actualIdDeliveryman = 0;
                int counter = 0;

                StringBuilder sb = null;
                StringBuilder dels = new StringBuilder();

                JSONObject del = null;
                while (rs.next()) {

                    counter++;

                    if (counter < totalRegisters) {

                        int idDeliveryman = rs.getInt("del_id");
                        String email = rs.getString("email");
                        String cel = rs.getString("cel");
                        String deliverymanName = rs.getString("full_name");

                        del = new JSONObject();
                        del.put("id", idDeliveryman);
                        del.put("name", deliverymanName);
                        del.put("email", email);
                        del.put("cel", cel);

                        /*if (sb != null && !sb.toString().contains(idOrder)
                                && actualIdDeliveryman == idDeliveryman) {
                            sb.append(" • ");
                            sb.append(idOrder);

                            del.put("id_orders", sb.substring(0, sb.length() - 3));
                        } else {
                            actualIdDeliveryman = idDeliveryman;

                            if (sb != null)
                                del.put("id_orders", sb.substring(0, sb.length() - 3));

                            sb = new StringBuilder();
                            sb.append(idOrder);
                            del.put("id_orders", sb.substring(0, sb.length() - 3));
                        }*/

                        /*if (!dels.toString().contains(String.valueOf(idDeliveryman))) {
                            array.put(del);
                            dels.append(del.get("id"));
                        } else {
                            array.getJSONObject(array.length() - 1).put("id_orders", sb.toString());
                        }*/
                        array.put(del);
                    } else {
                        array.getJSONObject(array.length() - 1).put("have_more", false);
                    }
                }

                con.close();
                pst.close();

          /*      if (account != null && deliveryMan.getIpAccess() != null &&
                        !deliveryMan.getIpAccess().equals(account.getIpAccess())) {
                    deliveryMan = new HotDeliveryMan();

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().
                }*/

            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();

                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return array;
    }

    public boolean activeDeliveryman(int idManager, String idDevice,int idDeliveryman, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_users_delivery del LEFT JOIN hot_managers man ");
                sqlConstruct.append("ON man.id > 0 SET del.status = 'active' ");
                sqlConstruct.append("WHERE man.id=? AND man.id_device=? AND del.id=?;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idManager);
                pst.setString(2, idDevice);
                pst.setInt(3, idDeliveryman);

                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

          /*      if (account != null && deliveryMan.getIpAccess() != null &&
                        !deliveryMan.getIpAccess().equals(account.getIpAccess())) {
                    deliveryMan = new HotDeliveryMan();

                    // Notify o entregador de se reconhece o acesso atual
                    //ApplicationStart.services.getApiEmailServer().
                }*/

                return (linesAffecteds > 0);
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();

                    Logger.getLogger(DaoAddress.class.getName()).log(Level.SEVERE, null, ex);
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
        return false;
    }
}
