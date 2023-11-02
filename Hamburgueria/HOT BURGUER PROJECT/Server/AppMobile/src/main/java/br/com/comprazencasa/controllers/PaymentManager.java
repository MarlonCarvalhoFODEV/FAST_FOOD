/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoPayment;
import br.com.comprazencasa.util.RateServices;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import com.mercadopago.resources.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Web Service
 *
 * @author Marlon
 */
@RestController
@RequestMapping("Payment")
public class PaymentManager {

    /**
     * Creates a new instance of PaymentManager
     */
    @Autowired
    public PaymentManager() {}
     
    @PostMapping(path = "/do-payment-custom")
    public String doPaymentCustomServer(@RequestBody Map<String, Object> map){
        Preference sucess = new DaoPayment().doPaymentAndroid(map);
        return new Gson().toJson(sucess) ;
    }

    @GetMapping(path = "/get-rate-services")
    public String getRateService(){
        RateServices rates = new DaoPayment().getRateServices();

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(5);
        response.setJsonObject(gson.toJson(rates));
        return gson.toJson(response) ;
    }

}
