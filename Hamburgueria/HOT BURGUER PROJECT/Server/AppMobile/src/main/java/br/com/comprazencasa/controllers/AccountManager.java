/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.controllers;

import apis.APIFinance;
import apis.date.DateManager;
import apis.encrypt.AES;
import apis.pagarme.userbuyer.BAddress;
import apis.pagarme.userbuyer.BDocument;
import apis.pagarme.userbuyer.BPhone;
import apis.pagarme.userbuyer.UserBuyer;
import br.com.comprazencasa.database.DaoPagarme;
import br.com.comprazencasa.database.DaoUser;
import br.com.comprazencasa.itens.ITFavorite;
import br.com.comprazencasa.itens.ITStore;
import br.com.comprazencasa.user.ContaUsuario;
import br.com.comprazencasa.user.UserLogin;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import me.pagar.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.Produces;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * REST Web Service
 *
 * @author Marlon
 */
@RestController
@RequestMapping("User")
public class AccountManager {
    /**
     * Creates a new instance of UserManager
     */
    @Autowired
    public AccountManager() {}

    @PostMapping(path = "/new-register")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String registerUser(@RequestBody WebResponses web) throws ParseException {

        Gson gson = new Gson();
        ContaUsuario newRegister = gson.fromJson(web.getJsonObject(), ContaUsuario.class);

        DaoUser dao = new DaoUser();
        WebResponses response = dao.userRegister(newRegister);

        if(response.getIdUser() > 0){

            UserBuyer buyer = new UserBuyer();
            buyer.setId(response.getIdUser());
            buyer.setBirthday(newRegister.getDataNascimento());
            buyer.setName(newRegister.getName());
            buyer.getContact().setEmail(newRegister.getEmail());
            buyer.getDocument().setCPF(newRegister.getCpf());
            buyer.getDocument().setType(BDocument.Type.CPF);

            BAddress add = new BAddress();
            add.setCity(newRegister.getEndereco().getCidade());
            add.setCountry("br");
            add.setNeighborhood(newRegister.getEndereco().getBairro());
            add.setState(newRegister.getEndereco().getEstado());
            add.setStreet(newRegister.getEndereco().getRuaLogradouro());
            add.setStreetNumber(String.valueOf(newRegister.getEndereco().getNumeroResidencia()));
            add.setZipCode(newRegister.getEndereco().getCep());

            buyer.setDefaultAddress(add);
            buyer.getContact().getPhone().setDdi(55);
            buyer.getContact().getPhone().setDDD(21);
            buyer.getContact().getPhone().setNumber(newRegister.getContato().getCel());
            buyer.getContact().getPhone().setType(BPhone.Type.CEL);

            buyer.setBirthday(
                    DateManager.format
                            ("dd-MM-yyyy",
                                    "yyyy-MM-dd",
                                    buyer.getBirthday()));

            Customer customer = new DaoPagarme().getCustomer().createCustomer(buyer);
            newRegister.setIdCustomer(customer.getId());
            dao.userUpdate(newRegister);
        }

        return gson.toJson(response);
    }
    
    @PostMapping(path = "/user-login")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String userLogin(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {
        String json = AES.decryptToJson(request);
        UserLogin login = new Gson().fromJson(json, UserLogin.class);

        ContaUsuario account = new DaoUser().userLogin(login);

        return AES.encryptToAESJson(account);
    }

    @PostMapping(path = "/get-count-register")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String countRegister(@RequestBody WebResponses web){

        int countRegisters = new DaoUser().getCountRegisters();

        web = new WebResponses();
        web.setComunication(countRegisters<=700?3:4);

        return new Gson().toJson(web);
    }

    @PostMapping(path = "/user-update")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String userUpdate(@RequestBody WebResponses web){

        Gson gson = new Gson();
        ContaUsuario registerUpdate = gson.fromJson(web.getJsonObject(), ContaUsuario.class);
        boolean status = new DaoUser().userUpdate(registerUpdate);

        WebResponses response = new WebResponses();
        response.setComunication(status==true?1:-1);
        
        return gson.toJson(response);
    }

    @PostMapping(path = "/get-favorites")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getFavorites(@RequestBody String request) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException {

        String json = AES.decryptToJson(request);
        ITFavorite favorite = new Gson().fromJson(json, ITFavorite.class);

        ArrayList<ITStore> favorites = new DaoUser().getFavorites(favorite);

        return AES.encryptToAESJson(favorites) ;
    }

    @PostMapping(path = "/get-user-perfil")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String userPerfil(String json){
        
        return null ;
    }
    


}
