package br.com.comprazencasa.database;

import br.com.comprazencasa.itens.ITCoupon;
import br.com.comprazencasa.util.DataPadraoUtil;
import br.com.comprazencasa.util.WebResponses;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DaoCoupons {

    public List<ITCoupon> getMyCoupons(ITCoupon coupon) {

        List<ITCoupon> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dis.id,dis.is_percentual,dis.rule, dis.take, dis.pay,dis.title, dis.minimum_purchase, dis.categorie, dis.description, ");
        sql.append(" dis.path_img, dis.id_product,dis.id_user,dis.discount, discount_limit,dis.description,datediff(dis.date_final, curdate()) ");
        sql.append("expiration_date,dis.date_final, ");
        sql.append("prod.id,prod.title,prod.path_img FROM app_discount_coupons dis ");
        sql.append("left join czc_products prod ");
        sql.append("on prod.id = dis.id_product " );
        sql.append("where dis.id>? and (datediff(dis.date_final, curdate())) > 0 ");
        sql.append("and dis.id_user=? ");
        sql.append("limit 11 ;");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, coupon.getId());
            pst.setInt(2, coupon.getIdUser());

            ResultSet rs = pst.executeQuery();

            ITCoupon item;

            while (rs.next()) {
                item = new ITCoupon();
                item.setId(rs.getInt("dis.id"));
                item.setIdUser(rs.getInt("dis.id_user"));
                item.setIdProduct(rs.getInt("prod.id"));
                item.setDescription(rs.getString("prod.title"));
                item.setTitle(rs.getString("dis.title"));
                item.setPathImgProduct(rs.getString("prod.path_img"));
                item.setPercentual(rs.getBoolean("dis.is_percentual"));
                item.setDiscount(rs.getDouble("dis.discount"));
                item.setCategorie(rs.getString("categorie"));
                item.setDiscountLimit(rs.getDouble("dis.discount_limit"));
                item.setCountDayForExpiration(rs.getInt("expiration_date"));
                item.setMinimumPurchase(rs.getDouble("dis.minimum_purchase"));
                item.setRule(rs.getString("dis.rule"));
                item.setTake(rs.getInt("dis.take"));
                item.setPay(rs.getInt("dis.pay"));
                item.setPathImgPromo(rs.getString("dis.path_img"));

                if (item.getDescription() == null ||
                        item.getDescription().isEmpty()) {
                    item.setDescription(rs.getString("dis.description"));
                }

                if (itens.size() < 11) {
                    itens.add(item);
                } else {
                    itens.get(itens.size() - 1).setHaveMore(true);
                }
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

        return itens;
    }

    public Set<ITCoupon> getCouponsFromListID(String idUser, Set<Integer> listId) {

        Set<ITCoupon> coupons = new HashSet<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dis.id, dis.categorie, dis.is_percentual, dis.take, dis.pay,dis.title,");
        sql.append("dis.minimum_purchase, dis.path_img, dis.id_product,dis.id_user,dis.discount, dis.discount_limit");
        sql.append(",dis.description,datediff(dis.date_final, curdate()) expiration_date,dis.date_final, ");
        sql.append("prod.id,prod.title,prod.path_img FROm app_discount_coupons dis ");
        sql.append("left join czc_products prod ");
        sql.append("on prod.id = dis.id_product ");
        sql.append("where dis.id_user=? and (datediff(dis.date_final, curdate())) > 0 ");
        sql.append("limit 11 ;");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setString(1, idUser);
            ResultSet rs = pst.executeQuery();

            ITCoupon item;
            while (rs.next()) {
                if (listId.contains(rs.getInt("id"))) {
                    item = new ITCoupon();
                    item.setId(rs.getInt("dis.id"));
                    item.setIdUser(rs.getInt("dis.id_user"));
                    item.setIdProduct(rs.getInt("prod.id"));
                    item.setDescription(rs.getString("dis.description"));
                    item.setTitle(rs.getString("prod.title"));
                    item.setPathImgProduct(rs.getString("prod.path_img"));
                    item.setPercentual(rs.getBoolean("dis.is_percentual"));
                    item.setDiscount(rs.getDouble("dis.discount"));
                    item.setCategorie(rs.getString("categorie"));
                    item.setDiscountLimit(rs.getDouble("dis.discount_limit"));
                    item.setCountDayForExpiration(rs.getInt("expiration_date"));
                    item.setMinimumPurchase(rs.getDouble("dis.minimum_purchase"));
                    item.setTake(rs.getInt("dis.take"));
                    item.setPay(rs.getInt("dis.pay"));
                    item.setPathImgPromo(rs.getString("dis.path_img"));

                    if (item.getDescription() == null && item.getDescription().isEmpty()
                            || item.getDescription().length() < 5) {
                        item.setDescription(item.getTitle());
                    }

                    coupons.add(item);
                    //deleteCouponFromID(item.getId(), idUser);
                }
            }

            con.close();
            pst.close();
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

        return coupons;
    }

    private synchronized boolean deleteCouponFromID(int id, String idUser) {

        Connection con = ConnectionDB_APP_ADMIN.connect();
        PreparedStatement pst = null;

        boolean sucess = false;
        if(con != null){
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM app_discount_coupons WHERE id=? AND id_user=? ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, id);
                pst.setString(2, idUser);
                sucess = pst.execute();

                con.close();
                pst.close();
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
        }

        return sucess;
    }

    public synchronized boolean deleteAllCouponFromListID(Set<Integer> ids, String idUser) {

        Connection con = ConnectionDB_APP_ADMIN.connect();
        PreparedStatement pst = null;

        boolean sucess = false;
        if(con != null){
            StringBuilder sql = new StringBuilder();
            sql.append("delete from app_discount_coupons where  id in (");

            for(int i=0; i < ids.size(); i++){

                if(i<ids.size()-1){
                    sql.append("?,");
                } else {
                    sql.append("?) ");
                }
            }

            sql.append("and id_user =");
            sql.append("'");
            sql.append(idUser);
            sql.append("'");


            try {
                pst = con.prepareStatement(sql.toString());

                int param = 1;
                for(int id : ids){
                    pst.setInt(param, id);
                    param++;
                }


                sucess = pst.execute();

                con.close();
                pst.close();
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
        }

        return sucess;
    }

    /*public void insertCouponFromInterestUser(int idUser,String cpf){
       DaoOrder dao = new DaoOrder();

       Connection con = ConnectionDB_APP.connect();
       PreparedStatement pst = null ;

       Map<Integer,ITProduct> products = dao.getAllProductsFromOrdersClient(idUser);

       List<ITCoupon> coupons = new ArrayList<>();
       List<ITProduct> interests = new ArrayList<>();

       ITCoupon coupon ;
       for (ITProduct p : products.values()){
           if(p.getCount() >= 2){
               interests.add(p);

               coupon = new ITCoupon();
               coupon.setIdProduct(p.getId());
               coupon.setIdUser(cpf);
               coupon.setDiscount();
               coupon.setDiscountLimit();
               coupon.setMinimumPurchase();
               coupon.setPercentual();
               coupon.setPathImgPromo();
               coupon.setCategorie();
               coupon.setTake();
               coupon.setPay();
               coupon.setPromotionName();
               coupon.setProductName();
               coupon.setRule();
           }
       }

       if(con != null){
           try {
               String sql = "INSERT INTO app_discount_coupons (id_product, id_user, discount, discount_limit, minimum_purchase,is_percentual, path_img, categorie, take, pay, info, rule) " +
                       "VALUES (?,?,?,?,?,?,?,?,?,?,?,?) " ;

               pst = con.prepareStatement(sql);
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }

        // saber quais produtos o usuario se interessa
       // descontos com base na quantidade de produtos da mesma unidade
       // criar descontos com 7% dos produtos de interresse
    }*/

    public void insert3CouponsForUsersRegisters(int idUser) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                ITCoupon c1 = new ITCoupon();
                c1.setIdProduct(0);
                c1.setIdUser(idUser);
                c1.setDiscount(14);
                c1.setDiscountLimit(5);
                c1.setPercentual(true);
                c1.setPathImgPromo(null);
                c1.setCategorie(null);
                c1.setTake(0);
                c1.setPay(0);
                c1.setCategorie("Hortifruti");
                c1.setTitle("14% de Desconto");
                c1.setDescription("Em Frutas, Legumes e Verduras");
                c1.setRule("Use esse cupom de 14% de desconto para produtos da categoria Hortifruti. Limite máximo de desconto de R$ 5,00.");
                c1.setExpirationDate(DataPadraoUtil.getDateMoreDayADD(35));

                ITCoupon c2 = new ITCoupon();
                c2.setIdProduct(0);
                c2.setIdUser(idUser);
                c2.setDiscount(16);
                c2.setDiscountLimit(5);
                c2.setMinimumPurchase(80);
                c2.setPercentual(true);
                c2.setPathImgPromo(null);
                c2.setCategorie(null);
                c2.setTake(0);
                c2.setPay(0);
                c2.setCategorie("Cookies & Sugarplum");
                c2.setTitle("16% de Desconto");
                c2.setDescription("Biscoitos e Chocolates");
                c2.setRule("Use esse cupom de 16% de desconto para produtos da categoria Biscoitos & Bombon's. Compra mínima R$ " + c2.getMinimumPurchase()
                        + " , limite máximo de desconto de R$ 5,00.");
                c2.setExpirationDate(DataPadraoUtil.getDateMoreDayADD(35));

                ITCoupon c3 = new ITCoupon();
                c3.setIdProduct(0);
                c3.setIdUser(idUser);
                c3.setDiscount(20);
                c3.setDiscountLimit(5);
                c3.setMinimumPurchase(135);
                c3.setPercentual(true);
                c3.setPathImgPromo(null);
                c3.setCategorie(null);
                c3.setTake(0);
                c3.setPay(0);
                c3.setCategorie("Morning");
                c3.setTitle("20% de Desconto");
                c3.setDescription("Produtos da Categoria Matinais");
                c3.setRule("Use esse cupom de 20% de desconto para produtos da categoria Matinais compra mínima R$ " + c3.getMinimumPurchase()
                        + " , limite máximo de desconto de R$ 5,00.");
                c3.setExpirationDate(DataPadraoUtil.getDateMoreDayADD(35));

                List<ITCoupon> coupons = new ArrayList<>();

                coupons.add(c1);
                coupons.add(c2);
                coupons.add(c3);

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO app_discount_coupons (id_product, id_user, discount, discount_limit, minimum_purchase,is_percentual");
                sql.append(", path_img,title,description, rule, categorie, take, pay, date_final) ");
                sql.append("VALUES ");

                StringBuilder sqlConstruct = new StringBuilder(sql);
                sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?),");
                sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?),");
                sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
                //sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?),");
                //sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?),");
                //sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?),");
                //sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?),");
                //sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?),");
                //sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?),");
                //sqlConstruct.append("(?,?,?,?,?,?,?,?,?,?,?,?,?);");

                pst = con.prepareStatement(sqlConstruct.toString());

                int counter = 1;
                for (ITCoupon c : coupons) {
                    pst.setInt(counter, c.getIdProduct());
                    counter++;
                    pst.setInt(counter, c.getIdUser());
                    counter++;
                    pst.setDouble(counter, c.getDiscount());
                    counter++;
                    pst.setDouble(counter, c.getDiscountLimit());
                    counter++;
                    pst.setDouble(counter, c.getMinimumPurchase());
                    counter++;
                    pst.setBoolean(counter, c.isPercentual());
                    counter++;

                    if (c.getPathImgProduct() != null) {
                        pst.setString(counter, c.getPathImgProduct());
                    } else {
                        pst.setString(counter, c.getPathImgPromo());
                    }

                    counter++;
                    pst.setString(counter, c.getTitle());
                    counter++;
                    pst.setString(counter, c.getDescription());
                    counter++;
                    pst.setString(counter, c.getRule());
                    counter++;
                    pst.setString(counter, c.getCategorie());
                    counter++;
                    pst.setInt(counter, c.getTake());
                    counter++;
                    pst.setInt(counter, c.getPay());
                    counter++;
                    pst.setString(counter, c.getExpirationDate());
                    counter++;
                }

                try{
                    pst.execute();
                } catch (SQLIntegrityConstraintViolationException ex){
                    ex.printStackTrace();
                }

                pst.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    pst.close();
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }
}
