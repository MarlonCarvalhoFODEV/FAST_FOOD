package br.com.comprazencasa.controllers;

import apis.encrypt.AES;
import br.com.comprazencasa.database.DaoCampaing;
import br.com.comprazencasa.itens.ITCampaing;
import br.com.comprazencasa.itens.ITStore;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@RestController
@RequestMapping("campaings")
public class CampaingManager {

    @PostMapping(path = "/get-campaings-seasonal")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getCampaing(@RequestBody String aesJson) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        byte[] json = AES.decrypt(new Gson().fromJson(aesJson, byte[].class));
        ITStore store = new Gson().fromJson(new String(json), ITStore.class);

        ArrayList<ITCampaing> campaings = new DaoCampaing().getCampaingsSeasonal(store.getCategorie(), true);

        return AES.encryptToAESJson(campaings);
    }

    @GetMapping(path = "/get-image-campaing/{imgPath}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody byte[] getImageFromCampaing(@PathVariable("imgPath") String imgPath){
        //byte[] json = AES.decrypt(new Gson().fromJson(AESInJson, byte[].class));
        //ITCampaing store = new Gson().fromJson(new String(json), ITCampaing.class);
        return new DaoCampaing().getImageFromCampaing(imgPath);
    }
}
