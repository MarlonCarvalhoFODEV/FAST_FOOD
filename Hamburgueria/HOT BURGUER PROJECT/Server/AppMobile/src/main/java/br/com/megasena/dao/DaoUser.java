package br.com.megasena.dao;

import br.com.megasena.itens.ITService;
import br.com.megasena.itens.ITUser;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoUser {

    public boolean insertUser(ITUser user) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO user_account(id_device, email, name) SELECT * FROM (SELECT ? as id_device, ? as email, ? as name ");
                sqlConstruct.append(") AS tmp ");
                sqlConstruct.append("WHERE NOT EXISTS ( SELECT email FROM user_account WHERE email = ? ) LIMIT 1;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, user.getIdDevice());
                pst.setString(2, user.getEmail());
                pst.setString(3, user.getName());

                try {
                    pst.executeUpdate();

                    ResultSet rs = pst.getGeneratedKeys();
                    if (rs.next())
                        user.setId(rs.getInt(1));

                    con.close();
                    pst.close();

                    if (user.getId() > 0)
                        return true;

                } catch (SQLIntegrityConstraintViolationException e) {
                    return false;
                }
                return false;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return false;
    }

    public ITUser loginUser(String email, String idDevice) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT acc.*, sc.id_service, ss.name sc_name ,(DATEDIFF(sc.expirate, now())>1) available, ");
                sqlConstruct.append("DATEDIFF(sc.expirate, now()) expirate_days FROM user_account acc ");
                sqlConstruct.append("LEFT JOIN user_services_contracteds sc ");
                sqlConstruct.append("ON sc.id_user=acc.id ");
                sqlConstruct.append("LEFT JOIN system_services ss ");
                sqlConstruct.append("ON ss.id=sc.id_service ");
                sqlConstruct.append("WHERE acc.email=? AND acc.id_device=?;");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, email);
                pst.setString(2, idDevice);

                ResultSet rs = pst.executeQuery();

                ITUser user = null ;
                ITService service ;
                while(rs.next()) {
                    if(user == null) {
                        user = new ITUser();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                    }
                    service = new ITService();
                    service.setId(rs.getInt("id_service"));
                    service.setName(rs.getString("sc_name"));
                    service.setAvailable(rs.getBoolean("available"));
                    service.setExpirateDays(rs.getInt("expirate_days"));

                    user.getAvailableServices().put(service.getId(), service);
                }

                con.close();
                pst.close();

                return user;
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoMegaSena.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return null;
    }

    public void updateUser(String email, String idDevice) {

    }

}
