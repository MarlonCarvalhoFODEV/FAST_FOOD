package br.com.comprazencasa.database;

import apis.date.HoursManager;
import apis.pagarme.userbuyer.UserBuyer;
import apis.promotion.PromotionAPI;
import br.com.comprazencasa.geo.Street;
import br.com.comprazencasa.itens.*;
import br.com.comprazencasa.market.Pedido;
import br.com.comprazencasa.user.Endereco;
import br.com.comprazencasa.util.SearchProduct;
import br.com.comprazencasa.util.UtilPrices;
import com.google.common.collect.Maps;
import org.springframework.util.Base64Utils;

import javax.xml.transform.Result;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoStores {

    private static final int LIMIT_LOAD_PRODUCTS = 16;
    private static final int LIMIT_LOAD_STORES = 16;

    public ArrayList<String> getStoreCategories(int idStore, String initialsState) {
        ArrayList<String> categories = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT catg_p.br FROM stores_products prod_store ");
        sql.append("LEFT JOIN app_categories_products catg_p ");
        sql.append("ON catg_p.id=prod_store.id_categorie ");
        sql.append("WHERE prod_store.id_store=? group by categorie;");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, idStore);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString(initialsState.toLowerCase()));
            }

            con.close();
            pst.close();

            return categories;
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
        return categories;
    }

    public ArrayList<ITCampaing> getStoreCampaings(ITStore store, boolean isVisitor) {
        ArrayList<ITCampaing> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM stores_campaings WHERE id_store=? AND now() >= start_date AND now() <= stop_date ;");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, store.getId());

            ResultSet rs = pst.executeQuery();

            ITCampaing c;

            int counter = 0;
            while (rs.next()) {

                c = new ITCampaing();
                c.setId(rs.getInt("id"));
                c.setIdStore(rs.getInt("id_store"));
                c.setIdProduct(rs.getInt("id_product"));
                c.setType(rs.getInt("type"));
                c.setHeadline(rs.getString("headline"));
                c.setContent(rs.getString("content"));
                c.setCta(rs.getString("cta"));
                c.setUrlImage(rs.getString("img_path"));
                c.setStartDate(rs.getString("start_date"));
                c.setStopDate(rs.getString("stop_date"));
                c.setCreatedDate(rs.getString("created_date"));

                itens.add(c);
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
        return null;
    }

    public ArrayList<ITStore> getStoreFamous(ITStore store, boolean isVisitor) {
        ArrayList<ITStore> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT store.id, store.type, store.name, categb.b_categorie, store_config.time_min_delivery, ");
        sql.append("store_config.time_max_delivery, store_config.max_installments_without_fee, store_config.fee_delivery, store_config.max_installments, ");
        sql.append("store_config.fee_installments, store_config.minimum_purchase_installments ,address.country, address.state, address.city, address.neighborhood, address.latitude, address.longitude, ");
        sql.append("Geo(acc.longitude,acc.latitude,address.longitude,address.latitude) distance_in_km, ");
        sql.append("AverageRating(eval.s1, eval.s2, eval.s3, eval.s4, eval.s5) avg_rating, ");
        sql.append("(SELECT favorited FROM stores_favorited WHERE id_account=acc.id AND id_store=store.id) favorited ");
        sql.append("FROM stores store ");
        sql.append("LEFT JOIN stores_configuration store_config ");
        sql.append("ON store_config.id_store = store.id ");
        sql.append("LEFT JOIN stores_address address ");
        sql.append("ON address.id_store = store.id ");
        sql.append("LEFT JOIN stores_evaluations eval ");
        sql.append("ON eval.id_store = store.id ");
        sql.append("LEFT JOIN app_categories_business categb ");
        sql.append("ON categb.id=store.id_categorie ");
        sql.append("LEFT JOIN accounts acc ");
        sql.append("ON acc.id >0 ");
        sql.append("WHERE store.id > ? AND categb.b_categorie=? ");

        if (!isVisitor)
            sql.append("AND acc.id=? ");

        sql.append("GROUP BY id ORDER BY id LIMIT ? ");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, store.getId());
            pst.setString(2, store.getCategorie());
            pst.setInt(!isVisitor ? 4 : 3, LIMIT_LOAD_STORES);

            if (!isVisitor) {
                pst.setInt(3, store.getIdRequisitor());
            }
            //pst.setString(3, web.getIdUserS());

            ResultSet rs = pst.executeQuery();

            ITStore s;

            int counter = 0;
            while (rs.next()) {
                s = new ITStore();
                s.setId(rs.getInt("store.id"));
                s.setType(rs.getInt("store.type"));
                s.setStoreName(rs.getString("store.name"));
                s.setCategorie(rs.getString("categb.b_categorie"));
                s.getStoreConfiguration().setAverageRating(rs.getDouble("avg_rating"));
                s.getStoreConfiguration().setTimeMinDelivery(rs.getInt("store_config.time_min_delivery"));
                s.getStoreConfiguration().setTimeMaxDelivery(rs.getInt("store_config.time_max_delivery"));
                s.getStoreConfiguration().setFeeDelivery(rs.getDouble("store_config.fee_delivery"));
                s.setFavorited(rs.getBoolean("favorited"));
                s.getStoreConfiguration().getPaymentConfiguration().setMaxInstallments(rs.getInt("store_config.max_installments"));
                s.getStoreConfiguration().getPaymentConfiguration().setMaxWithoutFEE(rs.getInt("store_config.max_installments_without_fee"));
                s.getStoreConfiguration().getPaymentConfiguration().setFeeInstallments(rs.getDouble("store_config.fee_installments"));
                s.getStoreConfiguration().getPaymentConfiguration().setMinimumPurchaseInstallments(rs.getInt("minimum_purchase_installments"));

                if (!isVisitor)
                    s.getStoreConfiguration().setDistanceInKM(rs.getDouble("distance_in_km"));

                counter++;

                if (counter == LIMIT_LOAD_STORES) {
                    itens.get(LIMIT_LOAD_STORES - 1).setHaveMore(true);
                } else {
                    itens.add(s);
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

    public ArrayList<ITStore> getStoreMoreSales(ITStore store, boolean isUser) {
        ArrayList<ITStore> itens = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT store.id, store.type, store.name, categb.b_categorie, store_config.time_min_delivery, ");
        sql.append("store_config.time_max_delivery, store_config.max_installments_without_fee, store_config.fee_delivery, store_config.max_installments, ");
        sql.append("store_config.fee_installments , store_config.minimum_purchase_installments ,address.country, address.state, address.city, address.neighborhood, address.latitude, address.longitude, ");
        sql.append("Geo(acc.longitude,acc.latitude,address.longitude,address.latitude) distance_in_km, ");
        sql.append("AverageRating(eval.s1, eval.s2, eval.s3, eval.s4, eval.s5) avg_rating, ");
        sql.append("(SELECT favorited FROM stores_favorited WHERE id_account=acc.id AND id_store=store.id) favorited ");
        sql.append("FROM stores store ");
        sql.append("LEFT JOIN stores_configuration store_config ");
        sql.append("ON store_config.id_store = store.id ");
        sql.append("LEFT JOIN stores_address address ");
        sql.append("ON address.id_store = store.id ");
        sql.append("LEFT JOIN stores_evaluations eval ");
        sql.append("ON eval.id_store = store.id ");
        sql.append("LEFT JOIN app_categories_business categb ");
        sql.append("ON categb.id=store.id_categorie ");
        sql.append("LEFT JOIN accounts acc ");
        sql.append("ON acc.id >0 ");
        sql.append("WHERE store.id > ? AND categb.b_categorie=? ");

        if (isUser)
            sql.append("AND acc.id=? ");

        sql.append("GROUP BY id ORDER BY id LIMIT ? ");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, store.getId());
            pst.setString(2, store.getCategorie());
            pst.setInt(isUser ? 4 : 3, LIMIT_LOAD_STORES);

            if (isUser) {
                pst.setInt(3, store.getIdRequisitor());
            }

            ResultSet rs = pst.executeQuery();

            ITStore s;

            int counter = 0;
            while (rs.next()) {
                s = new ITStore();
                s.setId(rs.getInt("store.id"));
                s.setType(rs.getInt("store.type"));
                s.setStoreName(rs.getString("store.name"));
                s.setCategorie(rs.getString("categb.b_categorie"));
                s.getStoreConfiguration().setAverageRating(rs.getDouble("avg_rating"));
                s.getStoreConfiguration().setTimeMinDelivery(rs.getInt("store_config.time_min_delivery"));
                s.getStoreConfiguration().setTimeMaxDelivery(rs.getInt("store_config.time_max_delivery"));
                s.getStoreConfiguration().setFeeDelivery(rs.getDouble("store_config.fee_delivery"));
                s.setFavorited(rs.getBoolean("favorited"));
                s.getStoreConfiguration().getPaymentConfiguration().setMaxInstallments(rs.getInt("store_config.max_installments"));
                s.getStoreConfiguration().getPaymentConfiguration().setMaxWithoutFEE(rs.getInt("store_config.max_installments_without_fee"));
                s.getStoreConfiguration().getPaymentConfiguration().setFeeInstallments(rs.getDouble("store_config.fee_installments"));

                if (isUser)
                    s.getStoreConfiguration().setDistanceInKM(rs.getDouble("distance_in_km"));

                counter++;

                if (counter == LIMIT_LOAD_STORES) {
                    itens.get(LIMIT_LOAD_STORES - 1).setHaveMore(true);
                } else {
                    itens.add(s);
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
        return null;
    }

    public ArrayList<ITProduct> getProducts(ITProduct it) {
        ArrayList<ITProduct> products = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                boolean isSearch = it != null && it.getTitle() != null && it.getTitle().contains("%");

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT prod_store.id, prod.title, prod.path_img, prod.categorie, prod.sub_categorie, prod.is_tangible tangible_default, prod.is_weight weight_default, ");
                sql.append("flav.id, flav.flavor, prod_store.id_store, prod_store.id_product, prod_store.in_price,  CAST((prod_store.per_price/((100-conf.apply_fee_all_products)/100)) AS DECIMAL(12,2)) per_price, prod_store.is_weight, prod_store.is_tangible, prod_store.is_inactive, ");
                sql.append("prod_store.proibited_minors, prod_store.instalments, prod_store.percentage_discount ");
                sql.append("FROM stores_products prod_store ");
                sql.append("LEFT JOIN czc_products prod ");
                sql.append("ON prod_store.id_product = prod.id ");
                sql.append("LEFT JOIN stores_products_flavors flav ");
                sql.append("ON flav.id=prod.id_flavor ");
                sql.append("LEFT JOIN stores_configuration conf ");
                sql.append("ON conf.id_store=prod_store.id_store ");
                sql.append("WHERE prod_store.id > ? ");
                sql.append("AND prod_store.id_store=? ");

                if (isSearch)
                    sql.append("AND title like ? ");
                else
                    sql.append("AND prod.categorie=?");

                sql.append("ORDER BY prod_store.id ASC limit ");
                sql.append(LIMIT_LOAD_PRODUCTS);

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, it.getId());
                pst.setInt(2, it.getIdStore());


                if (isSearch)
                    pst.setString(3, it.getTitle());
                else
                    pst.setString(3, it.getCategorie());

                ResultSet rs = pst.executeQuery();

                ITProduct p;
                while (rs.next()) {
                    p = new ITProduct();
                    p.setId(rs.getInt("prod_store.id"));
                    p.setIdProduct(rs.getInt("prod_store.id_product"));
                    p.setIdStore(rs.getInt("prod_store.id_store"));
                    p.setPathImg(rs.getString("prod.path_img"));
                    p.setPercentageDiscount(rs.getInt("prod_store.percentage_discount"));
                    p.setTitle(rs.getString("prod.title"));
                    p.setInPrice(rs.getDouble("prod_store.in_price"));
                    p.setPerPrice(rs.getDouble("per_price"));
                    p.setInstalments(rs.getInt("prod_store.instalments"));
                    p.setCategorie(rs.getString("prod.categorie"));
                    p.setSubCategorie(rs.getString("prod.sub_categorie"));
                    p.setWeight(rs.getBoolean("prod_store.is_weight"));
                    p.setFlavors(rs.getInt("flav.id") > 0 ? true : false);
                    p.setTangible(rs.getBoolean("prod_store.is_tangible"));

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));

                    p.setFlavor(f);

                    if (products.size() < LIMIT_LOAD_PRODUCTS - 1) {
                        products.add(p);
                    } else {
                        products.get(products.size() - 1).setHaveMore(true);
                        break;
                    }
                }

                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return products;
    }

    public ArrayList<ITProduct> getProducts1(ITProduct it) {
        ArrayList<ITProduct> products = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                boolean isSearch = it != null && it.getTitle() != null && it.getTitle().contains("%");

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT prod_store.id, prod_store.id_store, prod_store.img, prod_store.title, catg_p.categorie,subcatg_p.subcategorie, prod_store.is_tangible tangible_default, prod_store.is_weight weight_default, ");
                sql.append("flav.id, flav.flavor, prod_store.id, prod_store.in_price,  CAST((prod_store.per_price/((100-conf.apply_fee_all_products)/100)) AS DECIMAL(12,2)) per_price, prod_store.is_weight, prod_store.is_tangible, prod_store.enabled, ");
                sql.append("prod_store.prohibited_for_minors, prod_store.instalments, prod_store.percentage_discount ");
                sql.append("FROM stores_products prod_store LEFT JOIN stores_products_flavors flav ");
                sql.append("ON flav.id_product=prod_store.id LEFT JOIN stores_configuration conf ");
                sql.append("ON conf.id_store=prod_store.id_store LEFT JOIN app_categories_products catg_p ");
                sql.append("ON catg_p.id=prod_store.id_categorie LEFT JOIN app_subcategories_products subcatg_p ");
                sql.append("ON subcatg_p.id_categorie=catg_p.id WHERE prod_store.id > ? ");
                sql.append("AND prod_store.id_store=? ");

                if (isSearch)
                    sql.append("AND title like ? ");
                else
                    sql.append("AND catg_p.br=? AND subcatg_p.br like ? ");

                sql.append("GROUP BY title ORDER BY prod_store.id ASC limit ");
                sql.append(LIMIT_LOAD_PRODUCTS);

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, it.getId());
                pst.setInt(2, it.getIdStore());


                if (isSearch)
                    pst.setString(3, it.getTitle());
                else {
                    String sCateg = it.getSubCategorie();
                    sCateg = "%"+(sCateg!=null?sCateg:"")+"%";
                    pst.setString(3, it.getCategorie());
                    pst.setString(4, sCateg);
                }

                ResultSet rs = pst.executeQuery();

                ITProduct p;
                while (rs.next()) {
                    p = new ITProduct();
                    p.setId(rs.getInt("prod_store.id"));
                    //p.setIdProduct(rs.getInt("prod_store.id_product"));
                    p.setIdStore(rs.getInt("prod_store.id_store"));
                   // p.setPathImg(rs.getString("prod.path_img"));
                    p.setPercentageDiscount(rs.getInt("prod_store.percentage_discount"));
                    p.setTitle(rs.getString("prod_store.title"));
                    p.setInPrice(rs.getDouble("prod_store.in_price"));
                    p.setPerPrice(rs.getDouble("per_price"));
                    p.setInstalments(rs.getInt("prod_store.instalments"));
                    p.setCategorie(rs.getString("categorie"));
                    p.setSubCategorie(rs.getString("subcategorie"));
                    p.setWeight(rs.getBoolean("prod_store.is_weight"));
                    p.setFlavors(rs.getInt("flav.id") > 0 ? true : false);
                    p.setTangible(rs.getBoolean("prod_store.is_tangible"));

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));

                    p.setFlavor(f);

                    if (products.size() < LIMIT_LOAD_PRODUCTS - 1) {
                        products.add(p);
                    } else {
                        products.get(products.size() - 1).setHaveMore(true);
                        break;
                    }
                }

                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return products;
    }

    public ITDayOperation getDayOperation(int idStore, String date) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT op.*, conf.time_spacing_schedule time_spacing FROM stores_days_operations op ");
                sql.append("LEFT JOIN stores_configuration conf ");
                sql.append("ON conf.id_store=op.id_store ");
                sql.append("WHERE op.id_store=? AND day=(dayofweek(DATE_FORMAT(?,'%y/%m/%d'))); ");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1,idStore);
                pst.setString(2, date);

                ResultSet rs = pst.executeQuery();

                ITDayOperation p;
                if (rs.next()) {
                    p = new ITDayOperation();
                    p.setDayName(rs.getString("day_name"));
                    p.setClosingHour(rs.getString("closing_hour"));
                    p.setOpeningHour(rs.getString("opening_hour"));
                    p.setOperating(rs.getBoolean("is_operating"));
                    p.setInetervalScheduler(rs.getInt("time_spacing"));

                    con.close();
                    pst.close();

                    return p ;
                }

                pst.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public ArrayList<ITDayOperation> getAllDaysOperations(int idStore) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        ArrayList<ITDayOperation> days = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM stores_days_operations ");
                sql.append("WHERE id_store=? ;");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1,idStore);

                ResultSet rs = pst.executeQuery();

                ITDayOperation p;
                while (rs.next()) {
                    p = new ITDayOperation();
                    p.setDayName(rs.getString("day_name"));
                    p.setClosingHour(rs.getString("closing_hour"));
                    p.setOpeningHour(rs.getString("opening_hour"));
                    p.setOperating(rs.getBoolean("is_operating"));

                    days.add(p);
                }

                pst.close();
                con.close();

                return days ;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    con.close();
                    if (pst != null) pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public ArrayList<ITAvailableHour> getAvailableHoursToSchedule(int idStore, String date) {

        ITDayOperation day = getDayOperation(idStore, date);

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        ArrayList<ITAvailableHour> schedules = new ArrayList<>();

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT daysOp.day, daysOp.opening_hour, daysOp.closing_hour, date_format(hour_schedule,'%H:%i') hour ");
            sql.append("FROM stores_orders sor ");
            sql.append("LEFT JOIN stores_days_operations daysOp ");
            sql.append("ON daysOp.id_store=sor.id_store AND daysOp.is_operating=1 ");
            sql.append("WHERE daysOp.id_store=? ");
            sql.append("AND date_schedule=? ");
            sql.append("AND day=dayofweek(DATE_FORMAT(? ,'%y/%m/%d')); ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idStore);
                pst.setString(2, date);
                pst.setString(3, date);

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    schedules.add(new ITAvailableHour(schedules.size(), rs.getString("hour")));
                }
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList<ITAvailableHour> hoursAvailable = createHoursAvailabes(day, schedules);

        return hoursAvailable;
    }

    public Pedido createOrderIntoStore(Pedido order) {

        ArrayList<ITProduct> anomalies = checkAnomaly(order);
        if(anomalies != null && anomalies.size() == 0){
            // verifica se os cupons utilizados são do usuário
            Set<ITCoupon> coupons = new DaoCoupons().getCouponsFromListID(order.getCPF(), order.getIdCoupons());

            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null;

            double feeInstallment = (order.getValorCompra()+order.getFeeDelivery())*(order.getFeeInstallment()/100);
            double finalValue = order.getValorCompra()+feeInstallment+order.getFeeDelivery();
            finalValue -= PromotionAPI.caculateDiscount(order, coupons);

            try {

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO stores_orders ");
                sql.append("(id_user, id_store, installments, payment_local, payment_method, payment_status, ");
                sql.append("status_order, purchase_value, title, description, fee_iadd, fee_delivery, fee_installments, ");
                sql.append("cep, county, state, city, neighborhood, street, residence_number, complement, latitude, ");
                sql.append("longitude, base_distance, date_schedule, hour_schedule, send_method) ");
                sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ");

                pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, order.getIdUser());
                pst.setInt(2, order.getIdStore());
                pst.setInt(3, order.getParcelas());
                pst.setString(4, order.getPaymentLocal());
                pst.setString(5, order.getPaymentMode());
                pst.setString(6, order.getPaymentResult().getStatusPayment());
                pst.setString(7, "wait");
                pst.setDouble(8, finalValue);
                pst.setString(9, order.getTitle());
                pst.setString(10, order.getDescricao());
                pst.setDouble(11, 0.0);
                pst.setDouble(12, order.getFeeDelivery());
                pst.setDouble(13, order.getFeeInstallment());
                pst.setString(14, order.getEndereco().getCep());
                pst.setString(15, order.getEndereco().getPais());
                pst.setString(16, order.getEndereco().getEstado());
                pst.setString(17, order.getEndereco().getCidade());
                pst.setString(18, order.getEndereco().getBairro());
                pst.setString(19, order.getEndereco().getRuaLogradouro());
                pst.setInt(20, order.getEndereco().getNumeroResidencia());
                pst.setString(21, order.getEndereco().getComplemento());
                pst.setDouble(22, order.getEndereco().getLatitude());
                pst.setDouble(23, order.getEndereco().getLongitude());
                pst.setDouble(24, 1);
                pst.setString(25, order.getDataMarcada() != null ? order.getDataMarcada() : null);
                pst.setString(26, order.getHoraMarcada() != null ? order.getHoraMarcada() : null);
                pst.setString(27, order.getSendMethod());

                if (con != null) {
                    try {
                        pst.executeUpdate();

                        ResultSet rs = pst.getGeneratedKeys();

                        if(rs.next())
                            order.setId(rs.getInt(1));

                        pst.close();
                        con.close();

                        new Thread() {
                            @Override
                            public void run() {
                                for (ITProduct it : order.values()) {
                                    if (it.getFlavors().size() > 0) {
                                        insertFlavorsFromOrder(it, order);
                                    }
                                }
                                if (order.getIdCoupons().size() != 0)
                                    new DaoCoupons().deleteAllCouponFromListID(order.getIdCoupons(), order.getCPF());

                                insertOrderProducts(order);
                            }
                        }.start();
                        return order;
                    } catch (SQLIntegrityConstraintViolationException e) {
                        pst.close();
                        con.close();
                        e.printStackTrace();
                        return order ;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

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
        } else
            return null ;


        return order;
    }

    public ArrayList<ITAvailableHour> createHoursAvailabes(ITDayOperation dayOp, ArrayList<ITAvailableHour> schedules){

        ArrayList<ITAvailableHour> itens = new ArrayList<>();

        HoursManager opening = new HoursManager();
        opening.setHours(dayOp.getOpeningHour());

        HoursManager closing = new HoursManager();
        closing.setHours(dayOp.getClosingHour());

        for (int hora=0; hora < 24; hora++){

            boolean isOpen = false ;
            boolean isClosed = false ;
            boolean isEquals = false ;
            boolean contains = false ;

            for(int minute=0; minute < 60; minute+=dayOp.getInetervalScheduler()){

                String hour = opening.getHours();

                isOpen = opening.isAfter(hour, dayOp.getOpeningHour());
                isClosed = opening.isAfter(hour, dayOp.getClosingHour());
                isEquals = opening.equals(hour, dayOp.getOpeningHour());
                contains = schedules.contains(new ITAvailableHour(0,hour));

                if(isClosed)
                    break;

                if(isOpen && !contains || isEquals && !contains){
                    itens.add(new ITAvailableHour(0, hour));
                }

                opening.add("00:"+dayOp.getInetervalScheduler());
            }

            if(isClosed)
                break;

        }
        return itens ;
    }

    public ITStore createStore(ITStore store) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO stores (id_recipient, id_categorie, type, logo, background, name, cpf, cnpj) ");
        sql.append("VALUES (?,?,?,?,?,?,?,?); ");
        try {
            pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, store.getIdRecipient());
            pst.setInt(2, store.getIdCategorie());
            pst.setInt(3, 0);
            pst.setBytes(4, store.getLogo());
            pst.setBytes(5, store.getBackground());
            pst.setString(6, store.getStoreName());
            pst.setString(7, store.getCpf());
            pst.setString(8, store.getCnpj());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if(rs.next()){
                int idStoreCreated = rs.getInt(1);

                store.setId(idStoreCreated);
                createAddress(store);
            }


            con.close();
            pst.close();

            return store;
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
                if(con != null)con.close();
                if(pst != null)pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean createAddress(ITStore store) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO stores_address (uf, country, state, city, neighborhood, street, residence_number, postal_code, latitude, longitude, id_store) ");
        sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?); ");
        try {
            pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, store.getAddress().getUf());
            pst.setString(2, store.getAddress().getPais());
            pst.setString(3, store.getAddress().getEstado());
            pst.setString(4, store.getAddress().getCidade());
            pst.setString(5, store.getAddress().getBairro());
            pst.setString(6, store.getAddress().getRuaLogradouro());
            pst.setInt(7, store.getAddress().getNumeroResidencia());
            pst.setString(8, store.getAddress().getCep());
            pst.setDouble(9, store.getAddress().getLatitude());
            pst.setDouble(10, store.getAddress().getLongitude());
            pst.setDouble(11, store.getId());

            int linesAffecteds = pst.executeUpdate();

            con.close();
            pst.close();

            return (linesAffecteds >0);
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
                if(con != null)con.close();
                if(pst != null)pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean createAccountBank(int idStore, int id) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO stores_banks_accounts VALUES (?,?);");
        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, idStore);
            pst.setInt(2, id);
            int linesAffecteds = pst.executeUpdate();

            con.close();
            pst.close();

            return (linesAffecteds >0);
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
                if(con != null)con.close();
                if(pst != null)pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public synchronized LinkedList<ITProduct> searchProducts(SearchProduct it, boolean active) {

        ITProduct p;
        LinkedList<ITProduct> products = new LinkedList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT prod.id, prod.path_img, prod.percentage_discount, prod.title, prod.in_price, prod.per_price, prod.is_weight, prod.is_tangible, prod.id_flavor,");
                sqlConstruct.append("rate.rate_per_czc, rate.rate_per_credit, prod.per_price, prod.instalments, prod.categorie, prod.sub_categorie,");
                sqlConstruct.append("flav.id, flav.flavor FROM czc_products prod  ");
                sqlConstruct.append("JOIN rate_services rate ");
                sqlConstruct.append("LEFT JOIN flavors flav  ");
                sqlConstruct.append("ON flav.id=prod.id_flavor ");
                sqlConstruct.append("WHERE prod.id > ? ");
                sqlConstruct.append("AND prod.title LIKE '%");
                sqlConstruct.append(it.getTitle());
                sqlConstruct.append("%' ");
                sqlConstruct.append("AND prod.categorie=? ");
                sqlConstruct.append(" AND prod.status =");
                sqlConstruct.append((active ? " 'active' " : " 'inactive' "));
                sqlConstruct.append("AND rate.date=(select date from rate_services order by date DESC limit 1) ");
                sqlConstruct.append("ORDER BY prod.id limit ");
                sqlConstruct.append(LIMIT_LOAD_PRODUCTS);

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, it.getIdLastProduct());
                pst.setString(2, it.getCategorie());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {

                    p = new ITProduct();
                    p.setId(rs.getInt("prod.id"));
                    p.setPathImg(rs.getString("prod.path_img"));
                    p.setPercentageDiscount(rs.getInt("prod.percentage_discount"));
                    p.setTitle(rs.getString("prod.title"));
                    p.setTangible(rs.getBoolean("is_tangible"));

                    double inPrice = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.in_price"));
                    double price = UtilPrices.calculateRates(rs.getDouble("rate.rate_per_czc"), rs.getDouble("rate.rate_per_credit"), rs.getDouble("prod.per_price"));

                    p.setInPrice(rs.getInt("percentage_discount") > 0 ? inPrice : 0);

                    p.setPerPrice(active ? price : rs.getDouble("prod.per_price"));
                    p.setInstalments(rs.getInt("prod.instalments"));
                    p.setCategorie(rs.getString("prod.categorie"));
                    //p.setSubCategorie(rs.getString("prod.sub_categorie"));
                    p.setWeight(rs.getBoolean("prod.is_weight"));
                    p.setFlavors(rs.getInt("id_flavor") > 0 ? true : false);

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));

                    p.setFlavor(f);

                    if (products.size() < LIMIT_LOAD_PRODUCTS - 1) {
                        products.add(p);
                    } else {
                        products.get(products.size() - 1).setHaveMore(true);
                        break;
                    }
                }

                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);

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

        return products;
    }

    public byte[] getLogo(int storeID) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT logo FROM stores WHERE id=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, storeID);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] img = rs.getBytes("logo");
                    con.close();
                    pst.close();

                    return img;
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

        return null;
    }

    public byte[] getBackground(int storeID) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT background FROM stores WHERE id=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, storeID);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] img = rs.getBytes("background");
                    con.close();
                    pst.close();

                    return img;
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

        return null;
    }

    public byte[] getImageProduct(int idProduct) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT img FROM stores_products WHERE id=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
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

        return null;
    }

    public byte[] getImageCampaing(int campaingID) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT img FROM stores_campaings WHERE id=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, campaingID);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    byte[] img = rs.getBytes("img");
                    con.close();
                    pst.close();

                    return img;
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

        return null;
    }

    public ITDayOperation checkMakeWishAvailable(Pedido order) {

        Endereco address = null ;

        if(order.getEndereco().getLatitude() == 0){
            Street street = new DaoAddress().getStreetFromCEP(order.getEndereco().getCep());

            address = new Endereco();
            address.setEstado(street.getStateName());
            address.setCidade(street.getCityName());
            address.setBairro(street.getNeighborhood());
            address.setRuaLogradouro(street.getName());
            address.setLatitude(street.getLatitude());
            address.setLongitude(street.getLongitude());
            address.setCep(order.getEndereco().getCep());
        }

        int idStore = order.getIdStore();
        int idUser = order.getIdUser();

        String dateSchedule = order.getDataMarcada();
        String hourScheduler = order.getHoraMarcada();
        String fullDate = dateSchedule+" "+hourScheduler;

        boolean isDeliveryLocal = checkIfDeliveryLocal(idStore, idUser, address);

        ITDayOperation result = new ITDayOperation();

        if(isDeliveryLocal){

            ITDayOperation day = checkIfOperationDay(idStore, dateSchedule) ;

            if(day != null){

                ITDayOperation hour = checkIfOperationHour(idStore, fullDate);

                if(hour != null){

                    boolean isOrderDuplicate = checkOrderDuplicate(idUser, dateSchedule);

                    if(!isOrderDuplicate){
                        result.setIdDay(4);
                        return result ;
                    } else {
                        result.setIdDay(3);
                        return result ;
                    }
                } else {
                    result.setIdDay(2);

                    if(hour == null)
                        hour = getDayOperation(idStore,dateSchedule);
                    result.setOpeningHour(hour.getOpeningHour());
                    result.setClosingHour(hour.getClosingHour());
                    return result ;
                }
            } else {
                result.setIdDay(1);

                if(day == null)
                    day = getDayOperation(idStore, dateSchedule);

                result.setOpeningHour(day.getOpeningHour());
                result.setClosingHour(day.getClosingHour());
                return result ;
            }
        } else {
            result.setIdDay(0);
            return result ;
        }
    }

    public ITDayOperation checkIfOperationDay(int idStore, String date) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM stores_days_operations ");
        sql.append("WHERE id_store=? AND day=dayofweek(DATE_FORMAT(?,'%y/%m/%d')) ");
        sql.append("AND is_operating=1;");

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idStore);
                pst.setString(2, date);

                ResultSet rs = pst.executeQuery();

                ITDayOperation day ;
                if(rs.next()){
                    day = new ITDayOperation();
                    day.setIdDay(rs.getInt("day"));
                    day.setDayName(rs.getString("day_name"));
                    day.setOpeningHour(rs.getString("opening_hour"));
                    day.setClosingHour(rs.getString("closing_hour"));
                    day.setOperating(rs.getBoolean("is_operating"));

                    con.close();
                    pst.close();

                    return day ;
                }
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
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

    public ITDayOperation checkIfOperationHour(int idStore, String date) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM stores_days_operations ");
        sql.append("WHERE id_store=? AND day=dayofweek(DATE_FORMAT(?,'%y/%m/%d')) ");
        sql.append("AND ? >= opening_hour AND ? <= closing_hour ");
        sql.append("AND is_operating=1;");

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idStore);
                pst.setString(2, date);
                pst.setString(3, date);
                pst.setString(4, date);

                ResultSet rs = pst.executeQuery();

                ITDayOperation day ;
                if(rs.next()){
                    day = new ITDayOperation();
                    day.setIdDay(rs.getInt("day"));
                    day.setDayName(rs.getString("day_name"));
                    day.setOpeningHour(rs.getString("opening_hour"));
                    day.setClosingHour(rs.getString("closing_hour"));
                    day.setOperating(rs.getBoolean("is_operating"));

                    con.close();
                    pst.close();

                    return day ;
                }
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
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

    public boolean checkIfDeliveryLocal(int idStore, int idUser, Endereco otherAddress) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM stores_address addr LEFT JOIN stores_configuration conf ");
        sql.append("ON conf.id_store=addr.id_store LEFT JOIN accounts acc ON addr.uf=acc.uf ");
        sql.append("AND addr.country=acc.country AND addr.state=acc.state AND addr.city=acc.city ");
        sql.append("OR (addr.uf=acc.uf AND addr.country=acc.country AND addr.state=acc.state ");
        sql.append("AND addr.city=acc.city AND addr.neighborhood=acc.neighborhood) ");
        sql.append("LEFT JOIN store_collaborators coll ON coll.id_store=addr.id_store ");
        sql.append("WHERE addr.id_store=?  AND acc.id=? AND IF(coll.function='deliveryman', ");

        boolean isOtherAddress = (otherAddress != null);
        if (!isOtherAddress) {
            sql.append("IF(Geo(acc.longitude,acc.latitude,addr.longitude,addr.latitude) ");
        } else {
            sql.append("IF(Geo(?,?,addr.longitude,addr.latitude) ");
        }

        sql.append("<= conf.delivery_in_up_km, conf.delivery_in_up_km, 0) , 100) ;");

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idStore);
                pst.setInt(2, idUser);

                if(isOtherAddress){
                    pst.setDouble(3, otherAddress.getLongitude());
                    pst.setDouble(4, otherAddress.getLatitude());
                }

                ResultSet rs = pst.executeQuery();

                if(rs.next()){
                    con.close();
                    pst.close();

                    return true ;
                }
                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
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


    private void insertFlavorsFromOrder(ITProduct product, Pedido order) {
        StringBuilder sqlConstruct = new StringBuilder();
        sqlConstruct.append("INSERT INTO stores_orders_products  ");
        sqlConstruct.append("(id_user, id_order, id_product, count, id_flavor) ");
        sqlConstruct.append(" VALUES ");

        int counter = product.getFlavors().size();
        for (ITFlavor f : product.getFlavors().values()) {
            counter--;

            sqlConstruct.append(" (" + order.getIdUser() + ",'" + order.getId() + "'," + product.getId() + "," + f.getCount() + "," + f.getId() + ") ");
            sqlConstruct.append(counter > 0 ? "," : ";");

        }

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                pst = con.prepareStatement(sqlConstruct.toString());
                pst.execute();
            } catch (SQLException e) {
                e.printStackTrace();

                try {
                    pst.close();
                    con.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public synchronized boolean insertOrderProducts(Pedido order) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        boolean success = false;
        if (con != null) {
            List<ITProduct> products = new ArrayList<>(order.values());

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO stores_orders_products ");
            sql.append("(id_user, id_order, id_product, count, id_flavor, value) ");
            sql.append("VALUES ");

            StringBuilder sqlContruct = new StringBuilder(sql.toString());

            int counter = 0;
            for (ITProduct p : products) {
                counter++;

                if (counter == products.size() && p.getFlavor().getFlavor() != null) {
                    sqlContruct.append("(?,?,?,?,?,?);");
                } else if (p.getFlavor().getFlavor() != null) {
                    sqlContruct.append("(?,?,?,?,?,?),\n");
                } else if (counter == products.size()) {
                    sqlContruct.append("(?,?,?,?,?,?);");
                } else {
                    sqlContruct.append("(?,?,?,?,?,?),");
                }
            }

            try {
                pst = con.prepareStatement(sqlContruct.toString());

                counter = 1;
                for (ITProduct p : products) {
                    pst.setInt(counter, order.getIdUser() + 0);
                    counter++;
                    pst.setInt(counter, order.getId());
                    counter++;
                    pst.setInt(counter, p.getId() + 0);
                    counter++;
                    pst.setInt(counter, p.getCount());
                    counter++;

                    if (p.getFlavor().getFlavor() != null) {
                        pst.setInt(counter, p.getFlavor().getId());
                    } else {
                        pst.setInt(counter, 0);
                    }

                    counter++;
                    pst.setDouble(counter, p.getPerPriceMultiply());

                    counter++;
                }


                success = pst.execute();

                con.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private boolean checkOrderDuplicate(int idUser, String date) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("select * from stores_orders where DATE_FORMAT(date_schedule, '%Y-%m-%d')=? ");
        sql.append("and status_order = 'wait' ");
        sql.append("and id_user=? ");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, date);
                pst.setInt(2,idUser);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    con.close();
                    pst.close();

                    return true;
                }

                con.close();
                pst.close();
            } catch (SQLException e) {
                try {
                    e.printStackTrace();
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
        }

        return false;
    }

    public ITStore favorite(ITStore store) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT IGNORE INTO stores_favorited (id_account, id_store, favorited) ");
        sql.append("VALUES (?, ?, ?) ");
        sql.append("ON DUPLICATE KEY UPDATE favorited=VALUES(favorited); ");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, store.getIdRequisitor());
            pst.setInt(2, store.getId());
            pst.setBoolean(3, store.isFavorited());
            int linesAffecteds = pst.executeUpdate();

            if (linesAffecteds > 0) {
                con.close();
                pst.close();

                store.setFavorited(store.isFavorited());
                return store;
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
        return null;
    }



    private String generateHashOrder(Pedido order){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        StringBuilder builder = new StringBuilder();
        builder.append(order.getIdUser());
        builder.append("@*");
        builder.append(order.getIdStore());
        builder.append("*");
        builder.append(dateFormat.format(date));
        builder.append("*@");

        return Base64Utils.encodeToString(builder.toString().getBytes());
    }

    public String getIdRecipient(int idStore, int idProduct) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT store.id_recipient FROM stores store LEFT JOIN stores_products prods ");
        sql.append("ON prods.id_store=store.id WHERE store.id=? AND prods.id=? LIMIT 1;");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, idStore);
            pst.setInt(2, idProduct);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                String idRecipient = rs.getString("id_recipient");

                con.close();
                pst.close();

                return idRecipient ;
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
                if(con != null)con.close();
                if(pst != null)pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean updateIdRecipientStore(int idStore, String idRecipient) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE stores SET id_recipient=? WHERE id=?; ");
        try {
            pst = con.prepareStatement(sql.toString());
            pst.setString(1, idRecipient);
            pst.setInt(2, idStore);

            int linesAffecteds = pst.executeUpdate();

            con.close();
            pst.close();

            return (linesAffecteds >0);
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
                if(con != null)con.close();
                if(pst != null)pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private final ArrayList<ITProduct> checkAnomaly(Pedido order){

        Pedido orderSystem = new Pedido();
        ArrayList<ITProduct> anomalies = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT prod_store.id, prod_store.id_store, prod_store.img, prod_store.title, catg_p.categorie,subcatg_p.subcategorie, prod_store.is_tangible tangible_default, prod_store.is_weight weight_default, ");
        sql.append("flav.id, flav.flavor, prod_store.id, prod_store.in_price,  CAST((prod_store.per_price/((100-conf.apply_fee_all_products)/100)) AS DECIMAL(12,2)) per_price, prod_store.is_weight, prod_store.is_tangible, prod_store.enabled, ");
        sql.append("prod_store.prohibited_for_minors, prod_store.instalments, prod_store.percentage_discount ");
        sql.append("FROM stores_products prod_store LEFT JOIN stores_products_flavors flav ");
        sql.append("ON flav.id_product=prod_store.id LEFT JOIN stores_configuration conf ");
        sql.append("ON conf.id_store=prod_store.id_store LEFT JOIN app_categories_products catg_p ");
        sql.append("ON catg_p.id=prod_store.id_categorie LEFT JOIN app_subcategories_products subcatg_p ");
        sql.append("ON subcatg_p.id_categorie=catg_p.id WHERE prod_store.id_store=? ");

        int counter = 0 ;
        for(ITProduct p: order.values()){
            counter++;

            if(counter == 1){
                sql.append("AND prod_store.id=");
                sql.append(p.getId());
                sql.append(" ");
            } else {
                sql.append("OR prod_store.id_store=");
                sql.append(p.getIdStore());
                sql.append(" AND prod_store.id=");
                sql.append(p.getId());
                sql.append(" ");
            }

        }

        sql.append("GROUP BY prod_store.title ORDER BY prod_store.id ASC;");

        try {
            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, order.getIdStore());
            ResultSet rs = pst.executeQuery();

            ITProduct p ;
            while(rs.next()){
                p = new ITProduct();
                p.setId(rs.getInt("prod_store.id"));
                p.setIdStore(rs.getInt("prod_store.id_store"));
                p.setPercentageDiscount(rs.getInt("prod_store.percentage_discount"));
                p.setTitle(rs.getString("prod_store.title"));
                p.setInPrice(rs.getDouble("prod_store.in_price"));
                p.setPerPrice(rs.getDouble("per_price"));
                p.setInstalments(rs.getInt("prod_store.instalments"));
                p.setCategorie(rs.getString("categorie"));
                p.setSubCategorie(rs.getString("subcategorie"));
                p.setWeight(rs.getBoolean("prod_store.is_weight"));
                p.setFlavors(rs.getInt("flav.id") > 0 ? true : false);
                p.setTangible(rs.getBoolean("prod_store.is_tangible"));

                orderSystem.getCarrinho().adicionarAoCarrinho(p);

                if(order.getCarrinho().containsKey(p.getId())){
                    orderSystem.getCarrinho().get(p.getId()).setCount(order.getCarrinho().get(p.getId()).getCount());
                    boolean check = compareProducts(order.getCarrinho().get(p.getId()), p);

                    if(check == false){
                        con.close();
                        pst.close();

                        return null ;
                    }
                }
                else
                    anomalies.add(p);

            }

            con.close();
            pst.close();

            if(order.getValorCompra() != orderSystem.getValorCompra())
                return null ;

            return anomalies ;
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
                if(con != null)con.close();
                if(pst != null)pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private final boolean compareProducts(ITProduct p1, ITProduct p2){
        boolean cID = p1.getId() == p2.getId();
        boolean cIDStore = p1.getIdStore() == p2.getIdStore();
        boolean cDiscount = p1.getPercentageDiscount() == p2.getPercentageDiscount();
        boolean cTitle = p1.getTitle().equals(p2.getTitle());
        boolean cInPrice = p1.getInPrice() == p2.getInPrice();
        boolean cInstallments = p1.getInstalments() == p2.getInstalments();
        boolean cCategorie = p1.getCategorie().equals(p2.getCategorie());
        boolean cSubCategorie = p1.getSubCategorie().equals(p2.getSubCategorie());
        boolean cWeight = p1.isWeight() == p2.isWeight();
        boolean cTangible = p1.isTangible() == p2.isTangible();
        //boolean cFlavorContain = p1.getFlavor()!=null && p2.getFlavor() != null ;
        //boolean cFlavorSize = p1.getFlavors().size() == p2.getFlavors().size();
        boolean cPerPrice = p1.getPerPrice() == p2.getPerPrice();
        boolean priceMultiply = p1.getPerPriceMultiply() == p2.getPerPriceMultiply();

        return cID && cIDStore && cDiscount && cTitle && cInPrice && cInstallments &&
                cCategorie && cSubCategorie && cWeight && cTangible && cPerPrice && priceMultiply;
    }
}
