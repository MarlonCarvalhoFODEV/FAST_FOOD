package br.com.hotburguer.controllers;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import apis.onesignal.OneSignalResponse;
import apis.pagarme.system.CryptographSys;
import br.com.comprazencasa.collaborator.GroupOrders;
import br.com.comprazencasa.collaborator.ITOrderBuyer;
import br.com.comprazencasa.database.DaoOrder;
import br.com.comprazencasa.database.DaoPayment;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.comprazencasa.util.WebResponses;
import br.com.hotburguer.daos.*;
import br.com.hotburguer.itens.*;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("hot-order")
public class HotOrdersManager {

    @PostMapping(path = "/get-order")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getOrder(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        HotUser user = new Gson().fromJson(json, HotUser.class);

        HotOrder order = new DaoHotOrder().getOrder(user);

        ITOrderValidator validator = new ITOrderValidator(order!=null?order.getId():0, 0,0,30);
        validator.setBlocked(order==null);
        validator.setFeeInstalment(order.getFeeInstallment());
        validator.setFeeDelivery(order.getFeeDelivery());

        RSA.Keys pKey = new DaoHotCryptograph().getCryptographDefault();
        RSA rsa = new RSA(pKey.getPublicKey(), pKey.getPrivateKey(),2048);

        return rsa.encryptToJson(validator);
    }

    @PostMapping(path = "/new-order")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String insertOrder(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        HotOrder order = new Gson().fromJson(json, HotOrder.class);

        order = new DaoHotOrder().insertOrder(order);
        String status = order.getId()>0?"STATUS_CREATED":"ERRO_CREATE_ORDER";

        HotOrder horder = new HotOrder();
        horder.setStatus(status);
        horder.setId(order.getId());
        horder.setIdUser(order.getIdUser());
        horder.setIdDevice(order.getIdDevice());

        ITOrderValidator validator = new ITOrderValidator(order!=null?order.getId():0, 0,0,30);
        validator.setBlocked(order==null);
        validator.setFeeInstalment(order.getFeeInstallment());
        validator.setFeeDelivery(order.getFeeDelivery());
        validator.setIdUser(horder.getIdUser());
        validator.setIdDevice(horder.getIdDevice());

        RSA.Keys pKey = new DaoHotCryptograph().getCryptographDefault();
        RSA rsa = new RSA(pKey.getPublicKey(), pKey.getPrivateKey(),2048);

        horder.setValidator(rsa.encryptToJson(validator));

        return AES.encryptToAESJson(horder);
    }

    @PostMapping(path = "/update-status-order")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String updateStatusOrder(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        HotOrder order = new Gson().fromJson(json, HotOrder.class);

        boolean inserted = new DaoHotOrder().updateStatusOrder(order);
        String status = inserted?"STATUS_UPDATED":"ERRO_UPDATE_STATUS";

        order = new HotOrder();
        order.setStatus(status);
        return AES.encryptToAESJson(order);
    }

    @PostMapping(path = "/get-status-order")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getStatusOrder(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        HotOrder order = new Gson().fromJson(json, HotOrder.class);

        HotOrder responseOrder = new DaoHotOrder().getStatusOrder(order);

        return AES.encryptToAESJson(responseOrder);
    }

    @PostMapping(path = "/confirm-delivery")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String confirmDelivery(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        HotOrder order = new Gson().fromJson(json, HotOrder.class);

        boolean updated = new DaoHotOrder().updateStatusOrder(order);

        if(updated)
            order.setStatus(order.getStatus());
        else
            order.setStatus("ERRO");

        return AES.encryptToAESJson(order);
    }

    @PostMapping(path = "/check-is-delivery-local")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String checkIsDelivery(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        String country = c.has("cy") ? c.getString("cy") : null;
        String state = c.has("st") ? c.getString("st") : null;
        String city = c.has("ct") ? c.getString("ct") : null;
        String neighborhood = c.has("nd") ? c.getString("nd") : null;
        String cep = c.has("cp") ? c.getString("cp") : null;
        String uf = c.has("uf") ? c.getString("uf") : null;
        boolean haveCoordinates = c.has("co") ? c.getBoolean("co") : false;

        LAddress address = new LAddress();
        address.setCEP(cep);
        address.setContry(country);
        address.setState(state);
        address.setCity(city);
        address.setNeighborhood(neighborhood);
        address.setUF(uf);

        HotDayOperation day = new DaoHotOrder().checkMakeWishAvailable(address, haveCoordinates);

        return AES.encryptToAESJson(day);
    }

