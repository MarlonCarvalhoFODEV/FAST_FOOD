package br.com.comprazencasa.controllers;

import apis.encrypt.AES;
import br.com.comprazencasa.database.DaoCoupons;
import br.com.comprazencasa.itens.ITCoupon;
import br.com.comprazencasa.market.Pedido;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("Coupons")
public class CouponsManager {

    private final Gson gson ;
    private final DaoCoupons dao ;

    @Autowired
    public CouponsManager(Gson gson) {
        this.gson = gson;
        this.dao = new DaoCoupons();
    }

    @PostMapping(path = "/get-my-coupons")
    public String getMyCoupons(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(request);
        ITCoupon coupon = new Gson().fromJson(json, ITCoupon.class);

        List<ITCoupon> coupons = dao.getMyCoupons(coupon);

        return AES.encryptToAESJson(coupons);
    }
}
