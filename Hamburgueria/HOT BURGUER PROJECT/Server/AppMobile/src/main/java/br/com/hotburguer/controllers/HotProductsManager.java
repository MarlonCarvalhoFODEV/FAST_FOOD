package br.com.hotburguer.controllers;

import apis.encrypt.AES;
import apis.encrypt.RSA;
import br.com.comprazencasa.database.DaoSystemConfiguration;
import br.com.hotburguer.daos.DaoHotCryptograph;
import br.com.hotburguer.daos.DaoHotDeliveryMan;
import br.com.hotburguer.daos.DaoHotProduct;
import br.com.hotburguer.itens.HotProduct;
import br.com.hotburguer.itens.HotRatingProduct;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
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
import java.util.ArrayList;

@RestController
@RequestMapping("hot-products")
public class HotProductsManager {

    @PostMapping(path = "/get-all")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getAll(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        String categorie = c.has("categorie") ? c.getString("categorie") : null;

        ArrayList<HotProduct> products = new DaoHotProduct().getProducts(idUser, idDevice, categorie,0);

        return AES.encryptToAESJson(products);
    }

    @GetMapping(path = "/get-image/{idProduct}", produces = MediaType.IMAGE_JPEG_VALUE)
    public synchronized @ResponseBody byte[] getImage(@PathVariable int idProduct) {
        return new DaoHotProduct().getImage(idProduct);
    }

    @GetMapping(path = "/get-photo-profile/{id_user}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPhotoProfile(@PathVariable int id_user) {
        return new DaoHotDeliveryMan().getPhotoProfile(id_user);
    }

    @PostMapping(path = "/evaluation")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String evaluationProduct(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(request);
        HotRatingProduct rating = new Gson().fromJson(json, HotRatingProduct.class);

        boolean resultRating = new DaoHotProduct().evaluationProduct(rating);

        JSONObject response = new JSONObject();
        response.put("result_rating", resultRating);

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/like")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String likeProduct(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        int idProd = c.has("id_prod") ? c.getInt("id_prod") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;
        boolean status = c.has("status") ? c.getBoolean("status") : false;

        boolean result = new DaoHotProduct().likeProduct(idUser, idProd, status);

        JSONObject response = new JSONObject();
        response.put("result", result);

        return AES.encryptToAESJson(response);
    }

    @PostMapping(path = "/get-feedbacks")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public String getFeedbacks(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {

        PrivateKey pKey = new DaoHotCryptograph().getCryptographDefault().getPrivateKey();

        String json = new RSA(null, pKey).decryptFromJson(request);

        JSONObject c = new JSONObject(json);
        c = c.getJSONObject("nameValuePairs");

        int idUser = c.has("id") ? c.getInt("id") : 0;
        int idProd = c.has("id_prod") ? c.getInt("id_prod") : 0;
        String idDevice = c.has("id_dev") ? c.getString("id_dev") : null;

        JSONArray feedbacks = new DaoHotProduct().getFeedbacks(idUser,idDevice, idProd);

        return AES.encryptToAESJson(feedbacks);
    }
}