    @PostMapping(path = "/update-all-orders")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String updateAllOrder(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Gson gson = new Gson();

        byte[] json = AES.decrypt(new Gson().fromJson(request, byte[].class));
        GroupOrders group = new Gson().fromJson(new String(json), GroupOrders.class);

        boolean status = false ;

        WebResponses response = new WebResponses();
        for (ITOrderBuyer o : group) {
            status = new DaoOrder().updateOrderStatus2(o);

            if(o.getStatus().equals(ITOrderBuyer.STATUS_FOR_DELIVERY)){
                new DaoPayment().moneyTransferer(o);
            }
        }


        return gson.toJson(response);
    }

    @GetMapping(path = "/get-fee-delivery")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getFeeDelivery() throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        HotConfigurations config = new DaoHotManager().getHotConfiguration();

        return AES.encryptToAESJson(config);
    }

    @PostMapping(path = "/get-orders")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getOrders(@RequestBody String request, HttpServletRequest est) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String hash = c.has("hash") ? c.getString("hash") : null;
        String publicIp = est.getHeader("X-FORWARDED-FOR");


        DaoHotOrder daoOrder = new DaoHotOrder();
        Map<Integer,HotOrderManager> orders = daoOrder.getOrders(idManager, idDevice, hash);

        int ordersSize = orders.size();
        HotGroupOrders group = null ;
        ArrayList<HotGroupOrders> groups = new ArrayList<>();
        for (int i = 0; i <= ordersSize; i++) {
            // Grupo criado
            group = new DaoHotOrder().createGroupOrders2(new ArrayList<>(orders.values()), 3);

            if(group != null){
                groups.add(group);
                orders.values().removeAll(group);
                i=0;
                ordersSize = orders.size();

            } else break ;
        }


        return AES.encryptToAESJson(groups);
    }

    @PostMapping(path = "/start-production")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    @Secured({"ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String startProduction(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String hash = c.has("hash") ? c.getString("hash") : null;
        JSONArray ids = c.has("id_ords")?c.getJSONArray("id_ords"):null;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        boolean result = new DaoHotOrder().updateStatusOrderManager(idManager, idDevice,null,
                ids,HotOrder.Status.IN_PRODUCTION);

        ArrayList<String> idsClients = new DaoHotOrder().getIdsUsersFromOrdersIdsManager(idManager,idDevice,ids);

        String title = "Seu Pedido";
        String message = "Está sendo preparado...";
        OneSignalResponse responseNotify =
                new DaoHotNotification().notifyClient(title, message,message, idsClients,null);

        JSONObject response = new JSONObject();
        response.put("result",result);
        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/get-orders-prompt-delivery")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getOrdersPromptDelivery(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        int idOrder = c.has("id_order") ? c.getInt("id_order") : 0;
        String hash = c.has("hash") ? c.getString("hash") : null;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        DaoHotOrder daoOrder = new DaoHotOrder();
        Map<Integer,HotOrderManager> orders = daoOrder.getOrdersPromptDelivery(idManager, idDevice, hash);

        int ordersSize = orders.size();
        HotGroupOrders group = null ;
        ArrayList<HotGroupOrders> groups = new ArrayList<>();
        for (int i = 0; i <= ordersSize; i++) {
            // Grupo criado
            group = new DaoHotOrder().createGroupOrders2(new ArrayList<>(orders.values()), 3);
            if(group != null){
                groups.add(group);
                orders.values().removeAll(group);
                i=0;
                ordersSize = orders.size();

            } else break ;
        }
        return AES.encryptToAESJson(groups);
    }

    @PostMapping(path = "/check-products-available")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String checkProductsAvailables(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        JSONArray idProducts = c.has("id_prods") ? c.getJSONArray("id_prods") : null;
        String hash = c.has("hash") ? c.getString("hash") : null;
        String publicIp = hrequest.getHeader("X-FORWARDED-FOR");

        Map<String, Boolean> unavailables = new DaoHotOrder().checkProductsAvailables(idUser,idDevice, idProducts);

        return AES.encryptToAESJson(unavailables);
    }
}
