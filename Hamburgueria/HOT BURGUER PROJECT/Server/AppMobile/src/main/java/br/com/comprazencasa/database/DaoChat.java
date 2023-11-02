/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;


/**
 *
 * @author Marlon
 */
public class DaoChat {
    /*public List<MessageMobile> getMessages(MessageMobile msg) {
        
        try {
            String sql = "SELECT * FROM messages WHERE id_ads=? "
                    + "and (id_sender=? or id_sender=?)"
                    + "and (id_destiny=? or id_destiny=?)";
          
            Connection con = ConnectionDB.connect();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, msg.getIdProduct());
            pst.setInt(2, msg.getIdSender());
            pst.setInt(3, msg.getIdDestiny());
            pst.setInt(4, msg.getIdDestiny());
            pst.setInt(5, msg.getIdSender());
            ResultSet rs = pst.executeQuery();
            
            UserMessage m ;
            List<UserMessage> msgs = new ArrayList<>();
            while(rs.next()){
               
                m = new UserMessage();
                m.setIdProduct(rs.getInt("id_ads"));
                m.setIdSender(rs.getInt("id_sender"));
                m.setIdDestiny(rs.getInt("id_destiny"));
                m.setOwner(rs.getBoolean("owner_ads"));
                m.setIdAnoouncer(rs.getInt("id_announcer"));
                m.setPathPhotoUser(rs.getString("path_img_sender"));
                m.setPathPhotoAnnouncer(rs.getString("path_img_destiny"));
                m.setMessage(rs.getString("message"));
                m.setHour(rs.getString("hour"));
                m.setDate(rs.getString("date"));
                
                msgs.add(m);
            }
            return msgs ;
        } catch (SQLException ex) {
            Logger.getLogger(DAO_Chat.class.getName()).log(Level.SEVERE, null, ex);
            return null ;
        }
    }

    public void saveMessageDB(MessageMobile msg) {
        
       if(msg.getIdSender() > 0){
            try {
                String sql = "SELECT * FROM ads_sale WHERE id_ads=?";
                Connection con = ConnectionDB.connect();
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, msg.getIdProduct());

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    insertMessage(msg);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DAO_Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean insertMessage(MessageMobile msg) {
        
        try {
            String sql = "INSERT INTO messages (id_sender, id_destiny, path_img_sender,"
                    + " path_img_destiny, message, id_ads, owner_ads,hour, date,"
                    + "path_img_ads, title_ads, id_announcer, sender_name, destiny_name) \n" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Connection con = ConnectionDB.connect();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, msg.getIdSender());
            pst.setInt(2, msg.getIdDestiny());
            pst.setString(3, msg.getPathPhotoUser());
            pst.setString(4, msg.getPathPhotoAnnouncer());
            pst.setString(5, msg.getMessage());
            pst.setInt(6, msg.getIdProduct());
            pst.setBoolean(7, msg.isOwner());
            pst.setString(8, msg.getHour());
            pst.setString(9, msg.getDate());
            pst.setString(10, msg.getPathPhotoAds());
            pst.setString(11,msg.getProductName());
            pst.setInt(12, msg.getIdAnoouncer());
            pst.setString(13, msg.getUserName());
            pst.setString(14, msg.getDestinyName());
            pst.execute();
            return true ;
        } catch (SQLException ex) {
            Logger.getLogger(DAO_Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false ;
    }
    
    public List<MessageMobile> getMyAllMessageProducts(MessageMobile msg) {
        try {
            String sql = "SELECT * FROM messages WHERE (id_sender=? or id_destiny=?) "
                    + "ORDER by date desc";
          
            Connection con = ConnectionDB.connect();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, msg.getIdAnoouncer());
            pst.setInt(2, msg.getIdAnoouncer());
            ResultSet rs = pst.executeQuery();

            MessageMobile m ;
            List<MessageMobile> list = new ArrayList<>();
            while(rs.next()){
               
                m = new MessageMobile();
                m.setIdProduct(rs.getInt("id_ads"));
                m.setIdSender(rs.getInt("id_sender"));
                m.setIdDestiny(rs.getInt("id_destiny"));
                m.setIdAnoouncer(rs.getInt("id_announcer"));
                m.setOwner(rs.getBoolean("owner_ads"));
                m.setPathPhotoUser(rs.getString("path_img_sender"));
                m.setPathPhotoAnnouncer(rs.getString("path_img_destiny"));
                m.setMessage(rs.getString("message"));
                m.setUserName(rs.getString("sender_name"));
                m.setDestinyName(rs.getString("destiny_name"));
                m.setPathPhotoAds(rs.getString("path_img_ads"));
                m.setProductName(rs.getString("title_ads"));
                m.setHour(rs.getString("hour"));
                m.setDate(rs.getString("date"));

                System.out.println("PATH SENDER: "+m.getPathPhotoUser());
                
               list.add(m);
            }
            return list ;
        } catch (SQLException ex) {
            Logger.getLogger(DaoChat.class.getName()).log(Level.SEVERE, null, ex);
            return null ;
        }
    }*/
}
