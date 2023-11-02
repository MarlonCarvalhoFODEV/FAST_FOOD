package com.hotburguer.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hotburguer.acustom.Cart;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;

import java.util.HashMap;
import java.util.Map;

import apis.cache.CacheAPI;

public class CacheApp {

    public static final void saveAccountUser(Context context, HotUser user){
        CacheAPI.saveObject(context, HotUser.class.getSimpleName(), user, true);
    }

    public static HotUser getAccountUser(Context context){
        String json = CacheAPI.getObjectJson(context, HotUser.class.getSimpleName(),true);
        HotUser account = new Gson().fromJson(json, HotUser.class);

        if(account != null)
            return account ;
        else
            return new HotUser();
    }

    public static final void saveCart(Context context, Cart cart){
        CacheAPI.saveObject(context, HotProduct.class.getSimpleName(), cart, true);
    }

    public static Cart getCart(Context context){
        String json = CacheAPI.getObjectJson(context, HotProduct.class.getSimpleName(),true);

        try {
            Cart cart = new Gson().fromJson(json, Cart.class);

            if(cart != null) {
                return cart;
            }else {
                return new Cart();
            }
        }catch(JsonSyntaxException w){
            return new Cart();
        }
    }

    public static final void clearCart(Context context){
        saveCart(context, new Cart());
    }
}
