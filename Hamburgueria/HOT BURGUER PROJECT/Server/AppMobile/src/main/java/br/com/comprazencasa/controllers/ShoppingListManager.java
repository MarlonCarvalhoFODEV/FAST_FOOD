package br.com.comprazencasa.controllers;

import br.com.comprazencasa.database.DaoListShopping;
import br.com.comprazencasa.itens.ITListShopping;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.util.WebResponses;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ShoppingList")
public class ShoppingListManager {

    @Autowired
    public ShoppingListManager(){
    }

    @PostMapping(path = "/get-all-lists")
    public String getAllShoppingList(@RequestBody ITListShopping item){
        List<ITListShopping> list = new DaoListShopping().getAllLists(item);

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication((list!=null&&list.size()>0)?1:0);
        response.setJsonObject(gson.toJson(list));

        return gson.toJson(response);
    }

    @PostMapping(path = "/get-products-list")
    public String getProductsFromItem(@RequestBody ITListShopping item){
        List<ITProduct> list ;

        list =  new DaoListShopping().getProductsFromListShopping(item);

        Gson gson = new Gson();

        WebResponses response = new WebResponses();
        response.setComunication((list!=null&&list.size()>0)?1:0);
        response.setJsonObject(gson.toJson(list));

        return gson.toJson(response);
    }

}
