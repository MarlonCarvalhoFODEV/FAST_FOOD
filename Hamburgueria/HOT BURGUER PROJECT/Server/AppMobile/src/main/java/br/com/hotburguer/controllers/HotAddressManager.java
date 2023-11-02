package br.com.hotburguer.controllers;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import br.com.comprazencasa.geo.Street;
import br.com.hotburguer.daos.DaoHotAddress;
import br.com.hotburguer.daos.DaoHotCryptograph;
import br.com.hotburguer.itens.HotUser;
import br.com.hotburguer.itens.LAddress;
import br.com.hotburguer.itens.HotReuseAddress;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.text.ParseException;
import java.util.ArrayList;

@RestController
@RequestMapping("hot-address")
public class HotAddressManager {

    @PostMapping(path = "/get-address-from-cep")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getAddressFromCep(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        String cep = c.has("cep") ? c.getString("cep") : null;

        Street street = new DaoHotAddress().getAddressFromCep(cep);

        LAddress ad = new LAddress();
        if(street != null){

            ad.setCEP(street.getCep());
            ad.setCity(street.getCityName());
            ad.setNeighborhood(street.getNeighborhood());
            ad.setState(street.getStateName());
            ad.setStreet(street.getName());
            ad.setUF(street.getUF());
        }


        return AES.encryptToAESJson(ad);
    }

    @PostMapping(path = "/get-address-from-user")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getAddressFromUser(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, ParseException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;


        ArrayList<HotReuseAddress> addresses = new DaoHotAddress().getAddressFromUser(idUser, idDevice);

        return AES.encryptToAESJson(addresses);
    }

    @PostMapping(path = "/disability-address")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String disableReUseAddress(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, ParseException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        HotReuseAddress re = new Gson().fromJson(json, HotReuseAddress.class);

        boolean deactive = new DaoHotAddress()
                .disableReUserAddress(re.getIdUser(), re.getIdOrder());

        JSONObject ob = new JSONObject();
        ob.put("result_request", deactive);

        return AES.encryptToAESJson(ob.toString());
    }
}
