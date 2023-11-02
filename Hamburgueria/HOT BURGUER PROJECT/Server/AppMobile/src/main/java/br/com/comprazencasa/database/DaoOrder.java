/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import br.com.comprazencasa.collaborator.ITOrderBuyer;
import br.com.comprazencasa.geo.LatLng;
import br.com.comprazencasa.itens.*;
import br.com.comprazencasa.market.Pedido;
import br.com.comprazencasa.user.Endereco;
import br.com.comprazencasa.util.*;
import br.com.hotburguer.daos.DBConnections;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marlon
 */
public class DaoOrder {

    private final DaoAdmin dao;

    public DaoOrder() {
        this.dao = new DaoAdmin();
    }

    private boolean insertOrder(Pedido order, Set<ITCoupon> coupons) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        try {

            RateServices rate = UtilPrices.getLastRate();
            //double distanceKm = dao.getDaoRegions().calculateDistanceKM(CEP_BASE, order.getEndereco().getCep());
            double distanceKm = dao.getDaoAddress().calculateDistance(
                    dao.getConfig().getLocation().getLatitude(),dao.getConfig().getLocation().getLongitude(),
                    order.getEndereco().getLatitude(),order.getEndereco().getLongitude(),
                    "K");

            if (order.getEndereco().getLatitude() == 0 || order.getEndereco().getLongitude() == 0) {
                LatLng lat = dao.getDaoAddress().getLatLng(order.getEndereco().getCep());
                order.getEndereco().setLatitude(lat.lat);
                order.getEndereco().setLongitude(lat.lng);
            }

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO orders ");
            sql.append("(id, id_user, title, description, cpf, plots, ");
            sql.append("purchase_value, cep,county, city, neighborhood, ");
            sql.append("street, residence_number, complement, rate_service, ");
            sql.append("rate_mp, id_rate, base_distance, payment_method,payment_local, latitude, longitude, fee_delivery, date_schedule, hour_schedule, payment_status, id_transaction) ");
            sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pst = con.prepareStatement(sql.toString());
            pst.setInt(1, order.getId());
            pst.setInt(2, order.getIdUser());
            pst.setString(3, order.getTitle());
            pst.setString(4, order.getDescricao());
            pst.setString(5, order.getCPF());
            pst.setInt(6, order.getParcelas());
            pst.setDouble(7, UtilPrices.calculateValueFinal(order, coupons, order.getParcelas()));
            pst.setString(8, order.getEndereco().getCep());
            pst.setString(9, order.getEndereco().getEstado());
            pst.setString(10, order.getEndereco().getCidade());
            pst.setString(11, order.getEndereco().getBairro());
            pst.setString(12, order.getEndereco().getRuaLogradouro());
            pst.setInt(13, order.getEndereco().getNumeroResidencia());
            pst.setString(14, order.getEndereco().getComplemento());
            pst.setDouble(15, rate.getRateComprazemCasa());
            pst.setDouble(16, rate.getRatePercentualCredit());
            pst.setInt(17, rate.getId());
            pst.setDouble(18, distanceKm+(0.25));
            pst.setString(19, order.getPaymentMode());
            pst.setString(20, order.getPaymentLocal());
            pst.setDouble(21, order.getEndereco().getLatitude());
            pst.setDouble(22, order.getEndereco().getLongitude());
            pst.setDouble(23, UtilPrices.getFeeFelivery(order.getValorCompra()));
            pst.setString(24, order.getDataMarcada() != null ? order.getDataMarcada() : null);
            pst.setString(25, order.getHoraMarcada() != null ? order.getHoraMarcada() : null);
            pst.setString(26,order.getPaymentResult().getStatusPayment());
            pst.setString(27, order.getPaymentResult().getIdTransaction());

            if (con != null) {
                try {
                    pst.execute();
                    pst.close();
                    con.close();

                    new Thread() {
                        @Override
                        public void run() {
                            for (ITProduct it : order.values()) {
                                if (it.getFlavors().size() > 0) {
                                    saveAllFlavorFromProduct(it, order);
                                }
                            }
                            if (order.getIdCoupons().size() != 0)
                                dao.getDaoCoupons().deleteAllCouponFromListID(order.getIdCoupons(), order.getCPF());

                            saveOrderProducts2(order.getIdUser(), order.getId(), order.values());
                        }
                    }.start();
                    return true;
                } catch (SQLIntegrityConstraintViolationException e) {
                    pst.close();
                    con.close();

                    return true ;
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

        return false;
    }

    public synchronized int makeWish(Pedido order) {

        int result = -1 ;
        if(checkMakeWishAvailable(order.getIdUser(), order.getParcelas(), order.getDataMarcada(), order.getEndereco().getCep()) == 99){
            Set<ITCoupon> coupons = dao.getDaoCoupons().getCouponsFromListID(order.getCPF(), order.getIdCoupons());
            result = insertOrder(order, coupons) ? 1 : -1;
        }

        return result;
    }

    /*
    public synchronized int makeWish(Pedido order) {
        Set<ITCoupon> coupons = dao.getDaoCoupons().getCouponsFromListID(order.getCPF(), order.getIdCoupons());

        if (dao.getConfig().isReleaseMakeWish()) {
            if (getCountOrders() < dao.getConfig().getLimitOrders()) {
                if (order.getIdCoupons().size() == coupons.size()) {
                    if (order.getParcelas() <= 5) {
                        Calendar d = Calendar.getInstance(Locale.getDefault());

                        int hour = d.get(Calendar.HOUR_OF_DAY);
                        int minutes = d.get(Calendar.MINUTE);
                        int secounds = d.get(Calendar.SECOND);

                        int dayA = d.get(Calendar.DAY_OF_MONTH);
                        int monthA = d.get(Calendar.MONTH) + 1;
                        int yearA = d.get(Calendar.YEAR);

                        boolean isOpeningDay = dayA == dao.getConfig().getDay() && monthA == dao.getConfig().getMonth() && yearA == dao.getConfig().getYear();
                        boolean passedInauguration = dayA > dao.getConfig().getDay() && monthA > dao.getConfig().getMonth() && yearA > dao.getConfig().getYear() || yearA > dao.getConfig().getYear();

                        if (isOpeningDay || passedInauguration) {

                            // Verifica se esta no horario de funcionamento
                            if ((dao.getConfig().getHourInitial() > 0 && dao.getConfig().getHourFinal() > 0)
                                    && hour >= dao.getConfig().getHourInitial() && hour <= dao.getConfig().getHourFinal() && minutes <= 59 && secounds <= 59) {

                                // Verifica se entrega no endereço do cliente
                                if (isDelivered(order.getEndereco())) {

                                    if (isOrderDuplicate(order) == null) {
                                        return insertOrder(order, coupons) ? 1 : -1;
                                    } else {
                                        Pedido ver = isOrderDuplicate(order);
                                        if (Double.valueOf(ver.getStatusName()) == UtilPrices.calculateValueFinal(order, coupons, order.getParcelas())
                                                && ver.getParcelas() == order.getParcelas() && ver.getPaymentMode().equals(order.getPaymentMode())) {
                                            return 1;
                                        }
                                        return 600;
                                    }
                                } else {
                                    return 100;
                                }
                            } else {
                                return 200;
                            }
                        } else {
                            return 300;
                        }
                    } else {
                        return -10;
                    }
                } else return -1;
            } else {
                return 400;
            }
        } else {
            return 0;
        }
    }
    */

    public synchronized int makeWishScheduleOrder(Pedido order) {
        Set<ITCoupon> coupons = dao.getDaoCoupons().getCouponsFromListID(order.getCPF(), order.getIdCoupons());

        /*
        if (dao.getConfig().isReleaseMakeWish()) {

            if (getCountOrders() < dao.getConfig().getLimitOrders()) {

                if (order.getIdCoupons().size() == coupons.size()) {

                    if (order.getParcelas() <= 5) {

                        Calendar d = Calendar.getInstance(Locale.getDefault());

                        int hour = d.get(Calendar.HOUR_OF_DAY);
                        int minutes = d.get(Calendar.MINUTE);
                        int secounds = d.get(Calendar.SECOND);

                        int dayA = d.get(Calendar.DAY_OF_MONTH);
                        int monthA = d.get(Calendar.MONTH) + 1;
                        int yearA = d.get(Calendar.YEAR);

                        boolean isOpeningDay = dayA == dao.getConfig().getOpeningDate().getDay()
                                && monthA == dao.getConfig().getOpeningDate().getMonth()
                                && yearA == dao.getConfig().getOpeningDate().getYear();

                        boolean passedInauguration = dayA > dao.getConfig().getOpeningDate().getDay()
                                && monthA >= dao.getConfig().getOpeningDate().getMonth()
                                && yearA > dao.getConfig().getOpeningDate().getYear()
                                || yearA > dao.getConfig().getOpeningDate().getYear();

                        if (isOpeningDay || passedInauguration) {

                            // Verifica se esta no horario de funcionamento
                            int diaAtual = DataPadraoUtil.getDayOfWeek();
                            int hourInicialConf = dao.getConfig().getOpeningHour().getStartHour();
                            int hourFinalConf = dao.getConfig().getOpeningHour().getFinalHour();

                            if ((hourInicialConf > 0 && hourFinalConf > 0)
                                    && hour >= hourInicialConf && hour <= hourFinalConf && minutes <= 59 && secounds <= 59) {

                                // Verifica se entrega no endereço do cliente
                                if (isDelivered(order.getEndereco())) {

                                    if (isOrderDuplicate(order) == null) {
                                        return insertOrder(order, coupons) ? 1 : -1;
                                    } else {

                                        Pedido ver = isOrderDuplicate(order);
                                        if (Double.valueOf(ver.getStatusName()) == UtilPrices.calculateValueFinal(order, coupons, order.getParcelas())
                                                && ver.getParcelas() == order.getParcelas() && ver.getPaymentMode().equals(order.getPaymentMode())) {
                                            return 1;
                                        }
                                        return 600;
                                    }
                                } else {
                                    return 100;
                                }
                            } else {
                                return 200;
                            }
                        } else {
                            return 300;
                        }
                    } else {
                        return -10;
                    }
                } else return -1;
            } else {
                return 400;
            }
        } else {
            return 0;
        }*/
        return -1;
    }

    public synchronized int makeWishReScheduleOrder(int idUser, int idOrder, String newDate, String newHour) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            String sql = "UPDATE orders SET date_schedule=?, hour_schedule=? WHERE id_user=? AND id=? ";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, newDate);
                pst.setString(2, newHour);
                pst.setInt(3, idUser);
                pst.setInt(4, idOrder);
                pst.executeUpdate();

                con.close();
                pst.close();

                return 1;
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
        return 0;
    }

