package br.com.comprazencasa.database;

import br.com.comprazencasa.itens.ITListShopping;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.util.UtilPrices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoListShopping {

    private static final int COUNT_PRODUCTS = 9 ;

    public List<ITListShopping> getAllLists(ITListShopping item){

        List<ITListShopping> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        String sql = "SELECT * FROM shopping_lists WHERE id > ? ";

        if(con != null){
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, item.getId());

                ResultSet rs = pst.executeQuery();

                ITListShopping it ;
                while(rs.next()){
                    it = new ITListShopping();
                    it.setId(rs.getInt("id"));
                    it.setTitle(rs.getString("title"));
                    itens.add(it);
                }

                con.close();
                pst.close();

                return itens ;
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

        return null ;
    }

    public List<ITProduct> getProductsFromListShopping(ITListShopping item){

        List<ITProduct> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        String sql = "select * from shopping_lists_products sp " +
                "join czc_products p  " +
                "on sp.id_product = p.id " +
                "join rate_services rate " +
                "where rate.date=(select date from rate_services order by date DESC limit 1) " +
                "and p.status='active' and sp.id=? and p.id>? order by p.id ";

        if(con != null){
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, item.getId());
                pst.setInt(2,item.getIdLastProduct());

                ResultSet rs = pst.executeQuery();

                ITProduct p ;
                int count = 0 ;
                while(rs.next()){
                    p = new ITProduct();
                    p.setId(rs.getInt("p.id"));
                    p.setPathImg(rs.getString("p.path_img"));
                    p.setPercentageDiscount(rs.getInt("p.percentage_discount"));
                    p.setTitle(rs.getString("p.title"));
                    p.setInPrice(rs.getDouble("p.in_price"));
                    p.setWeight(rs.getBoolean("is_weight"));

                    double price = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"),rs.getDouble("rate.rate_per_credit"), rs.getDouble("p.per_price"));

                    p.setPerPrice(price);
                    p.setInstalments(rs.getInt("p.instalments"));
                    p.setCategorie(rs.getString("p.categorie"));

                    if(count > COUNT_PRODUCTS) {
                        itens.get(count-1).setHaveMore(true);
                        break;
                    }
                    else{
                        itens.add(p);
                    }
                    count++;
                }

                con.close();
                pst.close();

                return itens ;
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

        return null ;
    }
}
