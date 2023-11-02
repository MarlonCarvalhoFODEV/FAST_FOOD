/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import br.com.comprazencasa.collaborator.*;
import br.com.comprazencasa.util.SendImage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class DaoCollaborators {

    // OK VERIFY
    public CollaboratorAccount login(CollaboratorLogin login){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "select * from collaborators_registers colls " +
                        "left join coll_address addr " +
                        "on addr.id_coll=colls.id  " +
                        "left join coll_contact con " +
                        "on con.id_coll=colls.id  " +
                        "left join coll_function fun " +
                        "on fun.id_coll=colls.id  " +
                        "join coll_login log " +
                        "on log.id_coll = colls.id " +
                        "where log.email=? and log.password=? " ;

                pst = con.prepareStatement(sql);
                pst.setString(1, login.getEmail());
                pst.setString(2, login.getPassword());

                CollaboratorAccount account = null ;
                ResultSet rs = pst.executeQuery();

                while(rs.next()){
                    account = new CollaboratorAccount();
                    account.setId(rs.getInt("colls.id"));
                    account.setImgPath(rs.getString("img_path"));
                    account.setName(rs.getString("colls.name"));
                    account.setLastName(rs.getString("colls.last_name"));
                    account.setCpf(rs.getString("colls.cpf"));
                    account.setDate_birth(rs.getString("colls.date_birth"));
                    account.setDate(rs.getString("colls.date"));
                    account.setStatus(rs.getString("colls.status"));
                    account.getAddress().setState(rs.getString("addr.state"));
                    account.getAddress().setCity(rs.getString("addr.city"));
                    account.getAddress().setNeighborhood(rs.getString("addr.neighborhood"));
                    account.getAddress().setStreet(rs.getString("addr.street"));
                    account.getAddress().setResidenceNumber(rs.getInt("addr.residence_number"));
                    account.getAddress().setComplement(rs.getString("addr.complement"));
                    account.getContact().setEmail(rs.getString("con.email"));
                    account.getContact().setCel(rs.getString("con.cel"));
                    account.getContact().setTelResidence(rs.getString("con.tel_residence"));
                    account.getFunction().setSalary(rs.getDouble("fun.salary"));
                    account.getFunction().setResponsability(rs.getString("fun.responsibility"));
                    account.getFunction().setCompanyFunction(rs.getString("fun.company_function"));
                }

                pst.close();
                con.close();

                return account ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    
        return null ;
    }
    // OK VERIFY
    public synchronized boolean register(CollaboratorAccount account){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "INSERT INTO collaborators_registers (name,last_name, cpf, date_birth, status) "
                        + "VALUES (?,?,?,?,?) " ;
                pst = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, account.getName());
                pst.setString(2, account.getLastName());
                pst.setString(3, account.getCpf());
                pst.setString(4, account.getDate_birth());
                pst.setString(5, account.getStatus());
                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                int id = 0;
                if(rs.next()){
                    id = rs.getInt(1);
                }

                pst.close();
                con.close();

                boolean addressSuccess = false, contactSuccess = false,
                        functionSuccess = false, login = false;
                if(id > 0){
                    account.setId(id);
                    addressSuccess = insertAddress(account.getAddress());
                    contactSuccess = insertContact(account.getContact());
                    functionSuccess = insertFunction(account.getFunction());
                    login = insertLogin(account.getLogin());
                }

                boolean sucessAll = (addressSuccess && contactSuccess && functionSuccess && login) ;

                if(sucessAll == false){
                    deleteFromID(id);
                }
                return sucessAll;
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);

                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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

        return false ;
    }
    // OK VERIFY
    private synchronized boolean insertAddress(CollaboratorAddress address){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "INSERT INTO coll_address "
                        + "(id_coll,state,city, neighborhood, street, residence_number, complement) "
                        + "VALUES (?,?,?,?,?,?,?) " ;

                pst = con.prepareStatement(sql);
                pst.setInt(1, address.getId());
                pst.setString(2, address.getState());
                pst.setString(3, address.getCity());
                pst.setString(4, address.getNeighborhood());
                pst.setString(5, address.getStreet());
                pst.setInt(6, address.getResidenceNumber());
                pst.setString(7, address.getComplement());
                pst.execute();

                pst.close();
                con.close();

                return true ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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

        return false ;
    }
    
    private synchronized boolean insertContact(CollaboratorContact contact){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "INSERT INTO coll_contact "
                        + "(id_coll, email,cel, tel_residence) "
                        + "VALUES (?,?,?,?) " ;

                pst = con.prepareStatement(sql);
                pst.setInt(1, contact.getId());
                pst.setString(2, contact.getEmail());
                pst.setString(3, contact.getCel());
                pst.setString(4, contact.getTelResidence());
                pst.execute();

                pst.close();
                con.close();
                return true ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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

        return false ;
    }
    
    private synchronized boolean insertFunction(CollaboratorFunction function){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "INSERT INTO coll_function "
                        + "(id_coll, company_function,salary, responsibility) "
                        + "VALUES (?,?,?,?) " ;

                pst = con.prepareStatement(sql);
                pst.setInt(1, function.getId());
                pst.setString(2, function.getCompanyFunction());
                pst.setDouble(3, function.getSalary());
                pst.setString(4, function.getResponsability());
                pst.execute();

                pst.close();
                con.close();

                return true ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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

        return false ;
    }
    
    private synchronized boolean insertLogin(CollaboratorLogin login){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "INSERT INTO coll_login "
                        + "(id_coll, email,password) "
                        + "VALUES (?,?,?)" ;

                pst = con.prepareStatement(sql);
                pst.setInt(1, login.getId());
                pst.setString(2, login.getEmail());
                pst.setString(3, login.getPassword());
                pst.execute();

                pst.close();
                con.close();

                return true ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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

        return false ;
    }
    
    private synchronized void deleteFromID(int id){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "DELETE FROM collaborators_registers "
                        + "WHERE id=? " ;

                pst = con.prepareStatement(sql);
                pst.setInt(1, id);
                pst.execute();

                pst.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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

    }
    
    public synchronized boolean insertProduction(ITOrderBuyer order){
        if(!existRegisterProduction(order.getIdOrder())){
            Connection con = ConnectionDB_Collaborators.connect();
            PreparedStatement pst = null ;
          
            String sqlBuyer = "INSERT INTO coll_production (id_coll_buyer, id_order) "
                        + "VALUES(?,?) ";
            
            String sqlDelivery = "INSERT INTO coll_production (id_coll_deliveryman, id_order) "
                        + "VALUES(?,?) ";

            if(con != null){
                try {

                    pst = con.prepareStatement(order.getIdBuyer()>0?sqlBuyer:sqlDelivery);
                    pst.setInt(1, order.getIdBuyer()>0?order.getIdBuyer():order.getIdDeliveryMan());
                    pst.setString(2, order.getIdOrder());
                    pst.execute();

                    pst.close();
                    con.close();
                    return true ;
                } catch (SQLException ex) {
                    Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        pst.close();
                        con.close();
                        return false ;
                    } catch (SQLException ex1) {
                        Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex1);
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
        }
        else {
            return updateRegisterProduction(order);
        }
        return false ;
    }
    
    private boolean existRegisterProduction(String idOrder){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        if(con != null){
            try {
                String sql = "SELECT * FROM coll_production "
                        + "WHERE id_order=?";

                pst = con.prepareStatement(sql);
                pst.setString(1, idOrder);

                ResultSet rs = pst.executeQuery();
                boolean result = rs.next();
                con.close();
                pst.close();
                return result ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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

        return false ;
    }
    
    private synchronized boolean updateRegisterProduction(ITOrderBuyer order){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;
        
        if(con != null){
            try{
                String sql = "UPDATE coll_production SET "+(order.getIdBuyer()>0?"id_coll_buyer=? ":"id_coll_deliveryman=? ")
                        + "WHERE id_order=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, order.getIdBuyer()>0?order.getIdBuyer():order.getIdDeliveryMan());
                pst.setString(2, order.getIdOrder());
                pst.executeUpdate();

                pst.close();
                con.close();

                return true ;
            } catch (SQLException e){
                try {

                    pst.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
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

        return false ;
    }
    
    public synchronized List<Integer> getAllCollIDS(String companyFunction){
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;
        
        List<Integer> ids = new ArrayList<>();

        if(con != null){
            try{
                StringBuilder sql = new StringBuilder();
                sql.append("select colls.id from collaborators_registers colls ");
                sql.append("left join coll_function fun  ");
                sql.append("on fun.id_coll=colls.id ");
                sql.append("where colls.id > 0 and fun.company_function like '%");
                sql.append(companyFunction);
                sql.append("%'; ");

                pst = con.prepareStatement(sql.toString());
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    ids.add(rs.getInt("id"));
                }

                pst.close();
                con.close();
            } catch (SQLException e){
                try {
                    e.printStackTrace();
                    pst.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DaoCollaborators.class.getName()).log(Level.SEVERE, null, ex);
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

        return ids ;
    }
    
    public void delete(){
        
    }
    
    public void update(){
        
    }
    
    public void getAllOrdersFinalized(){
        
    }
    
    public void getProductionDay(){
        
    }
    
    public void getProductionWeek(){
        
    }
    
    public void getProductionMonth(){
        
    }

    // OK VERIFY
    public byte[] getPhoto(String imgPath) {
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null ;

        String sql = "SELECT img FROM coll_image WHERE path=? ";

        if(con != null){
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, imgPath);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){

                    byte[] img = rs.getBytes("img");
                    con.close();
                    pst.close();

                    return img ;
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
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null ;
    }

    // OK VERIFY
    public int insertPhoto(SendImage image) {
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null;

        String sql = "INSERT INTO coll_image (id, path, img) " +
                "VALUE (?,?,?) ";


        if(con != null){
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1,image.getIdUser());
                pst.setString(2, image.getImageName());
                pst.setBytes(3, image.getImageByte());
                pst.execute();

                pst.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                    return  - 1;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return 3 ;
    }

    public boolean checkExistCollaborator(int idBuyer, String email, String function) {
        Connection con = ConnectionDB_Collaborators.connect();
        PreparedStatement pst = null;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM coll_login log ");
        sb.append("LEFT JOIN coll_function func ");
        sb.append("ON func.id_coll=log.id_coll ");
        sb.append("WHERE log.id_coll=? AND log.email=? ");
        sb.append("AND func.company_function=? ");

        if(con != null){
            try {
                pst = con.prepareStatement(sb.toString());
                pst.setInt(1,idBuyer);
                pst.setString(2, email);
                pst.setString(3, function);
                ResultSet rs = pst.executeQuery();

                if(rs.next()){
                    con.close();
                    pst.close();

                    return true;
                }

                pst.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false ;
    }
}
