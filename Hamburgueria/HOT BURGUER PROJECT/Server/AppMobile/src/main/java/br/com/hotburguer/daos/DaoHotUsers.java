package br.com.hotburguer.daos;

import br.com.comprazencasa.database.DaoAddress;
import br.com.comprazencasa.database.DaoUser;
import br.com.hotburguer.itens.HotUser;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoHotUsers {

    public HotUser insertAccount(HotUser hotUser, String ip) {

        boolean inQuarantine = checkExistQuarantine(ip);
        boolean validEmail = checkValidTypeEmail(hotUser.getEmail());

        if (validEmail && !inQuarantine) {
            Connection con = DBConnections.connect();
            PreparedStatement pst = null;

            if (con != null) {
                try {

                    StringBuilder sqlConstruct = new StringBuilder();
                    sqlConstruct.append("INSERT INTO hot_users (name, email, date_of_birth, social_login, device_id, photo) ");
                    sqlConstruct.append("SELECT * FROM (SELECT ? AS name,? AS email, ? AS date_of_birth, ");
                    sqlConstruct.append("? AS social_login, ?  AS device_id, ? AS photo) AS tmp ");
                    sqlConstruct.append("WHERE NOT EXISTS ( SELECT email FROM hot_users WHERE email = ? ");
                    sqlConstruct.append(") LIMIT 1;");

                    pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                    pst.setString(1, hotUser.getName());
                    pst.setString(2, hotUser.getEmail());
                    pst.setString(3, hotUser.getBirthOfDate());
                    pst.setString(4, hotUser.getSocialLogin());
                    pst.setString(5, hotUser.getDeviceID());
                    pst.setString(6, hotUser.getUrlPhoto());
                    pst.setString(7, hotUser.getEmail());

                    try {
                        pst.executeUpdate();

                        ResultSet rs = pst.getGeneratedKeys();
                        if (rs.next())
                            hotUser.setId(rs.getInt(1));

                        con.close();
                        pst.close();

                        if (hotUser.getId() > 0)
                            insertIp(hotUser.getId(), ip);

                    } catch (SQLIntegrityConstraintViolationException e) {
                        hotUser.setId(-1);
                    }
                    return hotUser;
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
        } else if(!validEmail){
            insertInQuarentine(ip);
            hotUser.setId(-1);
        }
        else{
            hotUser.setId(-1);
        }

        return null;
    }

    public boolean insertIp(int idUser, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO hot_users_ips (id_user, public_ip) ");
                sqlConstruct.append("SELECT * FROM (SELECT ? AS id_user,? AS public_ip) AS tmp ");
                sqlConstruct.append(" WHERE NOT EXISTS ( SELECT id_user FROM hot_users_ips WHERE id_user = ? AND public_ip=? ");
                sqlConstruct.append(") AND EXISTS ( SELECT id_user FROM hot_users_ips WHERE id_user = ? AND public_ip=? ) ");
                sqlConstruct.append("OR (SELECT count(*) FROM  hot_users_ips WHERE id_user=?) = 0  LIMIT 1; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, idUser);
                pst.setString(2, ip);
                pst.setInt(3, idUser);
                pst.setString(4, ip);
                pst.setInt(5, idUser);
                pst.setString(6, ip);
                pst.setInt(7, idUser);
                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    con.close();
                    pst.close();

                    return true;
                }

                con.close();
                pst.close();

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

    public HotUser selectUser(HotUser hotUser, String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users users ");
                sqlConstruct.append("LEFT JOIN hot_users_ips ips ");
                sqlConstruct.append("ON ips.id_user=users.id ");
                sqlConstruct.append("LEFT JOIN hot_users_quarantine qt ");
                sqlConstruct.append("ON qt.public_ip = ips.public_ip ");
                sqlConstruct.append("WHERE users.id>0 AND email=? AND device_id=? AND ips.public_ip=? ");
                sqlConstruct.append("AND qt.public_ip IS NULL AND ips.public_ip IS NOT NULL limit 1; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, hotUser.getEmail());
                pst.setString(2, hotUser.getDeviceID());
                pst.setString(3, ip);
                ResultSet rs = pst.executeQuery();

                HotUser user = null;
                if (rs.next()) {
                    user = new HotUser();
                    user.setId(rs.getInt("users.id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setIdCustomer(rs.getInt("id_customer"));
                    user.setCpf(rs.getString("cpf"));
                    user.setBirthOfDate(rs.getString("date_of_birth"));
                    user.getContact().getPhone().setFullNumber(rs.getString("cel"));
                    user.setDeviceID(rs.getString("device_id"));
                    user.setUrlPhoto(rs.getString("photo"));

                    insertIp(user.getId(), ip);

                }

                con.close();
                pst.close();


                return user;
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

    public HotUser selectUser(HotUser hotUser, String ip, String secretIP) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users users ");
                sqlConstruct.append("LEFT JOIN hot_users_ips ips ");
                sqlConstruct.append("ON ips.id_user=users.id ");
                sqlConstruct.append("LEFT JOIN hot_users_quarantine qt ");
                sqlConstruct.append("ON qt.public_ip=ips.public_ip ");
                sqlConstruct.append("");
                sqlConstruct.append("WHERE users.id>0 AND email=? AND device_id=? AND qt.public_ip IS NULL AND (ips.public_ip=? OR ips.public_ip=?);");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, hotUser.getEmail());
                pst.setString(2, hotUser.getDeviceID());
                pst.setString(3, ip);
                pst.setString(4, secretIP);
                ResultSet rs = pst.executeQuery();

                HotUser user = null;
                if (rs.next()) {
                    user = new HotUser();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setIdCustomer(rs.getInt("id_customer"));
                    user.setCpf(rs.getString("cpf"));
                    user.setBirthOfDate(rs.getString("date_of_birth"));
                    user.getContact().getPhone().setFullNumber(rs.getString("cel"));
                    user.setDeviceID(rs.getString("device_id"));
                    user.setUrlPhoto(rs.getString("photo"));
                }

                con.close();
                pst.close();

                insertIp(user.getId(), secretIP);

                return user;
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

    public HotUser selectUser(int idUser) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users WHERE id=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                ResultSet rs = pst.executeQuery();

                HotUser user = null;
                if (rs.next()) {
                    user = new HotUser();
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setIdCustomer(rs.getInt("id_customer"));
                    user.setCpf(rs.getString("cpf"));
                    user.getContact().getPhone().setFullNumber(rs.getString("cel"));
                }

                con.close();
                pst.close();

                return user;
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

    public boolean completeRegister(HotUser user) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                if (user.getId() == 0) {
                    user.setId(getUserIDFromEmail(user.getEmail()));
                }

                StringBuilder sb = new StringBuilder();
                sb.append("UPDATE hot_users SET name=?, cpf=?, email=?, cel=?, id_customer=?, date_of_birth=? ");
                sb.append("WHERE id=?; ");

                pst = con.prepareStatement(sb.toString());
                pst.setString(1, user.getName());
                pst.setString(2, user.getCpf());
                pst.setString(3, user.getEmail());
                pst.setString(4, user.getContact().getPhone().getFullNumber());
                pst.setString(5, String.valueOf(user.getIdCustomer()));
                pst.setString(6, user.getBirthOfDate());
                pst.setInt(7, user.getId());

                pst.executeUpdate();

                con.close();
                pst.close();

                return true;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return false;
    }

    private int getUserIDFromEmail(String email) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        String sql = "SELECT id FROM hot_users WHERE email=?; ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, email);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    return rs.getInt("id");
                }
                con.close();
                pst.close();

                return 0;
            } catch (SQLException ex) {
                try {
                    con.close();
                    if (pst != null) pst.close();
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    if (pst != null) pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoUser.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return 0;
    }

    public byte[] getImage(int idProduct) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECt img FROM hot_products ");
                sqlConstruct.append("WHERE id=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idProduct);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] img = rs.getBytes("img");

                    con.close();
                    pst.close();

                    return img;
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
        return new byte[0];
    }

    private boolean checkExistQuarantine(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null ;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_users_quarantine WHERE public_ip=?; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, ip);
                ResultSet rs = pst.executeQuery();

                boolean exist = false;
                if (rs.next())
                    exist = true;

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

    private boolean checkValidTypeEmail(String email) {
        return !email.matches(".*\\.\\d{5}\\@+[yahoo, hotmail, gmail, uol, outlook].*");
    }

    private boolean insertInQuarentine(String ip) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO hot_users_quarantine (public_ip) ");
                sqlConstruct.append("SELECT * FROM (SELECT ? AS public_ip) AS tmp ");
                sqlConstruct.append(" WHERE NOT EXISTS ( SELECT public_ip FROM hot_users_quarantine WHERE public_ip = ? ");
                sqlConstruct.append(") ; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, ip);
                pst.setString(2, ip);
                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    con.close();
                    pst.close();

                    return true;
                }

                con.close();
                pst.close();

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
