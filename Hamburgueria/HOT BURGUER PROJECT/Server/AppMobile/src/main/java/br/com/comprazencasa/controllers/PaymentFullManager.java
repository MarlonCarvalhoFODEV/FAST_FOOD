package br.com.comprazencasa.controllers;

import apis.encrypt.AES;
import apis.encrypt.Cryptograph;
import apis.encrypt.RSA;
import apis.pagarme.system.SystemConfigurations;
import apis.pagarme.transaction.TransactionsManager;
import apis.pagarme.userbuyer.*;
import br.com.comprazencasa.database.*;
import br.com.comprazencasa.itens.ITOrderValidator;
import br.com.comprazencasa.itens.trasaction.ITTransaction;
import br.com.comprazencasa.user.ContaUsuario;
import br.com.comprazencasa.util.PaymentResult;
import com.google.gson.Gson;
import me.pagar.model.Card;
import me.pagar.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import apis.pagarme.transaction.Payment;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@RestController
@RequestMapping("pgm-payment")
public class PaymentFullManager {

    @Autowired
    public PaymentFullManager(){

    }

    @PostMapping(path = "/get-cards-saved")
    public String getCardsSaveds(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(request);

        UserBuyer bu = new Gson().fromJson(json, UserBuyer.class);

        ArrayList<BCard> cards = new DaoPagarme().getCards().getAllCard(bu.getId());

        return AES.encryptToAESJson(cards);
    }

    @GetMapping(path = "/get-card-saved/{iduserjsoncrypted}")
    public byte[] getCardSaved(@PathVariable("iduserjsoncrypted") String iduserjsoncrypted) throws Exception {

        PrivateKey pKey = new DaoSystemConfiguration().getCryptograph().getRsa().getPrivateKey();

        BCard card = new DaoPagarme().getCards().getCard(iduserjsoncrypted);
        RSA rsa = new RSA(null, pKey);

        return rsa.encrypt(new Gson().toJson(card));
    }

    @PostMapping(path = "/save-card")
    public boolean saveCard(@RequestBody String cardJsonCrypted) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(cardJsonCrypted);
        UserBuyer bu = new Gson().fromJson(json, UserBuyer.class);

        boolean result = new DaoPagarme().getCards().createCard(bu);

        return result ;
    }

    @PostMapping(path = "/create-payment")
    public String createPayment(@RequestBody String buyerJsonAES) throws IOException, ClassNotFoundException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        Gson gson = new Gson();
        PrivateKey pKey = new DaoSystemConfiguration().getCryptograph().getRsa().getPrivateKey();

        String buyerJson = AES.decryptToJson(buyerJsonAES);
        UserBuyer buyer = gson.fromJson(buyerJson, UserBuyer.class);

        String cardJson = new RSA(null, pKey,1024).decrypt(buyer.getInfSecurity());
        buyer.setCard(gson.fromJson(cardJson, BCard.class));

        int idStore = buyer.getIdStore();
        int idProduct = buyer.getProducts().get(0).getId();

        SystemConfigurations sysConfig = new SystemConfigurations();
        sysConfig.setIdStoreRecipient(new DaoStores().getIdRecipient(idStore, idProduct));
        sysConfig.setPaymentSys(new DaoSystemConfiguration().getPaymentConfig());

        byte[] keyOrder = buyer.getKeyOrder();
        String json = new RSA(null, pKey,2048).decrypt(keyOrder);

        ITOrderValidator validator = new Gson().fromJson(json, ITOrderValidator.class);

        Payment payment = new Payment();

        if(!validator.isBlocked()){

            buyer.getAddressDefault().setFeeDelivery((int) (validator.getFeeDelivery()*100));
            buyer.setFeeInstallment(validator.getFeeInstalment());
            Transaction resultPayment = new DaoPagarme()
                    .getTransactions()
                    .createSplitTransaction(buyer, sysConfig);

            if(resultPayment != null){
                payment.setId(resultPayment.getId());
                payment.setStatus(TransactionsManager.Status.getStatusFrom(resultPayment.getStatus().name()));
                payment.setMsgAlert(TransactionsManager.Status.getMsgAlert(resultPayment.getStatus().name()));
                payment.setMsgComplement(TransactionsManager.Status.getMessageFrom(resultPayment.getStatus().name()));
            } else {
                payment.setId(0);
                payment.setStatus(3);
                //payment.setMsgAlert();
                //payment.setMsgComplement(TransactionsManager.Status.getMessageFrom(resultPayment.getStatus().name()));
            }

            if(!validator.expiration() && resultPayment != null){
                validator.setIdStore(buyer.getIdStore());
                validator.setIdUser(buyer.getId());
                validator.setIdTransaction(resultPayment.getId());
                validator.setStatusPayment(resultPayment.getStatus().name());
                new DaoOrder().updateDataPayment(validator);
            }
        } else {
            payment.setBlocked(validator.isBlocked());
        }

        return AES.encryptToAESJson(payment) ;
    }
}
