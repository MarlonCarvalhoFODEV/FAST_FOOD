package br.com.hotburguer.controllers;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.hotburguer.daos.DaoHotCryptograph;
import br.com.hotburguer.daos.DaoHotManager;
import br.com.hotburguer.util.IpAddressManager;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;

@RestController
@RequestMapping("manager")
public class HotManagers {

    @PostMapping(path = "/get-openings-hours")
    public String openingHours(HttpServletRequest httpServletRequest, @RequestBody String request) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        int idDel = c.has("id_del") ? c.getInt("id_del") : 0;

        String publicIp = IpAddressManager.getPublicIP(httpServletRequest);
        ArrayList<JSONObject> hours = new DaoHotManager().getOpeningHours(idManager, idDevice,publicIp);

        return AES.encryptToAESJson(hours);
    }

    @PostMapping(path = "/update-opening-day")
    public String updateDay(HttpServletRequest httpServletRequest, @RequestBody String request) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographPayment().getPrivateKey();
        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idManager = c.has("1") ? c.getInt("1") : 0;
        String idDevice = c.has("2") ? c.getString("2") : null;

        String publicIp = IpAddressManager.getPublicIP(httpServletRequest);
        boolean result = new DaoHotManager().updateDay(idManager, idDevice,publicIp,c);

        JSONObject response = new JSONObject();
        response.put("result",result);

        return AES.encryptToAESJson(response);
    }
}
