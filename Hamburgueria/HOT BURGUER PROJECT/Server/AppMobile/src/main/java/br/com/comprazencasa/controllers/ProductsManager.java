/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoProducts;
import br.com.comprazencasa.itens.ITFlavor;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.itens.ITSubCategorieResponse;
import br.com.comprazencasa.util.SearchProduct;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.List;

/**
 * REST Web Service
 *
 * @author Marlon
 */
@RestController
@RequestMapping("Czc")
public class ProductsManager {

    @Autowired
    public ProductsManager(){}

    @PostMapping(path = "/get-products")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getProducts(@RequestBody WebResponses web){
        ITProduct it = new Gson().fromJson(web.getJsonObject(), ITProduct.class);

        List<ITProduct> list = null ;
        if(it != null){
            list = new DaoProducts().getProducts(it, true);
        }
        else{
            list = new DaoProducts().getProducts(true);
        }

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(1);
        response.setJsonObject(gson.toJson(list));
        return gson.toJson(response);
    }

    @PostMapping(path = "/get-subcategories")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getSubCategories(@RequestBody WebResponses web){
        ITSubCategorieResponse it = new Gson().fromJson(web.getJsonObject(), ITSubCategorieResponse.class);

        List<ITSubCategorieResponse> list = null ;
        list = new DaoProducts().getSubCategoriesFromCategorie(it, true);

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(1);
        response.setJsonObject(gson.toJson(list));
        return gson.toJson(response);
    }

    @PostMapping(path = "/search-products")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String searchProducts(@RequestBody SearchProduct it){
        List<ITProduct> list = new DaoProducts().searchProducts(it, true);

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(1);
        response.setJsonObject(gson.toJson(list));
      
        return gson.toJson(response);
    }

    @PostMapping(path = "/get-products-coll")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getProductsInactive(@RequestBody WebResponses web){

        ITProduct it = new Gson().fromJson(web.getJsonObject(), ITProduct.class);

        List<ITProduct> list = null ;
        if(it != null){
            list =  new DaoProducts().getProducts(it, web.getMessage().equals("active")?true:false);
        }
        else{
            list = new DaoProducts().getProducts(web.getMessage().equals("active")?true:false);
        }

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication(1);
        response.setJsonObject(gson.toJson(list));

        return gson.toJson(response);
    }

    @PostMapping(path = "/search-products-inactive")
    @Secured({"ADMIN","COLL"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String searchProductsInactive(@RequestBody SearchProduct it){

        Gson gson = new Gson();

        List<ITProduct> list = new DaoProducts().searchProducts(it, false);
        WebResponses response = new WebResponses();
        response.setComunication(1);
        response.setJsonObject(gson.toJson(list));

        return gson.toJson(response);
    }

    @PostMapping(path = "/update-active")
    @PreAuthorize("hasAnyRole('COLL','ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public WebResponses updateFlavorsAndPrices(@RequestBody WebResponses web){

        ITProduct p = new Gson().fromJson(web.getJsonObject(), ITProduct.class);

        boolean updated = false ;
        //boolean isFree = dao.verifyFreeProduct(p);
        //if(isFree && p != null){
            updated = new DaoProducts().updateActiveProduct(p, web.getMessage());
        //}

        WebResponses response = new WebResponses();
        response.setComunication(updated?1:0);

        return response ;
    }

    @PostMapping(path = "/create-products")
    @Secured("ADMIN")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String createProducts(@RequestBody WebResponses web){

        ITProduct it = new Gson().fromJson(web.getJsonObject(), ITProduct.class);



        int updated = -1 ;
        ITProduct pDB = new DaoProducts().existProductDB(it.getTitle());
        if(pDB == null){
            updated = new DaoProducts().insertProduct(it);
        }

        WebResponses response = new WebResponses();
        response.setComunication(updated > 0?1:0);
        Gson gson = new Gson();
        return gson.toJson(response);
    }

    @PostMapping(path = "/insert-product")
    @PreAuthorize("hasAnyRole('COLL','ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String insertNewProduct(@RequestBody WebResponses web){

        ITProduct it = new Gson().fromJson(web.getJsonObject(), ITProduct.class);

        int updated = -1 ;
        ITProduct pDB = new DaoProducts().existProductDB(it.getTitle());
        if(pDB == null){
            updated = new DaoProducts().insertProduct(it);
        }

        WebResponses response = new WebResponses();
        response.setComunication(updated>0?1:0);

        Gson gson = new Gson();
        return gson.toJson(response);
    }

    @GetMapping(path = "/verify-free")
    @PreAuthorize("hasAnyRole('COLL','ADMIN')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String verifyFree(@RequestBody WebResponses web){

        ITProduct p = new Gson().fromJson(web.getJsonObject(), ITProduct.class);

        //boolean updated = false ;
        boolean isFree = new DaoProducts().verifyFreeProduct(p);
        //if(isFree){
        //    updated = dao.updateActiveProduct(p);
        //}

        WebResponses response = new WebResponses();
        response.setComunication(isFree?1:0);

        Gson gson = new Gson();
        return gson.toJson(response);
    }

    @PostMapping(path = "/get-flavors")
    @Secured({"USER","COLL","ADMIN"})
    @PreAuthorize("hasAnyRole('ADMIN','COLL','USER')")
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getFlavors(@RequestBody WebResponses web){

        ITProduct it = new Gson().fromJson(web.getJsonObject(), ITProduct.class);
        Gson gson = new Gson();

        List<ITFlavor> list = new DaoProducts().getFlavors(it.getId());
        WebResponses response = new WebResponses();
        response.setComunication(list.size()>0?1:0);
        response.setJsonObject(gson.toJson(list));

        return gson.toJson(response);
    }
}
