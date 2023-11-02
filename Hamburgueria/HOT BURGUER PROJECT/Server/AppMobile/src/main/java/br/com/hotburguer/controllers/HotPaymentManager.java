package br.com.hotburguer.controllers;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import apis.pagarme.system.CryptographSys;
import apis.pagarme.transaction.Payment;
import apis.pagarme.transaction.TransactionsManager;
import apis.pagarme.userbuyer.BCard;
import apis.pagarme.userbuyer.BProduct;
import apis.pagarme.userbuyer.UserBuyer;
import br.com.hotburguer.daos.*;
import br.com.hotburguer.itens.HotUser;
import br.com.hotburguer.itens.ITOrderValidator;
import com.google.gson.Gson;
import me.pagar.model.Transaction;
import org.json.JSONObject;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("hot-payment")
public class HotPaymentManager {

    @GetMapping(path = "/get-public-key")
    @Produces("application/json;charset=utf-8")
    public String generatePublicKey() throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String publicKey =  new DaoPagarme().getCards().getPublicKeyToEncrypt();

        return AES.encryptToAESJson(publicKey);
    }

    @PostMapping(path = "/save-card")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String saveCard(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        RSA.Keys crypto = new DaoHotCryptograph().getCryptographPayment();
        RSA rsa = new RSA(crypto.getPublicKey(), crypto.getPrivateKey(), 2048);

        String json = rsa.decrypt(new Gson().fromJson(request, byte[].class));
        Map<String, Object> map = new Gson().fromJson(json, HashMap.class);

        double idCustomer = (double) map.get("i");
        String cardHash = String.valueOf(map.get("h"));

        boolean result = new DaoPagarme().getCards().createCard((int) idCustomer,cardHash);

        JSONObject obj = new JSONObject();
        obj.put("card_result_save", result);

        return AES.encryptToAESJson(obj.toString());
    }

    @PostMapping(path = "/get-cards-saved")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getCardsSaved(@RequestBody String request, HttpServletRequest hRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        RSA.Keys crypto = new DaoHotCryptograph().getCryptographPayment();
        RSA rsa = new RSA(crypto.getPublicKey(), crypto.getPrivateKey(), 2048);

        String json = rsa.decrypt(new Gson().fromJson(request, byte[].class));
        Map<String, String> map = new Gson().fromJson(json, HashMap.class);

        HotUser hotUser = new HotUser();
        hotUser.setId(Integer.valueOf(map.get("id_user")));
        hotUser.setEmail(map.get("email"));
        hotUser.setName(map.get("name"));
        hotUser.setDeviceID(map.get("device_id"));
        hotUser.setIdCustomer(Integer.valueOf(map.get("id_customer")));
        String secret = map.get("secret");

        String ip = hRequest.getHeader("X-FORWARDED-FOR");

        HotUser dbUser = new DaoHotUsers().selectUser(hotUser, ip,secret);
        DaoPagarme dao = new DaoPagarme();
        String extID = dao.getCustomer().getCustomer(hotUser.getIdCustomer()).getExternalId();

        if(dbUser != null && dbUser.getDeviceID().equals(hotUser.getDeviceID()) && extID != null &&
                Integer.valueOf(extID) == hotUser.getId() && dbUser.getEmail().equals(hotUser.getEmail())
                && dbUser.getIdCustomer() == hotUser.getIdCustomer() && hotUser.getId() == dbUser.getId()){

            ArrayList<BCard> cards = dao.getCards().getAllCard(hotUser.getIdCustomer());

            return AES.encryptToAESJson(cards);
        } else {

        }

        return  AES.encryptToAESJson(new ArrayList<>());
    }


    @PostMapping(path = "/create-payment")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String createPayment(@RequestBody String buyerJsonAES) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographPayment().getPrivateKey();

        byte[] json = AES.decrypt(new Gson().fromJson(buyerJsonAES, byte[].class));
        UserBuyer buyer = new Gson().fromJson(new String(json), UserBuyer.class);

        byte[] keyOrder = buyer.getKeyOrder();

        String validatorJson = new RSA(null, pKey, 2048).decryptFromJson(new String(keyOrder));

        ITOrderValidator validator = new Gson().fromJson(validatorJson, ITOrderValidator.class);

        Payment payment = new Payment();

        if (!validator.isBlocked()) {

            buyer.getAddressDefault().setFeeDelivery((int) (validator.getFeeDelivery() * 100));
            buyer.setFeeInstallment(validator.getFeeInstalment());

            Transaction resultPayment = new DaoPagarme()
                    .getTransactions()
                    .createTransactionCapture(buyer);

            if (resultPayment != null) {
                payment.setId(resultPayment.getId());
                payment.setStatus(TransactionsManager.Status.getStatusFrom(resultPayment.getStatus().name()));
                payment.setMsgAlert(TransactionsManager.Status.getMsgAlert(resultPayment.getStatus().name()));
                payment.setMsgComplement(TransactionsManager.Status.getMessageFrom(resultPayment.getStatus().name()));

                if(payment.getStatus() != Payment.PAID && payment.getStatus() != Payment.PROCESSING &&
                  payment.getStatus() != Payment.AUTHORIZED)
                    new DaoHotOrder().deleteOrder(validator);
                else {
                    String title = "Novo Pedido" ;
                    String message = "Mais um cliente na hot fome" ;

                    new DaoHotNotification().notifyManager(title, message, message, 1,null);
                    new DaoHotOrder().reduceQuantityProduct(validator.getIdUser(),validator.getIdDevice(),buyer.getProducts());
                }
            } else {
                payment.setId(0);
                payment.setStatus(3);
                //payment.setMsgAlert();
                //payment.setMsgComplement(TransactionsManager.Status.getMessageFrom(resultPayment.getStatus().name()));
            }

            if (!validator.expiration() && resultPayment != null) {
                validator.setIdStore(buyer.getIdStore());
                validator.setIdUser(buyer.getId());
                validator.setIdTransaction(resultPayment.getId());
                validator.setStatusPayment(resultPayment.getStatus().name());
                new DaoHotOrder().updateStatusPayment(validator);
            }
        }
        return AES.encryptToAESJson(payment);
    }
}
