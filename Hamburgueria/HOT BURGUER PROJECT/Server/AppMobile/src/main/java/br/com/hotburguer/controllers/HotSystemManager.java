package br.com.hotburguer.controllers;

import apis.encrypt.AES;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("system")
public class HotSystemManager {

    @GetMapping(path = "/check-update")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String asUpdate(HttpServletRequest request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String appVersion = request.getParameter("app_version");

        float version = new DaoSystemConfiguration().getLastVersionHotBurguer();

        boolean asUpdate = version > Float.valueOf(appVersion) ;
        JSONObject json = new JSONObject();
        json.put("as_update", asUpdate);

        return AES.encryptToAESJson(json);
    }

    @GetMapping(path = "/has-update-deliveryman")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String asUpdatDeliveryMan(HttpServletRequest request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String appVersion = request.getParameter("app_version");

        float version = new DaoSystemConfiguration().getLastVersionDeliveryMan();

        boolean asUpdate = version > Float.valueOf(appVersion) ;
        JSONObject json = new JSONObject();
        json.put("as_update", asUpdate);

        return AES.encryptToAESJson(json);
    }
}
