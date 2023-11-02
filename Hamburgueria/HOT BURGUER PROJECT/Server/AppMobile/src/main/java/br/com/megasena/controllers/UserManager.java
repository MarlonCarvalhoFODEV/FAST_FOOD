package br.com.megasena.controllers;

import apis.encrypt.AES;
import br.com.megasena.dao.DaoCryptography;
import br.com.megasena.dao.DaoUser;
import br.com.megasena.itens.ITUser;
import com.google.gson.Gson;
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

@RestController
@RequestMapping("users")
public class UserManager {

    @PostMapping(path = "/new-user")
    public String inserUser(@RequestBody String post, HttpServletRequest request) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        JSONObject ob = new DaoCryptography().decrypt(post);

        ITUser neoUser = new Gson().fromJson(ob.toString(), ITUser.class);

        boolean result = new DaoUser().insertUser(neoUser);

        JSONObject response = new JSONObject();
        response.put("result",result);
        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/login-user-access")
    public String loginUser(@RequestBody String post, HttpServletRequest request) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        JSONObject ob = new DaoCryptography().decrypt(post);

        ITUser userLogged = new DaoUser().loginUser(ob.getString("email"),ob.getString("id_dev"));

        return AES.encryptToAESJson(userLogged);
    }

}
