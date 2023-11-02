/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.controllers;

import apis.encrypt.AES;
import br.com.comprazencasa.collaborator.CollaboratorFunction;
import br.com.comprazencasa.collaborator.GroupOrders;
import br.com.comprazencasa.collaborator.ITOrderBuyer;
import br.com.comprazencasa.database.DaoOrder;
import br.com.comprazencasa.database.DaoPayment;
import br.com.comprazencasa.itens.ITAvailableHour;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.market.Pedido;
import br.com.comprazencasa.user.ContaUsuario;
import br.com.comprazencasa.util.ServerConfig;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * REST Web Service
 *
 * @author Marlon
 */
@RestController
@RequestMapping("Order")
public class OrderManager {
    /**
     * Creates a new instance of OrderManager
     */
    @Autowired
    public OrderManager() { }
    
    @PostMapping(path = "/make-a-wish")
    @Secured({"USER","ADMIN","COLL"})
    public String makeWish(@RequestBody WebResponses web){

        Pedido order = new Gson().fromJson(web.getJsonObject(), Pedido.class);

        String estado = order.getEndereco().getEstado();

        if(estado.contains("Cidade/Município"))
        estado = estado.substring(0,estado.indexOf("Cidade/Município")+16);

        String city = order.getEndereco().getCidade();

        if(city.contains("Cidade/Município "))
            city = city.substring(city.indexOf("Cidade/Município "));

        String bairro = order.getEndereco().getBairro();

        if(bairro.contains("Bairro "))
            bairro = bairro.replace("Bairro ","");

        order.getEndereco().setEstado(estado);
        order.getEndereco().setCidade(city);
        order.getEndereco().setBairro(bairro);

        int resultCode = new DaoOrder().makeWish(order);
        WebResponses response = new WebResponses();
        response.setComunication(resultCode);

        Gson gson = new Gson();

        if(resultCode == 300 || resultCode == 500 || resultCode == 200){
            response.setJsonObject(gson.toJson(new DaoOrder().getConfigureOrderManager()));
        }

        return gson.toJson(response) ;
    }

    @PostMapping(path = "/release-make-whish")
    @Secured({"USER","ADMIN","COLL"})
    public String releaseMakeWish(@RequestBody WebResponses web){

        boolean isRelease = new DaoOrder().checkMakeWishAvailable();

        WebResponses response = new WebResponses();
        response.setComunication(isRelease?800:0);

        return new Gson().toJson(response) ;
    }

    @PostMapping(path = "/schedule-order")
    public String schedulerOrder(@RequestBody WebResponses web){

        Pedido order = new Gson().fromJson(web.getJsonObject(), Pedido.class);

        int resultCode = 0;
        if(order.getDataMarcada() != null && !order.getDataMarcada().isEmpty() && web.getComunication() == 1){
            resultCode = new DaoOrder().makeWishScheduleOrder(order);
        } else{
            resultCode = new DaoOrder().makeWishReScheduleOrder(order.getIdUser(), order.getId(),order.getDataMarcada(), order.getHoraMarcada());
        }

        WebResponses response = new WebResponses();
        response.setComunication(resultCode);

        Gson gson = new Gson();

        if(resultCode == 300 || resultCode == 500 || resultCode == 200){
            response.setJsonObject(gson.toJson(new DaoOrder().getConfigureOrderManager()));
        }

        return gson.toJson(response) ;
    }

    @PostMapping(path = "/get-times-unavailables")
    public String getAllHousAvailableFromDate(@RequestBody WebResponses web){

        Gson gson = new Gson();

        List<ITAvailableHour> unavailableTimes = null;//new DaoOrder().getIndisponibleHoursFromDate(web.getJsonObject());
        WebResponses response = new WebResponses();
        response.setComunication(700);
        response.setJsonObject(gson.toJson(unavailableTimes));

        return gson.toJson(response);
    }

    @PostMapping(path = "/get-orders-from-user")
    public String getOrdersFromUser(@RequestBody String jsonAESObject) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(jsonAESObject);;
        ContaUsuario account = new Gson().fromJson(json, ContaUsuario.class);
        ArrayList<Pedido> list = new DaoOrder().getOrdersFromUserID(account.getId());

