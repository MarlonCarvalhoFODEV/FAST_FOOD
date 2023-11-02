package br.com.comprazencasa.database;

import br.com.comprazencasa.itens.ITCampaing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoCampaing {

    public ArrayList<ITCampaing> getCampaingsSeasonal(String categorie, boolean seasonal) {
        ArrayList<ITCampaing> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM campaings ");
        sql.append("WHERE now() >= start_date AND now() <= stop_date ");
        sql.append("AND is_seasonal=? AND categorie=? ");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setBoolean(1, seasonal);
            pst.setString(2, categorie);

            ResultSet rs = pst.executeQuery();

            ITCampaing s;

            while (rs.next()) {
                s = new ITCampaing();
                s.setId(rs.getInt("id"));
                s.setDiscount(rs.getInt("discount"));
                s.setHeadline(rs.getString("headline"));
                s.setContent(rs.getString("content"));
                s.setCta(rs.getString("cta"));
                s.setPercentual(rs.getBoolean("is_percentual"));
                s.setShowForUsersRegistereds(rs.getBoolean("show_for_users_registereds"));
                s.setUrlImage(rs.getString("img_path"));

                itens.add(s);
            }

            con.close();
            pst.close();

            return itens;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                con.close();
                pst.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return itens ;
    }

    public ITCampaing getCampaing(int idCampaing) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            String sql = "SELECT * FROM campaings WHERE id=?  ";
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, idCampaing);
                ResultSet rs = pst.executeQuery();

                ITCampaing item = null;
                if(rs.next()){
                   item = new ITCampaing();
                   item.setId(rs.getInt("id"));
                   item.setDiscount(rs.getInt("discount"));
                   item.setPercentual(rs.getBoolean("is_percentual"));
                   item.setHeadline(rs.getString("headline"));
                   item.setContent(rs.getString("content"));
                   item.setCta(rs.getString("cta"));
                   item.setExibitionDate(rs.getString("exibition_date"));
                   item.setCreatedDate(rs.getString("created_date"));
                }

                con.close();
                pst.close();

                return item ;
            } catch (SQLException e) {
                e.printStackTrace();
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            } finally {
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null ;
    }

    public boolean updateCampaing(ITCampaing c) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE campaings SET discount=?, discount=?, is_percentual=?,");
            sql.append("headline=?, content=?, cta=?, exibition_date=? ");
            sql.append("WHERE id=? ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, c.getId());
                pst.setInt(2, c.getDiscount());
                pst.setBoolean(3, c.isPercentual());
                pst.setString(4, c.getHeadline());
                pst.setString(5,c.getContent());
                pst.setString(6,c.getCta());
                pst.setString(7, c.getExibitionDate());
                pst.setBoolean(8, c.isShowForUsersRegistereds());

                int linesAffected = pst.executeUpdate();


                con.close();
                pst.close();

                return linesAffected>0 ;
            } catch (SQLException e) {
                e.printStackTrace();
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            } finally {
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false ;
    }

    public boolean insertCampaing(ITCampaing c) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO campaings (discount, is_percentual, headline, content, cta, exibition_date, show_for_users_registereds) ");
            sql.append("VALUES (?,?,?,?,?,?,?) ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(2, c.getDiscount());
                pst.setBoolean(3, c.isPercentual());
                pst.setString(4, c.getHeadline());
                pst.setString(5,c.getContent());
                pst.setString(6,c.getCta());
                pst.setString(7, c.getExibitionDate());
                pst.setBoolean(8, c.isShowForUsersRegistereds());

                int linesAffected = pst.executeUpdate();

                con.close();
                pst.close();

                return linesAffected>0 ;
            } catch (SQLException e) {
                e.printStackTrace();
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
            } finally {
                if(con != null){
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false ;
    }

    public byte[] getImageFromCampaing(String imgPath) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT img FROM campaings ");
        sql.append("WHERE img_path=? ");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setString(1, imgPath);

            ResultSet rs = pst.executeQuery();

            byte[] img = null ;
            if (rs.next()){
                img = rs.getBytes("img");
            }

            con.close();
            pst.close();

            return img;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                con.close();
                pst.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null ;
    }
}