    public ArrayList<ITAvailableHour> getHoursUnavailableFromDate(ITAvailableHour availableHour) {

        ArrayList<ITAvailableHour> hours = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT daysOp.day, daysOp.opening_hour, daysOp.closing_hour, date_format(hour_schedule,'%H:%i') hour FROM stores_orders sor ");
        sql.append("LEFT JOIN stores_days_operations daysOp ");
        sql.append("ON daysOp.id_store=sor.id_store ");
        sql.append("WHERE daysOp.id_store=? ");
        sql.append("AND date_schedule=? ");
        sql.append("AND day=dayofweek(DATE_FORMAT(?,'%y/%m/%d')); ");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, availableHour.getIdStore());
                pst.setString(2, availableHour.getDateRequisition());
                pst.setString(3, availableHour.getDateRequisition());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    hours.add(new ITAvailableHour(0, rs.getString("hour")));
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

        return hours;
    }

    public ArrayList<ITAvailableHour> getDaysOperations(ITAvailableHour availableHour) {

        ArrayList<ITAvailableHour> hours = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT daysOp.day, daysOp.opening_hour, daysOp.closing_hour, date_format(hour_schedule,'%H:%i') hour FROM stores_orders sor ");
        sql.append("LEFT JOIN stores_days_operations daysOp ");
        sql.append("ON daysOp.id_store=sor.id_store ");
        sql.append("WHERE daysOp.id_store=? ");
        sql.append("AND date_schedule=? ");
        sql.append("AND day=dayofweek(DATE_FORMAT(?,'%y/%m/%d')); ");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, availableHour.getIdStore());
                pst.setString(2, availableHour.getDateRequisition());
                pst.setString(3, availableHour.getDateRequisition());

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    hours.add(new ITAvailableHour(0, rs.getString("hour")));
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

        return hours;
    }

    public synchronized ArrayList<Pedido> getOrdersFromUserID(int id) {
        ArrayList<Pedido> pedis = new ArrayList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM stores_orders WHERE ");
                sql.append("datediff(DATE_FORMAT(date, '%Y-%m-%d'),DATE_FORMAT( CURDATE(), '%Y-%m-%d')) >= 0 ");
                sql.append("OR datediff(date_schedule, DATE_FORMAT( CURDATE(), '%Y-%m-%d')) >= 0 ");
                sql.append("OR date_schedule = null OR date_schedule = '' ");
                sql.append("AND status_order != 'canceled' ");
                sql.append("AND id_user=? ORDER BY date ASC ");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, id);

                ResultSet rs = pst.executeQuery();

                Pedido pd;
                while (rs.next()) {
                    pd = new Pedido();
                    pd.setId(rs.getInt("id"));
                    pd.setTitle(rs.getString("title"));
                    pd.setDescricao(rs.getString("description"));
                    pd.setParcelas(rs.getInt("installments"));
                    pd.setIdUser(rs.getInt("id_user"));
                    pd.setStatus(rs.getString("status_order"));
                    pd.setOrderPrice(rs.getDouble("purchase_value"));
                    pd.getEndereco().setCep(rs.getString("cep"));
                    pd.getEndereco().setEstado(rs.getString("county"));
                    pd.getEndereco().setCidade(rs.getString("city"));
                    pd.getEndereco().setBairro(rs.getString("neighborhood"));
                    pd.getEndereco().setRuaLogradouro(rs.getString("street"));
                    pd.getEndereco().setNumeroResidencia(rs.getInt("residence_number"));
                    pd.getEndereco().setComplemento(rs.getString("complement"));
                    pd.getEndereco().setLongitude(rs.getDouble("longitude"));
                    pd.getEndereco().setLatitude(rs.getDouble("latitude"));
                    pd.setDataMarcada(rs.getString("date_schedule"));
                    pd.setHoraMarcada(rs.getString("hour_schedule"));
                    pd.setData(DataPadraoUtil.formataData(rs.getString("date"), "yyyy-MM-dd", "dd-MM-yyyy"));
                    pd.getPaymentResult().setStatusPayment(rs.getString("payment_status"));
                    pedis.add(pd);
                }

                con.close();
                pst.close();

            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    if(pst!=null)pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } finally {
                try {
                    con.close();
                    if(pst!=null)pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return pedis;
    }

    public synchronized LinkedList<Pedido> getOrdersHistoricFromUserID(int id) {
        LinkedList<Pedido> pedis = new LinkedList<>();

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM stores_orders ord ");
                sql.append("where (datediff(ord.date,CURDATE())  < 0 and (ord.date_schedule = null or ord.date_schedule = '')) ");
                sql.append("or (datediff(ord.date,CURDATE())  < 0 and (datediff(ord.date_schedule,CURDATE())  >= 0 )) and status_order='canceled' ");
                sql.append("or (datediff(ord.date,CURDATE())  < 0 and (datediff(ord.date_schedule,CURDATE())  < 0 )) ");
                sql.append("AND id_user=? ORDER BY ord.date DESC ");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, id);
                //pst.setString(2,email);

                ResultSet rs = pst.executeQuery();

                Pedido pd;
                while (rs.next()) {
                    pd = new Pedido();
                    pd.setId(rs.getInt("id"));
                    pd.setTitle(rs.getString("title"));
                    //pd.setCPF(rs.getString("cpf"));
                    pd.setDescricao(rs.getString("description"));
                    pd.setParcelas(rs.getInt("installments"));
                    pd.setIdUser(rs.getInt("id_user"));
                    pd.setStatus(rs.getString("ord.status_order"));
                    pd.setOrderPrice(rs.getDouble("ord.purchase_value"));
                    pd.getEndereco().setCep(rs.getString("cep"));
                    pd.getEndereco().setEstado(rs.getString("county"));
                    pd.getEndereco().setCidade(rs.getString("city"));
                    pd.getEndereco().setBairro(rs.getString("neighborhood"));
                    pd.getEndereco().setRuaLogradouro(rs.getString("street"));
                    pd.getEndereco().setNumeroResidencia(rs.getInt("residence_number"));
                    pd.getEndereco().setComplemento(rs.getString("complement"));
                    pd.getEndereco().setLongitude(rs.getDouble("longitude"));
                    pd.getEndereco().setLatitude(rs.getDouble("latitude"));
                    pd.setDataMarcada(rs.getString("date_schedule"));
                    pd.setHoraMarcada(rs.getString("hour_schedule"));
                    pd.setData(DataPadraoUtil.formataData(rs.getString("ord.date"), "yyyy-MM-dd", "dd-MM-yyyy"));
                    pd.getPaymentResult().setStatusPayment(rs.getString("payment_status"));
                    pedis.add(pd);
                }

                con.close();
                pst.close();

            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return pedis;
    }

    public synchronized Pedido getOrdersFromID(int idOrder) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        Pedido pd = null;
        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM orders ord ");
                sql.append("join rate_services serv ");
                sql.append("on serv.id=ord.id_rate ");
                sql.append("where DATE_FORMAT(ord.date, '%Y-%m-%d') = CURDATE() ");
                sql.append("AND id_order=? ORDER BY ord.date DESC");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idOrder);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    pd = new Pedido();
                    pd.setId(rs.getInt("id"));
                    pd.setTitle(rs.getString("title"));
                    pd.setCPF(rs.getString("cpf"));
                    pd.setDescricao(rs.getString("description"));
                    pd.setParcelas(rs.getInt("plots"));
                    pd.setIdUser(rs.getInt("id_user"));
                    pd.setStatus(rs.getString("ord.status"));
                    pd.setOrderPrice(rs.getDouble("ord.purchase_value"));
                    pd.getEndereco().setCep(rs.getString("cep"));
                    pd.getEndereco().setEstado(rs.getString("county"));
                    pd.getEndereco().setCidade(rs.getString("city"));
                    pd.getEndereco().setBairro(rs.getString("neighborhood"));
                    pd.getEndereco().setRuaLogradouro(rs.getString("street"));
                    pd.getEndereco().setNumeroResidencia(rs.getInt("residence_number"));
                    pd.getEndereco().setComplemento(rs.getString("complement"));
                    pd.getEndereco().setLongitude(rs.getDouble("longitude"));
                    pd.getEndereco().setLatitude(rs.getDouble("latitude"));

                    Date data = rs.getDate("ord.date");
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    String dataFormatada = formato.format(data);

                    pd.setData(dataFormatada);

                    return pd;
                }

                con.close();
                pst.close();

            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        return pd;
    }

    public synchronized List<ITProduct> getCartFromOrder(String idOrder, int idUser) {
        List<ITProduct> products = new LinkedList<>();
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT rt.id,rt.rate_per_czc,rate_per_credit, id_order,shop.id_product,shop.count ,prod.title, prod.per_price, prod.categorie, prod.id_flavor, prod.path_img, prod.is_weight, ");
                sql.append("flav.id, flav.flavor ");
                sql.append("FROM order_products shop ");
                sql.append("JOIN czc_products prod ");
                sql.append("ON prod.id = shop.id_product ");
                sql.append("JOIN rate_services rt ");
                sql.append("ON rt.id=(SELECT id FROM rate_services order by date DESC limit 1) ");
                sql.append("LEFT JOIN flavors flav ");
                sql.append("ON shop.id_flavor=flav.id ");
                sql.append("WHERE id_order=? AND id_user=? ");


                pst = con.prepareStatement(sql.toString());
                pst.setString(1, idOrder);
                pst.setInt(2, idUser);

                ResultSet rs = pst.executeQuery();

                ITProduct sup = null;
                //double valueBuy = 0 ;
                while (rs.next()) {
                    sup = new ITProduct();
                    sup.setId(rs.getInt("shop.id_product"));
                    sup.setTitle(rs.getString("prod.title"));
                    sup.setPerPrice(rs.getDouble("prod.per_price"));
                    sup.setCount(rs.getInt("shop.count"));
                    sup.setFlavors(rs.getInt("prod.id_flavor") > 0 ? true : false);
                    sup.setPathImg(rs.getString("prod.path_img"));
                    sup.setWeight(rs.getBoolean("prod.is_weight"));

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));
                    f.setCount(rs.getInt("shop.count"));

                    sup.setFlavor(f);

                    products.add(sup);

                    //valueBuy += sup.getPerPrice();
                }
                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ee) {
                    ee.printStackTrace();
                }
            }
        }

        return products;
    }

    public synchronized List<ITProduct> getCartFromOrderClient(Pedido order, String language) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT prod.id, prod.title, c_prod.* , o_prod.count , o_prod.value, ");
                sql.append("prod.is_weight, flav.id, flav.flavor FROM stores_orders_products o_prod ");
                sql.append("LEFT JOIN stores_products prod ON o_prod.id_product = prod.id ");
                sql.append("LEFT JOIN stores_products_flavors flav ON flav.id = prod.id_flavor ");
                sql.append("LEFT JOIN app_categories_products c_prod ON c_prod.id = prod.id_categorie ");
                sql.append("WHERE id_order=? AND id_user=? ");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, order.getId());
                pst.setInt(2, order.getIdUser());

                List<ITProduct> products = new LinkedList<>();
                ResultSet rs = pst.executeQuery();

                ITProduct sup = null;
                //double valueBuy = 0 ;
                while (rs.next()) {
                    sup = new ITProduct();
                    sup.setId(rs.getInt("prod.id"));
                    sup.setTitle(rs.getString("prod.title"));
                    sup.setPerPrice(rs.getDouble("o_prod.value"));
                    sup.setCount(rs.getInt("o_prod.count"));
                    sup.setCategorie(rs.getString(language));
                    sup.setWeight(rs.getBoolean("prod.is_weight"));

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));

                    sup.setFlavor(f);
                    products.add(sup);

                    //valueBuy += sup.getPerPrice();
                }
                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
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

    public synchronized Map<Integer, ITProduct> getAllProductsFromOrdersClient(int idUser) {
        Map<Integer, ITProduct> products = new HashMap<>();
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT rt.id,rt.rate_per_czc,rate_per_credit, id_order,shop.id_product,shop.count ,prod.title, prod.per_price, prod.categorie, prod.id_flavor, prod.path_img, prod.is_weight, ");
                sql.append("flav.id, flav.flavor ");
                sql.append("FROM order_products shop ");
                sql.append("JOIN czc_products prod ");
                sql.append("ON prod.id = shop.id_product ");
                sql.append("JOIN rate_services rt ");
                sql.append("ON rt.id=(SELECT id FROM rate_services order by date DESC limit 1) ");
                sql.append("LEFT JOIN flavors flav ");
                sql.append("ON shop.id_flavor=flav.id ");
                sql.append("WHERE id_user=? ");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idUser);

                ResultSet rs = pst.executeQuery();

                ITProduct sup = null;
                //double valueBuy = 0 ;
                while (rs.next()) {
                    sup = new ITProduct();
                    sup.setId(rs.getInt("shop.id_product"));
                    sup.setTitle(rs.getString("prod.title"));
                    sup.setPerPrice(rs.getDouble("prod.per_price"));
                    sup.setCount(rs.getInt("shop.count"));
                    sup.setFlavors(rs.getInt("prod.id_flavor") > 0 ? true : false);
                    sup.setPathImg(rs.getString("prod.path_img"));
                    sup.setWeight(rs.getBoolean("prod.is_weight"));

                    ITFlavor f = new ITFlavor();
                    f.setId(rs.getInt("flav.id"));
                    f.setFlavor(rs.getString("flav.flavor"));
                    f.setCount(rs.getInt("shop.count"));

                    sup.setFlavor(f);

                    if (products.containsKey(sup.getId())) {
                        products.get(sup.getId()).setCount(products.get(sup.getId()).getCount() + sup.getCount());
                    } else {
                        products.put(sup.getId(), sup);
                    }


                    //valueBuy += sup.getPerPrice();
                }
                pst.close();
                con.close();

                return products;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ee) {
                    ee.printStackTrace();
                }
            }
        }

        return products;
    }

    private synchronized boolean saveOrderProducts(int idUser, String idOrder, int idProduct, int count, int idFlavor) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO order_products ");
                sql.append("(id_user, id_order, id_product, count, id_flavor) ");
                sql.append("VALUES (?,?,?,?,?);");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, idUser);
                pst.setString(2, idOrder);
                pst.setInt(3, idProduct);
                pst.setInt(4, count);
                pst.setInt(5, idFlavor);
                pst.execute();

                con.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        return false;
    }

    public synchronized boolean saveOrderProducts2(int idUser, int idOrder, Collection<ITProduct> coll) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        boolean success = false;
        if (con != null) {
            List<ITProduct> products = new ArrayList<>(coll);

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO order_products ");
            sql.append("(id_user, id_order, id_product, count, id_flavor) ");
            sql.append("VALUES ");

            StringBuilder sqlContruct = new StringBuilder(sql.toString());

            int counter = 0;
            for (ITProduct p : products) {
                counter++;

                if (counter == products.size() && p.getFlavor().getFlavor() != null) {
                    sqlContruct.append("(?,?,?,?,?);");
                } else if (p.getFlavor().getFlavor() != null) {
                    sqlContruct.append("(?,?,?,?,?),\n");
                } else if (counter == products.size()) {
                    sqlContruct.append("(?,?,?,?,?);");
                } else {
                    sqlContruct.append("(?,?,?,?,?),");
                }
            }

            try {
                pst = con.prepareStatement(sqlContruct.toString());

                counter = 1;
                for (ITProduct p : products) {
                    pst.setInt(counter, idUser + 0);
                    counter++;
                    pst.setInt(counter, idOrder);
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

    public synchronized boolean cancelOrderAndPaymentAPP(Pedido order) {

        int canceled = dao.getDaoPayment().cancelPayment(""+order.getId());

        if (canceled == 1) {
            try {
                String sql = "UPDATE orders SET status=? WHERE id_user=? AND id=? ";
                Connection con = ConnectionDB_APP.connect();
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, order.getStatus());
                pst.setInt(2, order.getIdUser());
                pst.setInt(3, order.getId());
                pst.executeUpdate();

                con.close();
                pst.close();

                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }

    public synchronized boolean cancelOrderAndPaymentOnDelivery(WebResponses web) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        boolean status = false;
        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE orders SET orders.status=?");
                sql.append("WHERE orders.id=? ");
                sql.append("AND orders.id_user=? ");
                sql.append("AND datediff(orders.date_schedule, curdate()) >= 5;");

                pst = con.prepareStatement(sql.toString(), PreparedStatement.NO_GENERATED_KEYS);
                pst.setString(1, "canceled");
                pst.setString(2, web.getIdOrder());
                pst.setInt(3, web.getIdUser());
                int result = pst.executeUpdate();

                con.close();
                pst.close();

                status = result > 0 ? true : false;
                return status;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                return status;
            }
        }
        return status;
    }

    public List<ITOrderBuyer> getOrderOfDayBuyer(ITOrderBuyer order) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        List<ITOrderBuyer> orders = new ArrayList<>();

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("select ord.id, ord.id_user, ord.id_buyer, ord.status, ord.date, ord.date_schedule, ord.hour_schedule, ord.cep, ord.county, ");
                sql.append("ord.city, ord.neighborhood, ord.residence_number, ord.complement, ord.latitude, ord.longitude, ");
                sql.append("ord.purchase_value, ord.street, ord.base_distance, ord.payment_status, ord.plots, ");
                sql.append("ac.name, ac.email, ac.cel, ord.payment_local from orders ord ");
                sql.append("inner join accounts ac ");
                sql.append("on ac.id=ord.id_user ");
                sql.append("where DATE_FORMAT(ord.date, '%Y-%m-%d') = DATE_FORMAT(CURDATE(), '%Y-%m-%d') ");
                sql.append("and DATE_FORMAT(ord.date_schedule, '%Y-%m-%d') = DATE_FORMAT(CURDATE(), '%Y-%m-%d') and ord.status=? ");
                sql.append("and ord.payment_status='approved' ");
                sql.append("order by ord.date asc ;");

                pst = con.prepareStatement(sql.toString());
                pst.setString(1, order.getStatus().toLowerCase());

                ResultSet rs = pst.executeQuery();

                int count = 0;
                ITOrderBuyer orderR;
                while (rs.next()) {
                    orderR = new ITOrderBuyer();
                    orderR.setIdUser(rs.getInt("ord.id_user"));
                    orderR.setIdBuyer(rs.getInt("ord.id_buyer"));
                    orderR.setIdOrder(rs.getString("ord.id"));
                    orderR.setStatus(rs.getString("ord.status"));
                    orderR.setDate(rs.getString("ord.date").split(" ")[0]);
                    orderR.setHour(rs.getString("ord.date").split(" ")[1]);
                    orderR.setClientName(rs.getString("ac.name"));
                    orderR.setClientEmail(rs.getString("ac.email"));
                    orderR.setCep(rs.getString("ord.cep"));
                    orderR.setCounty(rs.getString("ord.county"));
                    orderR.setCity("Cidade " + rs.getString("ord.city"));
                    orderR.setCel("Cel " + rs.getString("ac.cel"));
                    orderR.setPaymentLocal(rs.getString("payment_local").equalsIgnoreCase("Na entrega") ? 1 : 0);

                    String street = rs.getString("ord.street");

                    orderR.setNeighborhood("Bairro " + rs.getString("ord.neighborhood"));
                    orderR.setStreet(street + ", N° " + rs.getString("ord.residence_number"));
                    orderR.setComplements("Complemento " + (rs.getString("ord.complement").isEmpty() ? "?" : rs.getString("ord.complement")));
                    orderR.setLatitude(rs.getDouble("ord.latitude"));
                    orderR.setLongitude(rs.getDouble("ord.longitude"));
                    orderR.setIntallment(rs.getInt("ord.plots"));
                    orderR.setPurchaseValue("R$ " + rs.getString("ord.purchase_value"));

                    if (count > 9) {
                        orders.get(count - 1).setHaveMore(true);
                        break;
                    } else {
                        orders.add(orderR);
                    }
                    count++;
                }

                pst.close();
                con.close();

                Collections.sort(orders);

                return orders;
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
        }

        return orders;
    }

    private synchronized ITOrderBuyer selectOrder(ITOrderBuyer order) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM orders ord ");
                sql.append("JOIN accounts acc ");
                sql.append("ON acc.id = ord.id_user ");
                sql.append("WHERE ord.id_user=? ");
                sql.append("AND ord.id=? ");
                sql.append("AND status=? ");

                pst = con.prepareStatement(sql.toString());
                pst.setInt(1, order.getIdUser());
                pst.setString(2, order.getIdOrder());
                pst.setString(3, order.getStatus());

                ITOrderBuyer newOrder = null;
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    newOrder = new ITOrderBuyer();
                    newOrder.setIdBuyer(rs.getInt("id_buyer"));
                    newOrder.setIdDeliveryMan(rs.getInt("id_deliveryman"));
                }

                pst.close();
                con.close();
                return newOrder;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
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

        return null;
    }

    public int updateOrderStatus(ITOrderBuyer order) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                ITOrderBuyer orderDB = selectOrder(order);
                int idDB_BUY = orderDB.getIdBuyer();
                int idDB_DEL = orderDB.getIdDeliveryMan();

                boolean isBuyer = (order.getCompanyFunction().equalsIgnoreCase("Buyer"));
                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("update orders ord join accounts acc ");
                sqlConstruct.append("on acc.id = ord.id_user ");
                sqlConstruct.append("set ord.status=?, ");
                sqlConstruct.append((isBuyer ? "ord.id_buyer=? " : "ord.id_deliveryman=? "));
                sqlConstruct.append("where ord.id=? ");
                sqlConstruct.append("and ord.id_user=? ");
                sqlConstruct.append("and acc.email=? ");

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, order.getStatus());

                if (isBuyer && idDB_BUY == order.getIdBuyer() || isBuyer && idDB_BUY == 0 && order.getIdBuyer() > 0) {
                    pst.setInt(2, order.getIdBuyer());
                } else {
                    pst.setInt(2, order.getIdDeliveryMan());
                }

                pst.setString(3, order.getIdOrder());
                pst.setInt(4, order.getIdUser());
                pst.setString(5, order.getClientEmail());

                if (isBuyer && idDB_BUY == 0 && order.getIdBuyer() > 0 ||
                        isBuyer && idDB_BUY > 0 && idDB_BUY == order.getIdBuyer()) {

                    pst.executeUpdate();
                    con.close();
                    pst.close();
                    return 2;
                } else if (isBuyer && idDB_BUY == 0 && order.getIdBuyer() > 0 ||
                        isBuyer && idDB_BUY > 0 && idDB_BUY != order.getIdBuyer()) {
                    con.close();
                    pst.close();
                    return -2;
                }

                if (isBuyer == false && idDB_DEL == 0 && order.getIdDeliveryMan() > 0 ||
                        isBuyer == false && idDB_DEL > 0 &&
                                idDB_DEL == order.getIdDeliveryMan()) {
                    pst.executeUpdate();
                    con.close();
                    pst.close();
                    return 3;
                } else if (isBuyer == false && idDB_DEL == 0 && order.getIdDeliveryMan() > 0 ||
                        isBuyer == false && idDB_DEL > 0 &&
                                idDB_DEL != order.getIdDeliveryMan()) {

                    con.close();
                    pst.close();
                    return -3;
                }
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
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

        return 0;
    }

    public boolean updateOrderStatus2(ITOrderBuyer order) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {
                boolean isBuyer = (order.getCompanyFunction().equalsIgnoreCase("Buyer"));
                StringBuilder sqlConstruct = new StringBuilder();
                sqlConstruct.append("update orders ");
                sqlConstruct.append("set status=?, hour_schedule=DATE_FORMAT(DATE_ADD(now(), INTERVAL 60 MINUTE), \"%H:%i\"), ");
                sqlConstruct.append((isBuyer ? "id_buyer=? " : "id_deliveryman=? "));
                sqlConstruct.append("where id=? ");
                sqlConstruct.append("and id_user=? ");
                sqlConstruct.append("and date_schedule=DATE_FORMAT(NOW(), '%Y-%m-%d') ");
                sqlConstruct.append("and ");
                sqlConstruct.append((isBuyer ? "(id_buyer=0 or id_buyer=?) " : "(id_deliveryman=0 or id_deliveryman=?) "));

                pst = con.prepareStatement(sqlConstruct.toString());
                pst.setString(1, order.getStatus());
                pst.setInt(2, isBuyer ? order.getIdBuyer() : order.getIdDeliveryMan());
                pst.setString(3, order.getIdOrder());
                pst.setInt(4, order.getIdUser());
                pst.setInt(5, isBuyer ? order.getIdBuyer() : order.getIdDeliveryMan());
                int linesAffecteds = pst.executeUpdate();

                if(linesAffecteds > 0){
                    pst.close();
                    con.close();
                    return true ;
                }


                pst.close();
                con.close();
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                    pst.close();
                    con.close();
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

    public boolean updateOrderStatusDeliveryMan(ITOrderBuyer order) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                StringBuilder sql = new StringBuilder();
                sql.append("update orders ord join accounts acc ");
                sql.append("on acc.id = ord.id_user ");
                sql.append("set ord.status=? ");
                sql.append("where ord.id=? ");
                sql.append("and ord.id_user=? ");
                sql.append("and acc.email=? ");
                sql.append("and ord.id_deliveryman=0 or ord.id_deliveryman=? ");


                pst = con.prepareStatement(sql.toString());
                pst.setString(1, order.getStatus());
                pst.setString(2, order.getIdOrder());
                pst.setInt(3, order.getIdUser());
                pst.setString(4, order.getClientEmail());
                pst.setInt(5, order.getIdBuyer());
                ResultSet rs = pst.executeQuery();

                if (rs.rowUpdated()) {

                    con.close();
                    pst.close();
                    return true;
                }

                con.close();
                pst.close();
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
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

        return false;
    }

    public Pedido getStatusOrderFromID(Pedido order) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {
            try {

                String sql = "select * from stores_orders where id=? and id_user=? and status_order=? order by date desc";

                pst = con.prepareStatement(sql);
                pst.setInt(1, order.getId());
                pst.setInt(2, order.getIdUser());
                pst.setString(3, order.getStatus());
                ResultSet rs = pst.executeQuery();

                Pedido resultOrder = null;
                if (rs.next()) {
                    resultOrder = new Pedido();
                    resultOrder.setId(rs.getInt("id"));
                    resultOrder.setIdUser(rs.getInt("id_user"));
                    resultOrder.setIdBuyer(rs.getInt("id_buyer"));
                    resultOrder.setIdDeliveryMan(rs.getInt("id_deliveryman"));
                    resultOrder.setStatus(rs.getString("status_order"));

                }

                pst.close();
                con.close();

                return resultOrder;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
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

        return null;
    }

    public boolean verifyOrderAvailable(ITOrderBuyer order) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM orders ");
        sql.append("WHERE id=? ");
        sql.append("AND id_user=? ");
        sql.append("AND DATE_FORMAT(date, '%Y-%m-%d')=CURDATE() ");
        sql.append("AND (id_deliveryman=0 or id_deliveryman=?) ");
        sql.append("AND (id_buyer=0 or id_buyer=?) ");
        sql.append("AND status=? ");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, order.getIdOrder());
                pst.setInt(2, order.getIdUser());
                pst.setInt(3, order.getIdDeliveryMan());
                pst.setInt(4, order.getIdBuyer());
                pst.setString(5, order.getStatus());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    con.close();
                    pst.close();
                    return true;
                }
                con.close();
                pst.close();

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException ex1) {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return false;
    }

    public boolean isDelivered(Endereco endereco) {

        boolean delivered = false;
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM operation_neighborhood ");
            sql.append("WHERE uf=? AND county=? AND city LIKE '%");
            sql.append(endereco.getCidade());
            sql.append("%' ");
            sql.append("AND neighborhood LIKE '%");
            sql.append(endereco.getBairro());
            sql.append("%' ");
            sql.append("AND is_active=1 ");

            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, endereco.getUf());
                pst.setString(2, endereco.getEstado());

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    delivered = true;
                }

                con.close();
                pst.close();

                return delivered;
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return delivered;
    }

    private boolean updatePurchaseValueOrder(Pedido order, int countProduct) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT purchase_value, fee_delivery, plots FROM orders WHERE id=? ";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, order.getId());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    RateServices rate = UtilPrices.getLastRate();

                    int parcels = rs.getInt("plots");
                    double feeDelivery = rs.getDouble("fee_delivery");
                    double purchaseDB = rs.getDouble("purchase_value");
                    double priceProductFee = UtilPrices.calculateRates(rate.getRateComprazemCasa(), rate.getRatePercentualCredit(), (order.getValorCompra() * countProduct));

                    if (parcels > 1) {
                        // pegar o valor carrinnho com taxas
                        double valueCompraComTaxas = UtilPrices.calculateRates(rate.getRateComprazemCasa(), rate.getRatePercentualCredit(), getSumAllProductsFromOrder(order.getId()));

                        if (valueCompraComTaxas < 100) parcels = 1;
                        // UtilPrices.calculateFinal
                        purchaseDB = UtilPrices.calculateValueFinal(order, new HashSet<>(), parcels);
                    } else {
                        purchaseDB += priceProductFee;
                    }

                    con.close();
                    pst.close();

                    sql = "UPDATE orders SET purchase_value=? WHERE id=? ";

                    con = ConnectionDB_APP.connect();
                    pst = con.prepareStatement(sql);
                    pst.setDouble(1, UtilPrices.formatPrice(purchaseDB));
                    pst.setInt(2, order.getId());
                    pst.executeUpdate();

                    con.close();
                    pst.close();

                    return true;
                }

            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);

                    con.close();
                    pst.close();
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

    public double getSumAllProductsFromOrder(int idOrder) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        String sql = "SELECT sum(sh.count * czc.per_price) total FROM order_products sh join czc_products czc on sh.id_product=czc.id WHERE  id_order=? ;";

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, idOrder);
                ResultSet rs = pst.executeQuery();

                double sum = 0;
                if (rs.next()) {

                    sum = rs.getDouble("total");
                }
                con.close();
                pst.close();
                return sum;
            } catch (SQLException ex) {
                Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);
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

        return -1;
    }

    public boolean deleteProductFromOrder(ITProduct productDEL) {
        Connection con = ConnectionDB_APP.connect();

        String sql = "DELETE FROM order_products WHERE id_order=? and id_product=? ";

        PreparedStatement pst = null;

        if (con != null) {
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, productDEL.getOrderID());
                pst.setInt(2, productDEL.getId());
                pst.execute();

                con.close();
                pst.close();

                Pedido order = getOrdersFromID(Integer.valueOf(productDEL.getOrderID()));
                //updatePurchaseValueOrder(productDEL.getOrderID(), -productDEL.getPerPrice(), productDEL.getCount());
                updatePurchaseValueOrder(order, productDEL.getCount());
                dao.getDaoProducts().disableProduct(productDEL);
                return true;
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);

                    con.close();
                    pst.close();
                    return false;
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

    private Pedido isOrderDuplicate(Pedido order) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("select * from orders where DATE_FORMAT(date_schedule, '%Y-%m-%d')=? ");
        sql.append("and status = 'wait' ");
        sql.append("and id_user=? ");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, order.getDataMarcada());
                pst.setInt(2, order.getIdUser());

                ResultSet rs = pst.executeQuery();

                Pedido returnOrder;
                if (rs.next()) {

                    returnOrder = new Pedido();
                    returnOrder.setParcelas(rs.getInt("plots"));
                    returnOrder.setStatus(String.valueOf(rs.getDouble("purchase_value")));
                    returnOrder.setPaymentMode(rs.getString("payment_method"));
                    con.close();
                    pst.close();
                    return returnOrder;
                }
            } catch (SQLException e) {
                try {
                    e.printStackTrace();
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        }

        return null;
    }

    private Pedido isOrderDuplicate(int idUser, String dateSchedule) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sql = new StringBuilder();
        sql.append("select * from orders where DATE_FORMAT(date_schedule, '%Y-%m-%d')=? ");
        sql.append("and status = 'wait' ");
        sql.append("and id_user=? ");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, dateSchedule);
                pst.setInt(2, idUser);

                ResultSet rs = pst.executeQuery();

                Pedido returnOrder;
                if (rs.next()) {

                    returnOrder = new Pedido();
                    returnOrder.setParcelas(rs.getInt("plots"));
                    returnOrder.setStatus(String.valueOf(rs.getDouble("purchase_value")));
                    returnOrder.setPaymentMode(rs.getString("payment_method"));
                    con.close();
                    pst.close();
                    return returnOrder;
                }
            } catch (SQLException e) {
                try {
                    e.printStackTrace();
                    con.close();
                    pst.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                return null;
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

    public void bloquedMakeWish(ServerConfig config) {
        dao.setConfig(config);
    }

    public ServerConfig getConfigureOrderManager() {
        return dao.getConfig();
    }

    private void saveAllFlavorFromProduct(ITProduct product, Pedido order) {
        StringBuilder sqlConstruct = new StringBuilder();
        sqlConstruct.append("INSERT INTO order_products  ");
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

    private int getCountOrders() {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst;

        if (con != null) {

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT count(*) c FROM orders ");
            sql.append("where (DATE_FORMAT(date, '%Y-%m-%d') = DATE_FORMAT(CURDATE(), '%Y-%m-%d') ");
            sql.append("and isempty(date_schedule))");
            sql.append("or (DATE_FORMAT(date_schedule, '%Y-%m-%d') = DATE_FORMAT(CURDATE(), '%Y-%m-%d'))");

            try {
                pst = con.prepareStatement(sql.toString());
                ResultSet rs = pst.executeQuery();

                int countOrders = 0;
                if (rs.next()) {
                    countOrders = rs.getInt("c");
                }
                return countOrders;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return 0;
    }

    private boolean checkOperationNeighborhood(String neighborhood){
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){
            String sql = "SELECT ";

            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, neighborhood);

                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    con.close();
                    pst.close();

                    return true;
                }

                con.close();
                pst.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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

        return false ;
    }

    public int checkMakeWishAvailable(int idUser, int parcels, String dateSchedule,String cep){
        System.out.println("CEP: "+cep);
        // Verifica se estamos recebendo pedido
        if (dao.getConfig().isReleaseMakeWish()) {
            // Verifica nosso limite de pedido diário
            if (getCountOrders() < dao.getConfig().getLimitOrders()) {
                //if (idCoupons.size() == coupons.size()) {
                if (parcels <= 5) {
                    Calendar d = Calendar.getInstance(Locale.getDefault());

                    int hourA = d.get(Calendar.HOUR_OF_DAY);
                    int minutesA = d.get(Calendar.MINUTE);
                    int secoundsA = d.get(Calendar.SECOND);

                    int dayA = d.get(Calendar.DAY_OF_MONTH);
                    int monthA = d.get(Calendar.MONTH) + 1;
                    int yearA = d.get(Calendar.YEAR);

                    int diaAbertura = dao.getConfig().getOpeningDate().getDay();
                    int mesAbertura = dao.getConfig().getOpeningDate().getMonth();
                    int anoAbertura = dao.getConfig().getOpeningDate().getYear();

                    int dayActual = DataPadraoUtil.getDayOfWeek();
                    int startTimeCfig = dao.getConfig().getOpeningHour().getStartHour();
                    int finalTimeCfig = dao.getConfig().getOpeningHour().getFinalHour();

                    int startMinute = dao.getConfig().getOpeningHour().getStartMinute();
                    int finalMinute = dao.getConfig().getOpeningHour().getFinalMinute();

                    boolean isOpeningDay = dayA == diaAbertura && monthA == mesAbertura && yearA == anoAbertura;
                    boolean passedInauguration = dayA >= diaAbertura && monthA >= mesAbertura && yearA >= anoAbertura
                            || dayA >= 1 && monthA >= 1 && yearA > anoAbertura;

                    // Verifica se é dia da abertura ou se passou a data de inauguração
                    if (isOpeningDay || passedInauguration) {
                        // Verifica se tem pedido dupplicado ou se já fez pedido para o dia
                        if (isOrderDuplicate(idUser, dateSchedule) == null) {

                            // Verifica se operamos no bairro requisitado
                            if(new DaoAddress().verifyOperationNeighborhood(cep) != null){
                                try {
                                    // Verifica se esta detro do horário de funcionamento
                                    if (((hourA >= startTimeCfig && minutesA >= startMinute) && (hourA <= finalTimeCfig && minutesA <= finalMinute))
                                            || !DataPadraoUtil.dateCompare("yyyy-MM-dd",DataPadraoUtil.getCurrentDate("yyyy-MM-dd"),dateSchedule)
                                            || ((hourA >= startTimeCfig) && (hourA < finalTimeCfig))
                                            || (hourA == finalTimeCfig && minutesA <= finalMinute)){
                                        return 99;
                                    } else {
                                        return 200;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                return 100;
                            }
                        } else {
                            return 600;
                        }
                    } else {
                        return 300;
                    }
                } else {
                    return -10;
                }
            } else {
                return 400;
            }
        } else {
            return 0;
        }
        return 0 ;
    }

    public int checkMakeWishAvailable(Map<Object, Object> map){

        String dateSchedule = map.get("date_schedule").toString();

        int parcels = Integer.valueOf(map.get("parcels").toString());
        int idUser = Integer.valueOf(map.get("id_user").toString());

        // verifica se ta liberado para fazer pedido
        if (dao.getConfig().isReleaseMakeWish()) {
            // verifica o limite de pedidos da configuração
            if (getCountOrders() < dao.getConfig().getLimitOrders()) {
                //if (idCoupons.size() == coupons.size()) {
                    if (parcels <= 5) {
                        Calendar d = Calendar.getInstance(Locale.getDefault());

                        int hourA = d.get(Calendar.HOUR_OF_DAY);
                        int minutesA = d.get(Calendar.MINUTE);
                        int secoundsA = d.get(Calendar.SECOND);

                        int dayA = d.get(Calendar.DAY_OF_MONTH);
                        int monthA = d.get(Calendar.MONTH) + 1;
                        int yearA = d.get(Calendar.YEAR);

                        int diaAbertura = dao.getConfig().getOpeningDate().getDay();
                        int mesAbertura = dao.getConfig().getOpeningDate().getMonth();
                        int anoAbertura = dao.getConfig().getOpeningDate().getYear();

                        int dayActual = DataPadraoUtil.getDayOfWeek();
                        int startTimeCfig = dao.getConfig().getOpeningHour().getStartHour();
                        int finalTimeCfig = dao.getConfig().getOpeningHour().getFinalHour();

                        int startMinute = dao.getConfig().getOpeningHour().getStartMinute();
                        int finalMinute = dao.getConfig().getOpeningHour().getFinalMinute();

                        boolean isOpeningDay = dayA == diaAbertura && monthA == mesAbertura && yearA == anoAbertura;
                        boolean passedInauguration = dayA >= diaAbertura && monthA >= mesAbertura && yearA >= anoAbertura
                                || (dayA >= 1 && monthA >= 1 && yearA > anoAbertura || monthA >= mesAbertura && yearA >= anoAbertura);

                        // verifica se esta na data de abertura ou se passou
                        if (isOpeningDay || passedInauguration) {

                            // verifica se tem pedido duplicado
                            if (isOrderDuplicate(idUser, dateSchedule) == null) {

                                // Verifica o bairro de operação
                                if(new DaoAddress().verifyOperationNeighborhood(map.get("cep").toString()) != null){
                                    try {
                                        // Verifica se esta detro do horário de funcionamento
                                        if (((hourA >= startTimeCfig && minutesA >= startMinute) && (hourA <= finalTimeCfig && minutesA <= finalMinute))
                                                || !DataPadraoUtil.dateCompare("yyyy-MM-dd",DataPadraoUtil.getCurrentDate("yyyy-MM-dd"),dateSchedule)
                                                || ((hourA >= startTimeCfig) && (hourA < finalTimeCfig))
                                                || (hourA == finalTimeCfig && minutesA <= finalMinute)){
                                            return 99;
                                        } else {
                                            return 200;
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    return 100;
                                }
                            } else {
                                return 600;
                            }
                        } else {
                            return 300;
                        }
                    } else {
                        return -10;
                    }
            } else {
                return 400;
            }
        } else {
            return 0;
        }
        return 0 ;
    }

    public boolean checkMakeWishAvailable(){
        if (dao.getConfig().isReleaseMakeWish()) {
            if (getCountOrders() < dao.getConfig().getLimitOrders()) {
                //if (idCoupons.size() == coupons.size()) {
                    Calendar d = Calendar.getInstance(Locale.getDefault());

                int hourA = d.get(Calendar.HOUR_OF_DAY);
                int minutesA = d.get(Calendar.MINUTE);
                int secoundsA = d.get(Calendar.SECOND);

                int dayA = d.get(Calendar.DAY_OF_MONTH);
                int monthA = d.get(Calendar.MONTH) + 1;
                int yearA = d.get(Calendar.YEAR);

                int diaAbertura = dao.getConfig().getOpeningDate().getDay();
                int mesAbertura = dao.getConfig().getOpeningDate().getMonth();
                int anoAbertura = dao.getConfig().getOpeningDate().getYear();

                int dayActual = DataPadraoUtil.getDayOfWeek();
                int startTimeCfig = dao.getConfig().getOpeningHour().getStartHour();
                int finalTimeCfig = dao.getConfig().getOpeningHour().getFinalHour();

                int startMinute = dao.getConfig().getOpeningHour().getStartMinute();
                int finalMinute = dao.getConfig().getOpeningHour().getFinalMinute();

                boolean isOpeningDay = dayA == diaAbertura && monthA == mesAbertura && yearA == anoAbertura;
                boolean passedInauguration = dayA >= diaAbertura && monthA >= mesAbertura && yearA >= anoAbertura
                        || dayA >= 1 && monthA >= 1 && yearA > anoAbertura;

                    if (isOpeningDay || passedInauguration) {

                        // Verifica se esta no horario de funcionamento

                        if (((hourA >= startTimeCfig && minutesA >= startMinute) && (hourA <= finalTimeCfig && minutesA <= finalMinute))
                                || ((hourA >= startTimeCfig) && (hourA < finalTimeCfig))
                                || (hourA == finalTimeCfig && minutesA <= finalMinute)){
                            return true;
                        } else {
                        }
                    } else {
                    }
            } else {
            }
        } else {
        }

        return false;
    }

    public boolean checkExistOrder(String idOrder, int idBuyer, double value) {
        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM orders ");
        sb.append("WHERE id=? AND  id_buyer=? AND status='in_production' ");
        sb.append("AND purchase_value=? ");

        if(con != null){
            try {
                pst = con.prepareStatement(sb.toString());
                pst.setString(1,idOrder);
                pst.setInt(2, idBuyer);
                pst.setDouble(3, value);
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

    public boolean updateDataPayment(ITOrderValidator validator) {
        Connection con = DBConnections.connect();
        PreparedStatement pst = null;

        // idTransaction, paymentStatus

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE stores_orders SET id_transaction=?, payment_status=? ");
        sql.append("WHERE id_user=? AND id_store=? AND id=? ;");

        if (con != null) {
            try {
                pst = con.prepareStatement(sql.toString());
                pst.setString(1, String.valueOf(validator.getIdTransaction()));
                pst.setString(2, validator.getStatusPayment());
                pst.setInt(3, validator.getIdUser());
                pst.setInt(4, validator.getIdStore());
                pst.setInt(5, validator.getIdOrder());

                int linesAffecteds = 0 ;
                try {
                    linesAffecteds = pst.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e){
                    con.close();
                    pst.close();
                    return e.getMessage().contains("Duplicate")?true:false;
                }
                con.close();
                pst.close();

                return (linesAffecteds > 0);
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(DaoOrder.class.getName()).log(Level.SEVERE, null, ex);

                    con.close();
                    pst.close();
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
        return false ;
    }
}