        return AES.encryptToAESJson(list);
    }

    @PostMapping(path = "/cancel-order")
    public String cancelMyOrders(@RequestBody WebResponses web){
        boolean canceled = new DaoOrder().cancelOrderAndPaymentOnDelivery(web);

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(canceled?1:0);
        return gson.toJson(response);
    }

    @PostMapping(path = "/get-my-orders-historic")
    public String getMyOrdersHistoric(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(request);;
        ContaUsuario account = new Gson().fromJson(json, ContaUsuario.class);

        LinkedList<Pedido> list = new DaoOrder().getOrdersHistoricFromUserID(account.getId());

        return AES.encryptToAESJson(list);
    }

    @PostMapping(path = "/cancel-order-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelOrderAdmin(String json){

        /*long startTime = System.nanoTime();
        Pedido order = gson.fromJson(json, Pedido.class);
        boolean statusOperation = dao.cancelOrder(order);

        long estimatedTime = System.nanoTime() - startTime;

        System.out.println(TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS));

        WebResponses response = new WebResponses();
        response.setComunication(1);*/
        return new Gson().toJson(null);
    }
    
    @PostMapping(path = "/get-cart-origin")
    @Secured({"ADMIN","COLL"})
    @PostAuthorize("hasAnyRole('ADMIN','COLL')")
    public String getProductsFromOrder(@RequestBody WebResponses web){

        List<ITProduct> cart = new DaoOrder().getCartFromOrder(web.getIdOrder(),web.getIdUser());

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(cart.isEmpty()?0:1);
        response.setJsonObject(gson.toJson(cart));
        return gson.toJson(response);
    }
    
    @PostMapping("get-my-cart")
    public String getProductsFromOrderMyCart(@RequestBody WebResponses web){
        Pedido pedido = new Gson().fromJson(web.getJsonObject(), Pedido.class);
        List<ITProduct> cart = new DaoOrder().getCartFromOrderClient(pedido,"br");

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(1);
        response.setJsonObject(gson.toJson(cart));
        return gson.toJson(response);
    }

    @PostMapping(path = "/get-order-day")
    @Secured({"ADMIN","COLL"})
    @PostAuthorize("hasAnyRole('ADMIN','COLL')")
    public String getOrdersOfTheDay(@RequestBody ITOrderBuyer order){

        Gson gson = new Gson();

        List<ITOrderBuyer> list = new DaoOrder().getOrderOfDayBuyer(order);
        WebResponses response = new WebResponses();
        response.setComunication(list.size()>=1?1:0);
        response.setJsonObject(gson.toJson(list));
        return gson.toJson(response);
    } 
    
    @PostMapping(path = "/verify-order-available")
    @PostAuthorize("hasAnyRole('ADMIN','COLL')")
    public String verifyOrderFree(@RequestBody ITOrderBuyer order){
        boolean resultRequest = new DaoOrder().verifyOrderAvailable(order);
        
        WebResponses response = new WebResponses();
        response.setComunication(resultRequest?1:0);

        return new Gson().toJson(response);
    } 
    
    @PostMapping(path = "/get-status-order")
    public String getStatusOrder(@RequestBody String msgCrypt) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(msgCrypt);
        Pedido order = new Gson().fromJson(json, Pedido.class);
        Pedido orderResult = new DaoOrder().getStatusOrderFromID(order);

       if(orderResult != null && order.getStatus().equals(ITOrderBuyer.STATUS_FOR_DELIVERY)
                && orderResult.getIdDeliveryMan() == 0
                || orderResult != null && orderResult.getIdDeliveryMan() == order.getIdDeliveryMan()
                && orderResult.getIdDeliveryMan() != 0){
             
            ITOrderBuyer ord = new ITOrderBuyer();
            ord.setIdOrder(""+order.getId());
            ord.setIdUser(order.getIdUser());
            ord.setStatus(order.getStatus());
            ord.setCompanyFunction(CollaboratorFunction.DELIVERY_MAN);
           new DaoOrder().updateOrderStatus(ord);
        }
        
        WebResponses response = new WebResponses();
        response.setIdOrder(""+order.getId());
        response.setIdUser(orderResult.getIdUser());
        response.setIdBuyer(orderResult.getIdBuyer());
        response.setIdDeliveryMan(orderResult.getIdDeliveryMan());
        response.setMessage(orderResult.getStatus());
        response.setComunication(2); 
        
        return AES.encryptToAESJson(response);
    } 

    @PostMapping(path = "/update-all-orders")
    @PostAuthorize("hasAnyRole('ADMIN','COLL')")
    public String updateAllOrder(@RequestBody WebResponses droid){
        Gson gson = new Gson();

        GroupOrders group = gson.fromJson(droid.getJsonObject(), GroupOrders.class);
        boolean status = false ;

        WebResponses response = new WebResponses();
        for (ITOrderBuyer o : group) {
            status = new DaoOrder().updateOrderStatus2(o);

            if(o.getStatus().equals(ITOrderBuyer.STATUS_FOR_DELIVERY)){
                new DaoPayment().moneyTransferer(o);
            }
        }
        response.setComunication(status?2:0);
        response.setIdBuyer(droid.getIdBuyer());
        response.setIdDeliveryMan(droid.getIdDeliveryMan());
        response.setMessage(group.get(0).getStatus());
        response.setJsonObject(gson.toJson(status));

        return gson.toJson(response);
    } 
    
    @PostMapping(path = "/update-order")
    @PostAuthorize("hasAnyRole('ADMIN','COLL')")
    public String updateOrder(@RequestBody ITOrderBuyer order){

        boolean status = new DaoOrder().updateOrderStatus2(order);

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(status?1:0);
        response.setIdBuyer(order.getIdBuyer());
        response.setIdDeliveryMan(order.getIdDeliveryMan());
        response.setMessage(order.getStatus());
        response.setIdOrder(order.getIdOrder());
        response.setJsonObject(gson.toJson(status));
        return gson.toJson(response);
    } 
    
    @PostMapping(path = "/delete-product-from-order")
    @PostAuthorize("hasAnyRole('ADMIN','COLL')")
    public String deleteProductFromOrder(@RequestBody WebResponses web){

        ITProduct productDEL = new Gson().fromJson(web.getJsonObject(), ITProduct.class);

        boolean rec = new DaoOrder().deleteProductFromOrder(productDEL);

        WebResponses response = new WebResponses();
        response.setComunication(rec?1:0);

        return new Gson().toJson(response) ;
    }

    @PostMapping(path = "/release-make-wish")
    @PostAuthorize("hasAnyRole('ADMIN','COLL')")
    public String releaseMakeWish(@RequestBody ServerConfig config){

        new DaoOrder().bloquedMakeWish(config);

        WebResponses response = new WebResponses();
        response.setComunication(10);
        return new Gson().toJson(response) ;
    }

    @PostMapping(path = "/check-make-order")
    public String checkMakeWishAvailable(@RequestBody Map<Object, Object> map){
        WebResponses response = new WebResponses();
        response.setComunication(new DaoOrder().checkMakeWishAvailable(map));
        response.setJsonObject(new Gson().toJson(new DaoOrder().getConfigureOrderManager()));

        return new Gson().toJson(response);
    }


}
