package br.com.comprazencasa.controllers;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import apis.pagarme.system.CryptographSys;
import apis.pagarme.system.PaymentSys;
import br.com.comprazencasa.database.*;
import br.com.comprazencasa.itens.*;
import br.com.comprazencasa.market.Pedido;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import me.pagar.model.Recipient;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.Produces;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("stores")
public class StoresManager {

    @PostMapping(path = "/create-store")
    public String createStore(@RequestBody String objectInJson) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        String json = AES.decryptToJson(objectInJson);
        ITStore store = new Gson().fromJson(json, ITStore.class);

        DaoStores stores = new DaoStores();
        DaoPagarme pagarme = new DaoPagarme();

        PaymentSys configPaySys = new DaoSystemConfiguration().getPaymentConfig();
        Recipient recipientCreated = pagarme.getRecipient().createRecipient(configPaySys.getRecipient(), store.getStoreConfiguration().getAccountBank());

        ITStore storeResponse = new ITStore();
        if(recipientCreated != null){
            store.setIdRecipient(recipientCreated.getId());
            store = stores.createStore(store);

            storeResponse.setId(store.getId());
        }

        return AES.encryptToAESJson(storeResponse);
    }

    @PostMapping(path = "/get-store-famous")
    public String getStoreFamous(@RequestBody String objectInJson) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        String json = AES.decryptToJson(objectInJson);
        ITStore store = new Gson().fromJson(json, ITStore.class);

        ArrayList<ITStore> stores = new DaoStores().getStoreFamous(store, store.getIdRequisitor() == 0);

        return AES.encryptToAESJson(stores);
    }

    @PostMapping(path = "/get-store-more-sales")
    public String getStoreMoreSales(@RequestBody String AESInJson) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {

        String json = AES.decryptToJson(AESInJson);
        ITStore store = new Gson().fromJson(json, ITStore.class);

        ArrayList<ITStore> stores = new DaoStores().getStoreMoreSales(store, store.getIdRequisitor() > 0);
        return AES.encryptToAESJson(stores);
    }

    @PostMapping(path = "/get-store-campaing")
    public  String getStoreCampaings(@RequestBody String storeJsonAES) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(storeJsonAES);
        ITStore store = new Gson().fromJson(json, ITStore.class);

        ArrayList<ITCampaing> campaings = new DaoStores().getStoreCampaings(store, store.getIdRequisitor() == 0);
        return AES.encryptToAESJson(campaings);
    }

    @PostMapping(path = "/get-categories")
    public synchronized String getStoreCategories(@RequestBody String storeJsonAES) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(storeJsonAES);
        ITStore store = new Gson().fromJson(json, ITStore.class);
        ArrayList<String> products = new DaoStores().getStoreCategories(store.getId(),"br");

        return AES.encryptToAESJson(products);
    }

    @PostMapping(path = "/get-products")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public synchronized String getStoreProducts(@RequestBody String storeJsonAES) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(storeJsonAES);
        ITProduct product = new Gson().fromJson(json, ITProduct.class);
        ArrayList<ITProduct> products = new DaoStores().getProducts1(product);

        return AES.encryptToAESJson(products);
    }

    @PostMapping(path = "/favorite-store")
    public synchronized final String favoriteStore(@RequestBody String jsonAESStore) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(jsonAESStore);
        ITStore store = new Gson().fromJson(json, ITStore.class);

        return AES.encryptToAESJson(new DaoStores().favorite(store));
    }

    @PostMapping(path = "/get-available-hours-from-date")
    public String getAllHousAvailableFromDate(@RequestBody String jsonAESStore) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(jsonAESStore);
        ITAvailableHour availableHour = new Gson().fromJson(json, ITAvailableHour.class);

        int idStore = availableHour.getIdStore();
        String date = availableHour.getDateRequisition();

        ArrayList<ITAvailableHour> avals = new DaoStores().getAvailableHoursToSchedule(idStore, date);

        return AES.encryptToAESJson(avals);
    }

    @PostMapping(path = "/make-order")
    @Secured({"USER", "ADMIN", "COLL"})
    public String storeMakeWish(@RequestBody String orderJsonAES) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException, ClassNotFoundException {

        String json = AES.decryptToJson(orderJsonAES);
        Pedido order = new Gson().fromJson(json, Pedido.class);

        String estado = order.getEndereco().getEstado();

        if (estado.contains("Cidade/Município"))
            estado = estado.substring(0, estado.indexOf("Cidade/Município") + 16);

        String city = order.getEndereco().getCidade();

        if (city != null && city.contains("Cidade/Município "))
            city = city.substring(city.indexOf("Cidade/Município "));

        String bairro = order.getEndereco().getBairro();

        if (bairro.contains("Bairro "))
            bairro = bairro.replace("Bairro ", "");

        order.getEndereco().setEstado(estado);
        order.getEndereco().setCidade(city);
        order.getEndereco().setBairro(bairro);

        Pedido result = new DaoStores().createOrderIntoStore(order);

        ITOrderValidator validator = new ITOrderValidator(result!=null?result.getId():0, 0,0,30);
        validator.setBlocked(result==null);
        validator.setFeeInstalment(order.getFeeInstallment());
        validator.setFeeDelivery(order.getFeeDelivery());

        if(validator.isBlocked())
            new DaoUser().ban(order.getIdUser(),"Tentativa de Burlar o Sistema");

        CryptographSys crypto = new DaoSystemConfiguration().getCryptograph();
        RSA rsa = new RSA(crypto.getRsa().getPublicKey(), crypto.getRsa().getPrivateKey(),2048);

        return rsa.encryptToJson(validator);
    }

    @PostMapping(path = "/scheduler-order")
    public String schedulerOrder(@RequestBody String orderJsonAES) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {

        byte[] json = AES.decrypt(new Gson().fromJson(orderJsonAES, byte[].class));
        Pedido order = new Gson().fromJson(new String(json), Pedido.class);

        int resultCode = new DaoOrder().makeWishReScheduleOrder(order.getIdUser(), order.getId(), order.getDataMarcada(), order.getHoraMarcada());

        WebResponses response = new WebResponses();
        response.setComunication(resultCode);

        Gson gson = new Gson();

        if (resultCode == 300 || resultCode == 500 || resultCode == 200) {
            response.setJsonObject(gson.toJson(new DaoOrder().getConfigureOrderManager()));
        }

        return gson.toJson(response);
    }

    @PostMapping(path = "/check-is-delivery-order")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public synchronized String checkIsDeliveryOrder(@RequestBody String orderJsonAES) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        byte[] json = AES.decrypt(new Gson().fromJson(orderJsonAES, byte[].class));
        Pedido order = new Gson().fromJson(new String(json), Pedido.class);
        ITDayOperation day = new DaoStores().checkMakeWishAvailable(order);

        return AES.encryptToAESJson(day);
    }

    @GetMapping(path = "/get-logo/{storeID}", produces = MediaType.IMAGE_JPEG_VALUE)
    public synchronized @ResponseBody byte[] getStoreLogo(@PathVariable int storeID) {
        return new DaoStores().getLogo(storeID);
    }

    @GetMapping(path = "/get-background/{storeID}", produces = MediaType.IMAGE_JPEG_VALUE)
    public synchronized @ResponseBody
    byte[] getStoreBackground(@PathVariable int storeID) {
        return new DaoStores().getBackground(storeID);
    }

    @GetMapping(path = "/get-img-slider/{campaigID}", produces = MediaType.IMAGE_JPEG_VALUE)
    public synchronized @ResponseBody
    byte[] getImageCampaingStore(@PathVariable int campaigID) {
        return new DaoStores().getImageCampaing(campaigID);
    }

    @GetMapping(path = "/get-image/{idProduct}", produces = MediaType.IMAGE_JPEG_VALUE)
    public synchronized @ResponseBody byte[] getImageProductStore(@PathVariable int idProduct) {
        return new DaoStores().getImageProduct(idProduct);
    }

}
