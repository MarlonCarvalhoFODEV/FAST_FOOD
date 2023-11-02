package br.com.hotburguer.controllers;

import apis.date.DateManager;
import apis.encrypt.AES;
import apis.encrypt.RSA;
import apis.pagarme.system.CryptographSys;
import apis.pagarme.userbuyer.BAddress;
import apis.pagarme.userbuyer.BDocument;
import apis.pagarme.userbuyer.BPhone;
import apis.pagarme.userbuyer.UserBuyer;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.hotburguer.daos.DaoHotCryptograph;
import br.com.hotburguer.daos.DaoHotProduct;
import br.com.hotburguer.daos.DaoPagarme;
import br.com.hotburguer.itens.HotUser;
import br.com.hotburguer.daos.DaoHotUsers;
import com.google.gson.Gson;
import me.pagar.model.Customer;
import org.json.JSONObject;
import org.springframework.http.MediaType;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("hot-users")
public class HotUserManager {

    @PostMapping(path = "/new-user")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String insertUser(@RequestBody String request, HttpServletRequest hrequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, ParseException, IOException {
        Gson gson = new Gson();

        RSA.Keys crypto = new DaoHotCryptograph().getCryptographDefault();
        RSA rsa = new RSA(crypto.getPublicKey(), crypto.getPrivateKey(), 2048);

        String json = rsa.decrypt(gson.fromJson(request, byte[].class));
        Map<String, String> user = gson.fromJson(json, HashMap.class);

        HotUser h = new HotUser();
        h.setEmail(user.get("email"));
        h.setName(user.get("name"));
        h.setUrlPhoto(user.get("photo"));
        h.setDeviceID(user.get("id_android"));
        h.setSocialLogin(user.get("social_media"));
        String ip = hrequest.getHeader("X-FORWARDED-FOR");

        DaoHotUsers dao = new DaoHotUsers();

        HotUser selected = dao.selectUser(h, ip);

        if(selected == null)
            selected = dao.insertAccount(h, ip);

        JSONObject response = new JSONObject();

        if(selected != null && selected.getId() != -1){
            response.put("result",selected.getId());
            response.put("id_customer", selected.getIdCustomer());
            response.put("secret",ip);
        }
        else
            response.put("device_registered",true);

        return AES.encryptToAESJson(response);
    }

   /* @PostMapping(path = "/user-login")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String selectUser(@RequestBody String request, HttpServletRequest hRequest) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        HotUser user = new Gson().fromJson(json, HotUser.class);

        String ip = hRequest.getHeader("X-FORWARDED-FOR");

        HotUser userLogin = new DaoHotUsers().selectUser(user, ip);

        return AES.encryptToAESJson(userLogin);
    }*/

    @PostMapping(path = "/update-user")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String updateUser(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, ParseException, IOException {
        Gson gson = new Gson();

        RSA.Keys crypto = new DaoHotCryptograph().getCryptographDefault();
        RSA rsa = new RSA(crypto.getPublicKey(), crypto.getPrivateKey(), 2048);

        String json = rsa.decrypt(gson.fromJson(request, byte[].class));
        Map<String, Object> userData = gson.fromJson(json, HashMap.class);

        double idd = (double) userData.get("id");

        HotUser user = new HotUser();
        user.setId((int) idd);
        user.setEmail((String) userData.get("em"));
        user.setName((String) userData.get("n"));
        user.setCpf((String) userData.get("cpf"));
        user.setBirthOfDate((String) userData.get("dtn"));
        user.getContact().getPhone().setFullNumber((String) userData.get("cl"));

        if (user != null) {

            UserBuyer buyer = new UserBuyer();
            buyer.setId(user.getId());
            buyer.setBirthday(user.getBirthOfDate());
            buyer.setName(user.getName());
            buyer.getContact().setEmail(user.getEmail());
            buyer.getDocument().setCPF(user.getCpf());
            buyer.getDocument().setType(BDocument.Type.CPF);

            BAddress add = new BAddress();
            //add.setCity(user.getAddress().getCity());
            add.setCountry("Brasil");
            /*add.setNeighborhood(user.getAddress().getNeighborhood());
            add.setState(user.getAddress().getState());
            add.setStreet(user.getAddress().getStreet());
            add.setStreetNumber(String.valueOf(user.getAddress().getNumber()));
            add.setZipCode(user.getAddress().getCEP());*/

            buyer.setDefaultAddress(add);
            buyer.getContact().getPhone().setDdi(55);
            buyer.getContact().getPhone().setDDD(user.getContact().getPhone().getDDD());
            buyer.getContact().getPhone().setNumber(user.getContact().getPhone().getNumber());
            buyer.getContact().getPhone().setType(BPhone.Type.CEL);

            if (buyer.getBirthday() != null && !buyer.getBirthday().isEmpty()) {
                buyer.setBirthday(
                        DateManager.format
                                ("dd-MM-yyyy",
                                        "yyyy-MM-dd",
                                        buyer.getBirthday()));

            }

            Customer customer = new DaoPagarme().getCustomer().createCustomer(buyer);
            if(customer != null){
                user.setIdCustomer(customer.getId());

                boolean updated = new DaoHotUsers().completeRegister(user);
                user.setStatus(updated);
            }
        }

        return AES.encryptToAESJson(user);
    }

    @GetMapping(path = "/get-image-profile/{idUser}", produces = MediaType.IMAGE_JPEG_VALUE)
    public synchronized @ResponseBody byte[] getImage(@PathVariable int idUser) {
        return new DaoHotUsers().getImage(idUser);
    }
}
