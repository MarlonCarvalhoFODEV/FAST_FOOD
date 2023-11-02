/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import br.com.comprazencasa.collaborator.ITOrderBuyer;
import br.com.comprazencasa.util.RateServices;
import br.com.comprazencasa.util.UtilPrices;
import br.com.comprazencasa.util.WebJson;
import com.google.gson.Gson;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marlon
 */
public class DaoPayment {

    private float equacaoPrimeiroGrau(int parcel, double valTot, double valInitial) {
        double valX = valTot / (1 + (UtilPrices.decodeParcels(parcel)));
        double percentToValX = (((valX * 100) / valInitial) - 100) / 100;
        return Float.valueOf(String.valueOf(UtilPrices.formatPrice((valInitial + (valInitial * percentToValX)))));
    }

    private double getPercentualFromNumber(double n1, double n2) {
        return (((n2 * 100) / n1) / 100);
    }

    public Preference doPaymentAndroid(Map<String, Object> infs) {
        try {

            initiMercadoPagoOperation();

            //int quantity = (int) infs.get("item_quantity");
            int parcels = Integer.valueOf(infs.get("installments").toString());
            Item item = new Item();
            item.setId((String) infs.get("item_id"))
                    .setTitle((String) infs.get("item_title"))
                    .setDescription(((String) infs.get("item_description")))
                    .setQuantity(1)
                    .setCurrencyId("BRL")
                    .setCategoryId("Products")
                    .setUnitPrice(
                            parcels > 1 ?
                                    equacaoPrimeiroGrau(parcels, Double.valueOf(infs.get("item_amount").toString()), Double.valueOf(infs.get("item_value").toString()))
                                    : Float.valueOf(infs.get("item_amount").toString()))
                    //.setUnitPrice(Float.valueOf(String.valueOf(UtilPrices.formatPrice(Double.valueOf(infs.get("item_amount").toString())))))
                    .setPictureUrl("https://www.comprazencasa.com.br/AppMobile/Image/get-others-image/24");


            //System.out.println(ajustPaymemt(Double.valueOf(infs.get("item_amount").toString()), Double.valueOf(infs.get("item_value").toString()),parcels));

            SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat hour = new SimpleDateFormat("hh:mm:ss");

            Date d = Calendar.getInstance().getTime();

            StringBuilder dataComplete = new StringBuilder(date.format(d));
            dataComplete.append("T");
            dataComplete.append(hour.format(d));

            String name = ((String) infs.get("payer_name")).split(" ")[0];
            String surName = null;
            if (name.split(" ").length > 1)
                surName = ((String) infs.get("payer_name")).split(" ")[1];

            Payer payer = new Payer();

            if (surName != null && !surName.isEmpty()) payer.setSurname(surName);

            payer.setName(name);
            payer.setEmail((String) infs.get("payer_email"));
            payer.setDateCreated(dataComplete.toString());
            payer.setAddress(new Address()
                    .setStreetName((String) infs.get("payer_street"))
                    .setZipCode((String) infs.get("payer_zip_code")));
            payer.setPhone(new Phone()
                    .setAreaCode("21")
                    .setNumber((String) infs.get("payer_tel_cel")));
            payer.setIdentification(new Identification()
                    .setType("CPF")
                    .setNumber((String) infs.get("payer_cpf")));

            PaymentMethods paymentMethods = new PaymentMethods();
            paymentMethods.setDefaultInstallments(parcels);
            paymentMethods.setExcludedPaymentTypes("atm", "ticket", "prepaid_card");
            paymentMethods.setInstallments(parcels);

            Preference preference = new Preference();
            preference.setPayer(payer);
            preference.appendItem(item);
            preference.setPaymentMethods(paymentMethods);
            preference = preference.save();
            return preference;
        } catch (MPException ex) {
            Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Payment doPaymentAndroidFee(Map<String, Object> infs) {
        try {
            MercadoPago.SDK.setClientId(System.getenv("8307365280321886"));
            MercadoPago.SDK.setClientSecret(System.getenv("WROmlPVsfgwvAR7m0iuF7Mpa6QHE4gbj"));
            MercadoPago.SDK.configure("APP_USR-8307365280321886-102019-575d97d018dc91f18ee42c8d0da96761-301727568");
            MercadoPago.SDK.setAccessToken("APP_USR-8307365280321886-102019-575d97d018dc91f18ee42c8d0da96761-301727568");

            //int quantity = (int) infs.get("item_quantity");
            Item item = new Item();
            item.setId((String) infs.get("item_id"))
                    .setTitle((String) infs.get("item_title"))
                    .setDescription(((String) infs.get("item_description")))
                    .setQuantity(1)
                    .setCurrencyId("BRL")
                    .setCategoryId("Products")
                    .setUnitPrice(Float.valueOf(infs.get("item_amount").toString()));
            //.setPictureUrl("");

            com.mercadopago.resources.datastructures.payment.Payer newPayer = new com.mercadopago.resources.datastructures.payment.Payer();
            newPayer.setFirstName(((String) infs.get("payer_name")).split(" ")[0])
                    .setLastName(((String) infs.get("payer_name")).split(" ")[1])
                    .setEmail((String) infs.get("payer_email"))
                    .setAddress(new com.mercadopago.resources.datastructures.payment.Address()
                            .setStreetName((String) infs.get("payer_street"))
                            .setZipCode((String) infs.get("payer_zip_code")));

            Payment payment = new Payment();
            payment.setTransactionAmount(Float.valueOf(infs.get("item_amount").toString()))
                    .setToken("APP_USR-3668301829553636-120617-43f9c458f880379b4b23183af122d5fa-384436224")
                    .setDescription("Title of what you are paying for")
                    .setInstallments(1)
                    .setPaymentMethodId("master")
                    .setPayer(newPayer)
                    .setApplicationFee(Float.valueOf(1));

            return payment.save();
        } catch (MPException ex) {
            Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int cancelPayment(String paymentId) {
        try {
            Payment payment = null;
            try {
                payment = Payment.findById(paymentId);
            } catch (NullPointerException n) {
                return 0;
            }
            payment.setStatus(Payment.Status.cancelled);
            payment.update();
            return 1;
        } catch (MPException ex) {
            Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int refundPayment(String paymentId, float val) {

        try {
            Payment payment = Payment.findById(paymentId);
            // JSONObject ff = payment.get ("/v1/payments/[ID]");
        } catch (MPException ex) {
            Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public String moneyTransferer(ITOrderBuyer o){

        boolean collExist = new DaoCollaborators().checkExistCollaborator(o.getIdBuyer(), o.getEmailBuyer(), o.getCompanyFunction());
        boolean orderExist = new DaoOrder().checkExistOrder(o.getIdOrder(), o.getIdBuyer(), Double.valueOf(o.getPurchaseValue()));

        if(collExist && orderExist){

        }

        JSONObject ob = new JSONObject();
        ob.put("transaction_amount",o.getPurchaseValue());
        ob.put("operation_type","money_transfer");
        ob.put("description","Compra do Cliente");
        ob.put("installments",o.getIntallment());
        ob.put("marketplace","NONE");
        ob.put("payment_method_id","account_money");

        JSONObject collector = new JSONObject();
        collector.put("email",o.getEmailBuyer());

        ob.put("collector",collector);
        ob.put("external_reference",o.getIdBuyer());
        ob.put("notification_url","");

        HttpURLConnection conn = null ;
        BufferedReader br = null ;

        StringBuilder sb = new StringBuilder();
        sb.append("https://api.mercadopago.com/v1/payments?access_token=");
        sb.append("APP_USR-8307365280321886-050717-2f50bbc686e95ff53e22adfdf9a894f0-301727568");

        try {
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(new Gson().toJson(ob).getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            StringBuilder response = new StringBuilder();

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            os.close();
            conn.disconnect();
            br.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();

            conn.disconnect();
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null ;
    }

   /* public List<Refund> getRefunds(String paymentId){
        try {
            return Payment.findById(paymentId).getRefunds();
        } catch (MPException ex) {
            Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  null ;
    }*/

    public void receiverNotification(String idTrasaction) throws MPException {
        if (idTrasaction != null && !idTrasaction.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("https://api.mercadopago.com/v1/payments/");
            sb.append(idTrasaction);
            sb.append("?access_token=");
            sb.append(initiMercadoPagoOperation());

            String jsonRough = new WebJson().getJson(sb.toString(), WebJson.AUTH_SERVER);

            updateStatusPayment(idTrasaction, new JSONObject(jsonRough));
        }
    }

    private boolean updateStatusPayment(String idTransaction, JSONObject json) {

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null;
        try {
            if (con != null) {

                String sql = "UPDATE orders SET status=? where id_transaction=? ";

                pst = con.prepareStatement(sql);

                pst.setString(1, json.getString("status"));
                pst.setString(2, idTransaction);
                int linesAffecteds = pst.executeUpdate();

                con.close();
                pst.close();

                return linesAffecteds>0?true:false ;
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public void receiverNotification(JSONObject json) {
        int id = json.getInt("id");
        boolean liveMode = json.getBoolean("live_mode");
        String type = json.getString("type");
        String dateCreated = json.getString("date_created");
        int applicationId = json.getInt("application_id");
        int userId = json.getInt("user_id");
        int version = json.getInt("version");
        String apiVersion = json.getString("action");
        JSONObject data = json.getJSONObject("data");
        int idData = data.getInt("id");

        System.out.println("ID: " + id);
        System.out.println("LIVE_MODE: " + liveMode);
        System.out.println("TYPE: " + type);
        System.out.println("DATE_CREATED: " + dateCreated);
        System.out.println("APPLICATION_ID: " + applicationId);
        System.out.println("USER_ID: " + userId);
        System.out.println("VERSION: " + version);
        System.out.println("API_VERSION: " + apiVersion);
        System.out.println("ID_DATA: " + idData);

        switch (type) {
            case "payment":

                break;
            case "plan":

                break;
            case "subscription":

                break;
            case "invoice":

                break;
        }
    }

    public boolean insertRateService(RateServices rates) {

        try {
            String sql = "INSERT INTO rate_services (rate_per_czc, rate_per_credit) "
                    + "VALUES (?,?)";

            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDouble(1, rates.getRateComprazemCasa());
            pst.setDouble(2, rates.getRatePercentualCredit());

            pst.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public RateServices getRateServices() {

        try {
            String sql = "SELECT * FROM rate_services ORDER BY date DESC";
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            RateServices rate = null;
            if (rs.next()) {
                rate = new RateServices();
                rate.setRateComprazemCasa(rs.getDouble("rate_per_czc"));
                rate.setRatePercentualCredit(rs.getDouble("rate_per_credit"));
            }
            return rate;
        } catch (SQLException ex) {
            Logger.getLogger(DaoPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String initiMercadoPagoOperation() throws MPException {
        String ACESS_TOKEN = "APP_USR-8307365280321886-050717-2f50bbc686e95ff53e22adfdf9a894f0-301727568";
        MercadoPago.SDK.setClientId(System.getenv("8307365280321886"));
        MercadoPago.SDK.setClientSecret(System.getenv("WROmlPVsfgwvAR7m0iuF7Mpa6QHE4gbj"));
        MercadoPago.SDK.setAccessToken("APP_USR-8307365280321886-102019-575d97d018dc91f18ee42c8d0da96761-301727568");
        MercadoPago.SDK.configure("APP_USR-8307365280321886-050717-2f50bbc686e95ff53e22adfdf9a894f0-301727568");
        return ACESS_TOKEN;
    }
}
