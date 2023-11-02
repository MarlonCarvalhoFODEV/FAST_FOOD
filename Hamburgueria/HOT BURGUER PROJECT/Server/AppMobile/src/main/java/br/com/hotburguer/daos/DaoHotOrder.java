package br.com.hotburguer.daos;

import apis.pagarme.userbuyer.BProduct;
import br.com.comprazencasa.database.DaoAddress;
import br.com.comprazencasa.database.DaoOrder;
import br.com.comprazencasa.database.DaoProducts;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.comprazencasa.geo.Street;
import br.com.hotburguer.itens.*;
import br.com.hotburguer.geo.DaoRegions;
import br.com.hotburguer.itens.HotDayOperation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoHotOrder {

    public HotOrder getOrder(HotUser hotUser) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        HotOrder order = new HotOrder();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_orders_products prod ");
                sqlConstruct.append("ON prod.id_order=ord.id ");
                sqlConstruct.append("LEFT JOIN hot_users users ");
                sqlConstruct.append("ON users.id=ord.id_user ");
                sqlConstruct.append("WHERE users.email=? AND date_created=now(); ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, hotUser.getId());

                ResultSet rs = pst.executeQuery();

                HotProduct hot;
                while (rs.next()) {
                    hot = new HotProduct();
                    hot.setId(rs.getInt("prod.id"));
                    hot.setCostPrice(rs.getDouble("prod.cost_price"));
                    hot.setCategorie(rs.getString("prod.categorie"));
                    hot.setDescription(rs.getString("prod.description"));
                    hot.setRecipe(rs.getString("prod.recipe"));
                    hot.setFeeDeliveryman(rs.getDouble("prod.fee_deliveryman"));
                    hot.setTitle(rs.getString("prod.title"));
                    hot.setDisabled(rs.getBoolean("prod.disable"));

                    order.setId(rs.getInt("ord.id"));
                    order.setIdUser(rs.getInt("ord.id_user"));
                    order.setTimeDelivery(rs.getString("ord.time_delivery"));
                    order.setStatus(rs.getString("ord.status"));
                    order.setStatuspayment(rs.getString("ord.status_payment"));
                    order.getAddress().setContry(rs.getString("ord.country"));
                    order.getAddress().setState(rs.getString("ord.state"));
                    order.getAddress().setCity(rs.getString("ord.city"));
                    order.getAddress().setNeighborhood(rs.getString("ord.neighborhood"));
                    order.getAddress().setStreet(rs.getString("ord.street"));
                    order.getAddress().setCEP(rs.getString("ord.cep"));
                    order.getAddress().setNumber(rs.getInt("ord.number"));
                    order.getAddress().setLatitude(rs.getDouble("ord.latitude"));
                    order.getAddress().setLongitude(rs.getDouble("ord.longitude"));
                    order.getAddress().setComplement(rs.getString("ord.complement"));
                    order.setDateCreated(rs.getString("ord.date_created"));
                    order.getCart().addItem(hot);
                }

                con.close();
                pst.close();

                return order;
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

    public HotOrder getOrderFromID(int idOrder) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        HotOrder order = new HotOrder();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT * FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_orders_products prod ");
                sqlConstruct.append("ON prod.id_order=ord.id ");
                sqlConstruct.append("LEFT JOIN hot_users users ");
                sqlConstruct.append("ON users.id=ord.id_user ");
                sqlConstruct.append("LEFT JOIN hot_configurations config ");
                sqlConstruct.append("ON config.id=1 WHERE ord.id=? AND ");
                sqlConstruct.append("date_format(ord.date_created, '%y-%m-%d')=date_format(now(), '%y-%m-%d') ");
                sqlConstruct.append("limit 1; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idOrder);

                ResultSet rs = pst.executeQuery();

                HotProduct hot;
                while (rs.next()) {
                    hot = new HotProduct();
                    hot.setId(rs.getInt("prod.id_product"));
                    hot.setPerPrice(rs.getDouble("unit_value"));
                    hot.setCount(rs.getInt("count"));

                    order.setId(rs.getInt("ord.id"));
                    order.setIdTransaction(rs.getInt("ord.id_transaction"));
                    order.setIdUser(rs.getInt("ord.id_user"));
                    order.setTimeDelivery(rs.getString("ord.time_delivery"));
                    order.setStatus(rs.getString("ord.status"));
                    order.setStatuspayment(rs.getString("ord.status_payment"));
                    order.getAddress().setContry(rs.getString("ord.country"));
                    order.getAddress().setState(rs.getString("ord.state"));
                    order.getAddress().setCity(rs.getString("ord.city"));
                    order.getAddress().setNeighborhood(rs.getString("ord.neighborhood"));
                    order.getAddress().setStreet(rs.getString("ord.street"));
                    order.getAddress().setCEP(rs.getString("ord.cep"));
                    order.getAddress().setNumber(rs.getInt("ord.number"));
                    order.getAddress().setLatitude(rs.getDouble("ord.latitude"));
                    order.getAddress().setLongitude(rs.getDouble("ord.longitude"));
                    order.getAddress().setComplement(rs.getString("ord.complement"));
                    order.setFeeDelivery(rs.getDouble("config.fee_delivery"));
                    order.setDateCreated(rs.getString("ord.date_created"));
                    order.getCart().addItem(hot);
                }

                con.close();
                pst.close();

                return order;
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

    public ArrayList<String> getIdsUsersFromOrdersIds(int idDeliveryman, String idDevice, String idRecipient, int[] idOrders) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<String> ids = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ord.id_user FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery del ");
                sqlConstruct.append("ON del.id=ord.id_deliveryman ");
                sqlConstruct.append("WHERE del.id=? AND del.id_device=? ");
                sqlConstruct.append("AND date_format(ord.date_created, '%y-%m-%d')=");
                sqlConstruct.append("date_format(now(), '%y-%m-%d') ");
                sqlConstruct.append("AND del.id_recipient=? AND ord.id IN (");

                for (int i = 0; i < idOrders.length; i++) {
                    if (i == idOrders.length - 1) {
                        sqlConstruct.append("?");
                    } else {
                        sqlConstruct.append("?");
                        sqlConstruct.append(",");
                    }
                }
                sqlConstruct.append(");");

                pst = con.prepareStatement(sqlConstruct.toString());

                pst.setInt(1, idDeliveryman);
                pst.setString(2, idDevice);
                pst.setString(3, idRecipient);

                int paramter = 4;
                for (int i = 0; i < idOrders.length; i++) {
                    pst.setInt(paramter, idOrders[i]);
                    paramter++;
                }

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    ids.add(rs.getString("ord.id_user"));
                }

                con.close();
                pst.close();

                return ids;
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

    public ArrayList<String> getIdsUsersFromOrdersIdsManager(int idManager, String idDevice, JSONArray idOrders) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        ArrayList<String> ids = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ord.id_user FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_managers man ");
                sqlConstruct.append("ON man.id > 0 ");
                sqlConstruct.append("WHERE man.id=? AND man.id_device=? ");
                sqlConstruct.append("AND date_format(ord.date_created, '%y-%m-%d')=");
                sqlConstruct.append("date_format(now(), '%y-%m-%d') ");
                sqlConstruct.append("AND ord.id IN (");

                for (int i = 0; i < idOrders.length(); i++) {
                    if (i == idOrders.length() - 1) {
                        sqlConstruct.append("?");
                    } else {
                        sqlConstruct.append("?");
                        sqlConstruct.append(",");
                    }
                }
                sqlConstruct.append(");");

                pst = con.prepareStatement(sqlConstruct.toString());

                pst.setInt(1, idManager);
                pst.setString(2, idDevice);

                int paramter = 3;
                for (int i = 0; i < idOrders.length(); i++) {
                    pst.setInt(paramter, idOrders.getInt(i));
                    paramter++;
                }

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    ids.add(rs.getString("ord.id_user"));
                }

                con.close();
                pst.close();

                return ids;
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

    public synchronized HotOrder insertOrder(HotOrder order) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        boolean success = false;
        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO hot_orders (id_user, country, ");
            sql.append("state, city, neighborhood, street, cep, latitude, longitude, complement, number) ");
            sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?); ");

            try {
                pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setInt(1, order.getIdUser());
                pst.setString(2, order.getAddress().getContry());
                pst.setString(3, order.getAddress().getState());
                pst.setString(4, order.getAddress().getCity());
                pst.setString(5, order.getAddress().getNeighborhood());
                pst.setString(6, order.getAddress().getStreet());
                pst.setString(7, order.getAddress().getCEP());
                pst.setDouble(8, order.getAddress().getLatitude());
                pst.setDouble(9, order.getAddress().getLongitude());
                pst.setString(10, order.getAddress().getComplement());
                pst.setInt(11, order.getAddress().getNumber());

                int linesAffecteds = pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                rs.next();

                int idOrder = rs.getInt(1);

                con.close();
                pst.close();

                order.setId(idOrder);
                insertOrderProducts(order);

                return order;
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

    private synchronized boolean insertOrderProducts(HotOrder order) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        boolean success = false;
        if (con != null) {
            List<HotProduct> products = new ArrayList<>(order.values());

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO hot_orders_products ");
            sql.append("(id_order, id_product, count, unit_value, total_value) ");
            sql.append("VALUES ");

            StringBuilder sqlContruct = new StringBuilder(sql.toString());

            int counter = 0;
            for (HotProduct p : products) {
                counter++;

                if (counter == products.size()) {
                    sqlContruct.append("(?,?,?,?,?);");
                } else {
                    sqlContruct.append("(?,?,?,?,?),");
                }
            }

            try {
                pst = con.prepareStatement(sqlContruct.toString());

                double feeDelivery = 0;
                counter = 1;
                for (HotProduct p : products) {
                    pst.setInt(counter, order.getId() + 0);
                    counter++;
                    pst.setInt(counter, p.getId() + 0);
                    counter++;
                    pst.setInt(counter, p.getCount() + 0);
                    counter++;
                    pst.setDouble(counter, p.getPerPrice());
                    counter++;

                    Map<String, Integer> map = null;
                    Integer drinks = null;
                    Integer snacks = null;

                    if (feeDelivery == 0) {
                        map = order.getCountProductFromCategorie();
                        drinks = map.get(HotProduct.Categories.DRINKS);
                        snacks = map.get(HotProduct.Categories.SNACKS);
                    }

                    if (drinks != null && drinks > 0 &&
                            (snacks != null && snacks == 0 || snacks == null)) {

                        if (feeDelivery == 0)
                            feeDelivery = new DaoHotManager().getHotConfiguration().getDeliveryMan().getFeeDelivery();

                        pst.setDouble(counter, p.getPerPriceMultiply() + feeDelivery);
                    } else
                        pst.setDouble(counter, p.getPerPriceMultiply());

                    counter++;
                }

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
        return success;
    }

    public synchronized boolean updateStatusOrder(HotOrder order) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (order != null && order.getStatus() != null) {
            if (con != null) {

                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE hot_orders SET status=? WHERE id=?; ");
                try {
                    pst = con.prepareStatement(sql.toString());
                    pst.setString(1, order.getStatus());
                    pst.setInt(2, order.getId());

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
        }
        return false;
    }

    public synchronized boolean updateStatusOrder(int idDeliveryman, String idDevice, String idRecipient, JSONArray idsArray, String status) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null && status != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE hot_orders ord ");
            sql.append("LEFT JOIN hot_users_delivery del ");
            sql.append("ON del.id=ord.id_deliveryman ");
            sql.append("SET ord.status=? WHERE ord.id=? ");
            sql.append("AND del.id=? AND del.id_device=? ");
            sql.append("AND id_recipient=?; ");

            try {
                pst = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);

                for (int i = 0; i < idsArray.length(); i++) {
                    pst.setString(1, status);
                    pst.setInt(2, idsArray.getInt(i));
                    pst.setInt(3, idDeliveryman);
                    pst.setString(4, idDevice);
                    pst.setString(5, idRecipient);
                    pst.addBatch();
                }

                int[] linesAffecteds = pst.executeBatch();
                boolean result = false;

                for (int i = 0; i < linesAffecteds.length; i++) {
                    result = linesAffecteds.length == 0 ? false : true;
                    if (!result)
                        break;
                }

                con.close();
                pst.close();

                return result;
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

    public synchronized boolean updateStatusOrderManager(int idManager, String idDevice, String hash, JSONArray idsOrders, String status) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null && status != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE hot_orders ord ");
            sql.append("LEFT JOIN hot_managers man ");
            sql.append("ON man.id > 0 SET ord.status=? ");
            sql.append("WHERE ord.id=? AND man.id=? AND man.id_device=?  ");
            sql.append("AND (ord.status='waiting' OR ord.status='in_production');");

            try {
                pst = con.prepareStatement(sql.toString());

                for (int i = 0; i < idsOrders.length(); i++) {
                    pst.setString(1, status);
                    pst.setInt(2, idsOrders.getInt(i));
                    pst.setInt(3, idManager);
                    pst.setString(4, idDevice);
                    pst.addBatch();
                }

                int[] linesAffecteds = pst.executeBatch();
                boolean result = false;

                for (int i = 0; i < linesAffecteds.length; i++) {
                    result = linesAffecteds.length == 0 ? false : true;
                    if (!result)
                        break;
                }

                con.close();
                pst.close();

                return result;
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

    public final boolean updateStatusPayment(ITOrderValidator validator) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE hot_orders SET id_transaction=?, status_payment=? ");
        sql.append("WHERE id=? AND id_user=?;");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, String.valueOf(validator.getIdTransaction()));
                pst.setString(2, validator.getStatusPayment());
                pst.setInt(3, validator.getIdOrder());
                pst.setInt(4, validator.getIdUser());

                int linesAffecteds = 0;
                try {
                    linesAffecteds = pst.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e) {
                    con.close();
                    pst.close();
                    return e.getMessage().contains("Duplicate") ? true : false;
                }
                con.close();
                pst.close();

                return (linesAffecteds > 0);
            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();

                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex1);
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
        return false;
    }

    public HotDayOperation checkMakeWishAvailable(LAddress orderAddress, boolean coordinates) {

        LAddress address = null;

        if (!coordinates) {
            Street street = new DaoHotAddress().getAddressFromCep(orderAddress.getCEP());

            address = new LAddress();
            address.setState(street.getStateName());
            address.setCity(street.getCityName());
            address.setNeighborhood(street.getNeighborhood());
            address.setStreet(street.getName());
            address.setLatitude(street.getLatitude());
            address.setLongitude(street.getLongitude());
            address.setCEP(orderAddress.getCEP());
        }

        boolean isDeliveryLocal = checkIfDeliveryLocal(orderAddress);

        HotDayOperation result = new HotDayOperation();

        if (isDeliveryLocal) {

            HotDayOperation day = checkIfOperationDayNow();

            if (day != null) {

                HotDayOperation hour = checkIfOperationHourNow();

                if (hour != null && hour.isOperatingHour()) {
                    result.setIdDay(3);
                    return result;
                } else {

                    hour = getDayOperationNow();

                    result.setIdDay(2);
                    result.setOpeningHour(hour.getOpeningHour());
                    result.setClosingHour(hour.getClosingHour());
                    return result;
                }
            } else {
                result.setIdDay(1);
                if (day != null) {
                    result.setOpeningHour(day.getOpeningHour());
                    result.setClosingHour(day.getClosingHour());
                }
                return result;
            }
        } else {
            result.setIdDay(0);
            return result;
        }
    }

    public HotDayOperation checkIfOperationDayNow() {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM hot_operations_days ");
                sql.append("WHERE day=dayofweek(DATE_FORMAT(now(),'%y/%m/%d')) ");
                sql.append("AND is_operating=1;");

                pst = con.prepareStatement(sql.toString());

                ResultSet rs = pst.executeQuery();

                HotDayOperation day;
                if (rs.next()) {
                    day = new HotDayOperation();
                    day.setIdDay(rs.getInt("day"));
                    day.setDayName(rs.getString("day_name"));
                    day.setOpeningHour(rs.getString("opening_hour"));
                    day.setClosingHour(rs.getString("closing_hour"));
                    day.setOperating(rs.getBoolean("is_operating"));

                    con.close();
                    pst.close();

                    return day;
                }
                con.close();
                pst.close();
            } catch (SQLException e) {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

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
        return null;
    }

    public HotDayOperation checkIfOperationHourNow() {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *,(now() >= opening_hour AND now() <= closing_hour)is_operating_hour FROM hot_operations_days ");
        sql.append("WHERE day=dayofweek(DATE_FORMAT(now(),'%y/%m/%d')) ");
        sql.append("AND now() >= opening_hour AND now() <= closing_hour ");
        sql.append("AND is_operating=1;");

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            try {
                pst = con.prepareStatement(sql.toString());
                ResultSet rs = pst.executeQuery();

                HotDayOperation day;
                if (rs.next()) {
                    day = new HotDayOperation();
                    day.setIdDay(rs.getInt("day"));
                    day.setDayName(rs.getString("day_name"));
                    day.setOpeningHour(rs.getString("opening_hour"));
                    day.setClosingHour(rs.getString("closing_hour"));
                    day.setOperating(rs.getBoolean("is_operating"));
                    day.setOperatingHour(rs.getBoolean("is_operating_hour"));

                    con.close();
                    pst.close();

                    return day;
                }
                con.close();
                pst.close();
            } catch (SQLException e) {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
        return null;
    }

    public HotDayOperation getDayOperationNow() {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM hot_operations_days WHERE day=(dayofweek(DATE_FORMAT(now(),'%y/%m/%d'))); ");

                pst = con.prepareStatement(sql.toString());

                ResultSet rs = pst.executeQuery();

                HotDayOperation p;
                if (rs.next()) {
                    p = new HotDayOperation();
                    p.setDayName(rs.getString("day_name"));
                    p.setClosingHour(rs.getString("closing_hour"));
                    p.setOpeningHour(rs.getString("opening_hour"));
                    p.setOperating(rs.getBoolean("is_operating"));

                    con.close();
                    pst.close();

                    return p;
                }

                pst.close();
                con.close();

            } catch (SQLException ex) {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Logger.getLogger(DaoProducts.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean checkIfDeliveryLocal(LAddress address) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            try {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM hot_operations_neighborhood neighb ");
                sql.append("LEFT JOIN hot_operations_street street ");
                sql.append("ON street.id_operation_neighborhood=neighb.id ");
                sql.append("WHERE country=? AND state=? ");
                sql.append("AND city=? AND neighborhood=? AND is_operating=1 ");
                sql.append("GROUP BY neighborhood; ");

                pst = con.prepareStatement(sql.toString());
                pst.setString(1, address.getContry());
                pst.setString(2, address.getState());
                pst.setString(3, address.getCity());
                pst.setString(4, address.getNeighborhood());
                //pst.setString(5, address.getCEP());
                //pst.setString(5, address.getStreet());

                ResultSet rs = pst.executeQuery();

                DaoHotAddress daoAddress = new DaoHotAddress();
                if (rs.next()) {
                    daoAddress
                            .insertStreet(
                                    rs.getInt("id"),
                                    address.getStreet(),
                                    address.getCEP());
                    con.close();
                    pst.close();

                    return true;
                } else {
                    // insert region
                    LAddress result = daoAddress.insertNeighborhood(address);
                    if (result.getId() > 0)
                        daoAddress.insertStreet(result.getId(), result.getStreet(), result.getCEP());
                }
                con.close();
                pst.close();
            } catch (SQLException e) {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
        return false;
    }


    public HotOrder getStatusOrder(HotOrder order) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT status FROM hot_orders WHERE id=? AND id_user=?; ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, order.getId());
                pst.setInt(2, order.getIdUser());

                ResultSet rs = pst.executeQuery();
                HotOrder ord = null;
                if (rs.next()) {
                    ord = new HotOrder();

                    String status = rs.getString("status");
                    if (status.equals(HotOrder.Status.DELIVERYMAN_HOLDING))
                        ord.setStatus(HotOrder.Status.IN_PRODUCTION);
                    else if (status.equals(HotOrder.Status.FOR_DELIVERY))
                        ord.setStatus(HotOrder.Status.IN_PRODUCTION);
                    else
                        ord.setStatus(status);
                }

                con.close();
                pst.close();


                return ord;
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

    public boolean deleteOrder(ITOrderValidator validator) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM hot_orders WHERE id=? AND id_user=?; ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, validator.getIdOrder());
                pst.setInt(2, validator.getIdUser());

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

    public Map<Integer, HotOrderManager> getOrders(int idManager, String idDevice, String hash) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        Map<Integer, HotOrderManager> orders = new TreeMap<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT ord.id, users.name, prod.id, prod.title, prod.recipe, ");
                sqlConstruct.append("ord_prod.count, ord.latitude, ord.longitude FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_orders_products ord_prod  ON ord_prod.id_order=ord.id ");
                sqlConstruct.append("LEFT JOIN hot_users users ON users.id=ord.id_user LEFT JOIN hot_products prod ");
                sqlConstruct.append("ON prod.id=ord_prod.id_product LEFT JOIN hot_configurations config ON config.id=1 ");
                sqlConstruct.append("LEFT JOIN hot_managers man ON man.id > 0 WHERE man.id=? AND man.id_device=? AND ");
                sqlConstruct.append("(ord.status='waiting' OR ord.status='in_production') AND ord.status_payment='AUTHORIZED' AND ");
                sqlConstruct.append("date_format(ord.date_created,'%y-%m-%d') = date_format(now(),'%y-%m-%d') ");
                sqlConstruct.append("ORDER BY ord.id; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idManager);
                pst.setString(2, idDevice);
                // pst.setString(3, hash);

                ResultSet rs = pst.executeQuery();

                HotProduct hot;
                HotOrderManager orderManager;
                while (rs.next()) {
                    hot = new HotProduct();
                    hot.setId(rs.getInt("prod.id"));
                    hot.setRecipe(rs.getString("prod.recipe"));
                    hot.setTitle(rs.getString("prod.title"));
                    hot.setCount(rs.getInt("count"));

                    orderManager = new HotOrderManager();
                    orderManager.setIdOrder(rs.getInt("ord.id"));
                    orderManager.setClientName(rs.getString("name"));
                    orderManager.setLatitude(rs.getDouble("latitude"));
                    orderManager.setLongitude(rs.getDouble("longitude"));
                    //orderManager.setDistance(rs.getDouble("distance"));
                    orderManager.getProducts().add(hot);

                    if (!orders.containsKey(orderManager.getIdOrder()))
                        orders.put(orderManager.getIdOrder(), orderManager);
                    else
                        orders.get(orderManager.getIdOrder()).getProducts().add(hot);
                }


                con.close();
                pst.close();

                return orders;
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

        return orders;
    }

    public Map<Integer, HotOrderManager> getOrdersPromptDelivery(int idManager, String idDevice, String hash) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        Map<Integer, HotOrderManager> orders = new TreeMap<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT del.id, del.full_name, ord.id, users.name, prod.id, prod.title, prod.recipe, ");
                sqlConstruct.append("ord_prod.count, ord.latitude, ord.longitude FROM hot_orders ord ");
                sqlConstruct.append("LEFT JOIN hot_orders_products ord_prod  ON ord_prod.id_order=ord.id ");
                sqlConstruct.append("LEFT JOIN hot_users users ON users.id=ord.id_user LEFT JOIN hot_products prod ");
                sqlConstruct.append("ON prod.id=ord_prod.id_product LEFT JOIN hot_configurations config ON config.id=1 ");
                sqlConstruct.append("LEFT JOIN hot_users_delivery del ON del.id = ord.id_deliveryman_notifyed ");
                sqlConstruct.append("LEFT JOIN hot_managers man ON man.id > 0 WHERE man.id=? AND man.id_device=? AND ");
                sqlConstruct.append("(ord.status='for_delivery') AND ord.status_payment='AUTHORIZED' AND ");
                sqlConstruct.append("date_format(ord.date_created,'%y-%m-%d') = date_format(now(),'%y-%m-%d') ");
                sqlConstruct.append("AND ord.id_deliveryman IS NULL AND ord.id_deliveryman_notifyed IS NOT NULL ORDER BY ord.id; ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idManager);
                pst.setString(2, idDevice);
                // pst.setString(3, hash);

                ResultSet rs = pst.executeQuery();

                HotProduct hot;
                HotOrderManager orderManager;
                while (rs.next()) {
                    hot = new HotProduct();
                    hot.setId(rs.getInt("prod.id"));
                    hot.setRecipe(rs.getString("prod.recipe"));
                    hot.setTitle(rs.getString("prod.title"));
                    hot.setCount(rs.getInt("count"));

                    orderManager = new HotOrderManager();
                    orderManager.setIdOrder(rs.getInt("ord.id"));
                    orderManager.setIdDeliveryman(rs.getInt("del.id"));
                    orderManager.setDeliverymanName(rs.getString("del.full_name"));
                    orderManager.setClientName(rs.getString("name"));
                    orderManager.setLatitude(rs.getDouble("latitude"));
                    orderManager.setLongitude(rs.getDouble("longitude"));
                    //orderManager.setDistance(rs.getDouble("distance"));
                    orderManager.getProducts().add(hot);

                    if (!orders.containsKey(orderManager.getIdOrder()))
                        orders.put(orderManager.getIdOrder(), orderManager);
                    else
                        orders.get(orderManager.getIdOrder()).getProducts().add(hot);
                }
                con.close();
                pst.close();

                return orders;
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

        return orders;
    }

    public HotGroupOrders createGroupOrders(ArrayList<HotOrderManager> orders) {
        HotGroupOrders groups = null;
        if (orders.size() > 0) {
            // Pega o primeiro pedido mais proximo da base
            HotOrderManager first = orders.get(0);
            // Remove da lista para comparação
            orders.remove(first);

            // Cria o primeiro membro de um Grupo de Pedidos
            groups = new HotGroupOrders(first);

            for (HotOrderManager m : orders) {
                double distance = DaoRegions.calculateDistanceKM(first.getLatitude(), first.getLongitude(),
                        m.getLatitude(), m.getLongitude());

                m.setDistance(distance);

                if (distance <= 2.7) {
                    // Adiciona um novo membro P/Grupo

                    groups.add(m);
                    //orders.remove(m);
                } else
                    break;
            }
        }

        return groups;
    }

    public HotGroupOrders createGroupOrders2(ArrayList<HotOrderManager> orders, double distanceOrders) {
        HotGroupOrders group = null;
        if (orders.size() > 0) {
            // Pega o primeiro pedido mais proximo da base
            HotOrderManager compare = orders.get(0);

            LAddress baseAddress = new DaoSystemConfiguration().getCurrentLocationBusiness();
            compare.setDistance(DaoRegions.calculateDistanceKM(baseAddress.getLatitude(), baseAddress.getLongitude(),
                    compare.getLatitude(), compare.getLongitude()));

            // Remove da lista para comparação
            orders.remove(compare);

            // Cria o primeiro membro de um Grupo de Pedidos
            group = new HotGroupOrders(compare);

            for (HotOrderManager m : orders) {

                double distance = DaoRegions.calculateDistanceKM(compare.getLatitude(), compare.getLongitude(),
                        m.getLatitude(), m.getLongitude());

                m.setDistance(distance);

                if (distance <= distanceOrders) {
                    // Adiciona um novo membro P/Grupo

                    group.add(m);
                    //orders.remove(m);
                }

                //compare = m ;
            }
        }

        return group;
    }

    public boolean checkIfAcceptedRunning(int idOrder) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT id_deliveryman FROM hot_orders WHERE id=?;");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idOrder);

                ResultSet rs = pst.executeQuery();

                boolean accepted = false;
                if (rs.next()) {
                    accepted = (rs.getInt("id_deliveryman") > 0);
                }

                con.close();
                pst.close();

                return accepted;
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

    public boolean updateIdDeliverymanNotifyed(int idManager, String idDevice, int idDeliveryMan, JSONArray idOrders) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_orders ord LEFT JOIN hot_managers man ON man.id > 0 ");
                sqlConstruct.append("SET id_deliveryman_notifyed=? ");
                sqlConstruct.append("WHERE man.id=? AND man.id_device=? AND ord.id=? ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);

                for (int i = 0; i < idOrders.length(); i++) {
                    pst.setInt(1, idDeliveryMan);
                    pst.setInt(2, idManager);
                    pst.setString(3, idDevice);
                    pst.setInt(4, idOrders.getInt(i));
                    pst.addBatch();
                }

                int[] linesAffecteds = pst.executeBatch();
                boolean result = false;

                for (int i = 0; i < linesAffecteds.length; i++) {
                    result = linesAffecteds.length == 0 ? false : true;
                    if (!result)
                        break;
                }

                con.close();
                pst.close();

                return result;
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

    public boolean reduceQuantityProduct(int idUser, String idDevice, ArrayList<BProduct> products) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("UPDATE hot_products prods LEFT JOIN hot_users users ");
                sqlConstruct.append("ON users.id > 0 SET quantity_in_stock=(quantity_in_stock-1) WHERE prods.id=? ");
                sqlConstruct.append("AND users.id=? AND users.device_id=?; ");

                pst = con.prepareStatement(sqlConstruct.toString(), PreparedStatement.RETURN_GENERATED_KEYS);

                for (BProduct p : products) {
                    pst.setInt(1, p.getId());
                    pst.setInt(2, idUser);
                    pst.setString(3, idDevice);
                    pst.addBatch();

                }

                int[] linesAffecteds = pst.executeBatch();
                boolean result = false;

                for (int i = 0; i < linesAffecteds.length; i++) {
                    result = linesAffecteds.length == 0 ? false : true;
                    if (!result)
                        break;
                }

                con.close();
                pst.close();

                return result;
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

    public Map<String, Boolean> checkProductsAvailables(int idUser, String idDevice, JSONArray idProducts) {

        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        Map<String, Boolean> map = new HashMap<>();

        if (con != null) {
            try {

                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("SELECT prods.id, title, (quantity_in_stock > 0) available ");
                sqlConstruct.append("FROM hot_products prods LEFT JOIN hot_users users ");
                sqlConstruct.append("ON users.id > 0 WHERE users.id=? AND users.device_id=? ");
                sqlConstruct.append("AND prods.id IN (");
                for (int i = 0; i < idProducts.length(); i++) {
                    if (i == 0)
                        sqlConstruct.append("?");
                    else {
                        sqlConstruct.append(",");
                        sqlConstruct.append("?");
                    }
                }
                sqlConstruct.append(");");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setInt(1, idUser);
                pst.setString(2, idDevice);

                int counter = 3;
                for (int i = 0; i < idProducts.length(); i++) {
                    pst.setInt(counter, idProducts.getInt(i));
                    counter++;
                }

                ResultSet rs = pst.executeQuery();

                JSONObject ob = null;
                while (rs.next()) {
                    String title = rs.getString("title");
                    boolean available = rs.getBoolean("available");

                    if (!available)
                        map.put(title, available);
                }
                con.close();
                pst.close();

                return map;
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
}
