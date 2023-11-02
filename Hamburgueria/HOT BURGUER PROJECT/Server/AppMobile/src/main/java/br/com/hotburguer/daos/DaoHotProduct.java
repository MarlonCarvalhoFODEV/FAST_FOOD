package br.com.hotburguer.daos;

import apis.APIFinance;
import br.com.comprazencasa.database.ConnectionDB_APP;
import br.com.comprazencasa.database.DaoAddress;
import br.com.hotburguer.itens.HotProduct;
import br.com.hotburguer.itens.HotRatingProduct;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoHotProduct {

    public ArrayList<HotProduct> getProducts(int idUser, String idDevice, String categorie, int idProduct){
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<HotProduct> itens = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT *,config.id,CAST((cost_price+config.fee_credcard_processing ");
                sqlConstruct.append("+config.fee_credcard_antifraud)/((100-(config.fee_credcard+i_add))/100) AS DECIMAL(12,2)) price, ");
                sqlConstruct.append("(SELECT liked FROM hot_products_likeds WHERE id_user=users.id AND id_product=prods.id) liked ");
                sqlConstruct.append("FROM hot_products prods LEFT JOIN hot_configurations config ");
                sqlConstruct.append("LEFT JOIN hot_users users ON users.id > 0 ");
                sqlConstruct.append("LEFT JOIN hot_users_ips ips ON ips.id_user = users.id ");
                sqlConstruct.append("LEFT JOIN hot_users_quarantine qt ON qt.public_ip = ips.public_ip ");
                sqlConstruct.append("ON config.id = 1 WHERE prods.id > 0 AND categorie=? ");
                sqlConstruct.append("AND users.id=? AND users.device_id=? AND disable=0 AND quantity_in_stock > 0 ");
                sqlConstruct.append("AND qt.public_ip IS NULL; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, categorie);
                pst.setInt(2, idUser);
                pst.setString(3, idDevice);

                ResultSet rs = pst.executeQuery();

                HotProduct hot ;
                while (rs.next()) {
                    hot = new HotProduct();
                    hot.setId(rs.getInt("id"));
                    hot.setCostPrice(rs.getDouble("cost_price"));
                    hot.setCategorie(rs.getString("categorie"));
                    hot.setDescription(rs.getString("description"));
                    //hot.setRecipe(rs.getString("recipe"));
                    hot.setFeeDeliveryman(rs.getDouble("fee_deliveryman"));
                    hot.setTitle(rs.getString("title"));
                    hot.setDisabled(rs.getBoolean("disable"));
                    hot.setPerPrice(APIFinance.treatDecimal(rs.getDouble("price")));
                    hot.setTangible(rs.getBoolean("tangible"));
                    hot.setLiked(rs.getBoolean("liked"));
                    hot.setCountLikes(rs.getInt("likeds"));
                    hot.setCountFeedbacks(rs.getInt("feedbacks"));
                    hot.setCombo(rs.getBoolean("is_combo"));

                    itens.add(hot);
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

        return itens ;
    }

    public boolean evaluationProduct(HotRatingProduct hotRating){
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("INSERT INTO hot_products_evaluations (id_user, id_product, id_order, rating, feedback) VALUES (?,?,?,?,?); ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, hotRating.getIdUser());
                pst.setInt(2, hotRating.getIdProduct());
                pst.setInt(3, hotRating.getIdOrder());
                pst.setFloat(4, hotRating.getRating());
                pst.setString(5, hotRating.getFeedBack());

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

        return false ;
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

                    return img ;
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

    public boolean likeProduct(int idUser, int idProd, boolean status) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT IGNORE INTO hot_products_likeds (id_user, id_product, liked) ");
        sql.append("VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE liked=NOT liked ; ");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, idUser);
            pst.setInt(2, idProd);
            pst.setBoolean(3, status);
            int linesAffecteds = pst.executeUpdate();

            if (linesAffecteds > 0) {
                con.close();
                pst.close();


                return true;
            }
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
        return false ;
    }

    public JSONArray getFeedbacks(int idUser, String idDevice, int idProd) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        JSONArray itens = new JSONArray();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT users.id,users.photo, users.name, evals.feedback FROM hot_products_evaluations evals ");
                sqlConstruct.append("LEFT JOIN hot_users users ON users.id>0 ");
                sqlConstruct.append("WHERE users.id=? AND users.device_id=? AND evals.id_product=? AND evals.id_user=users.id; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);
                pst.setInt(3, idProd);

                ResultSet rs = pst.executeQuery();

                JSONObject feed ;
                while (rs.next()) {
                    feed = new JSONObject();
                    feed.put("id_user", rs.getInt("users.id"));
                    feed.put("name",rs.getString("users.name"));
                    feed.put("feedback",rs.getString("evals.feedback"));
                    feed.put("photo",rs.getString("users.photo"));
                    itens.put(feed);
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
        return itens ;
    }
}
