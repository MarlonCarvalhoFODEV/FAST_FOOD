package br.com.hotbmanager.utils;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import apis.cache.CacheAPI;
import br.com.hotbmanager.application.CustomApplication;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;

public class CacheApp {

    public static void saveAccount(Context context, HotManagerAccount account){
        CacheAPI.saveObject(context, HotManagerAccount.class.getSimpleName(),account, true);
        CustomApplication.setAccount(account);
    }

    public static HotManagerAccount getAccount(Context context){
        String json = CacheAPI.getObjectJson(context, HotManagerAccount.class.getSimpleName(), true);
        return new Gson().fromJson(json, HotManagerAccount.class);
    }

    public static void saveProgressProduction(Context context, Map<String, Integer> progress){
        CacheAPI.saveObject(context, "saveProgressProductionUP",progress, true);
    }

    public static Map<String,Integer> getProgressProduction(Context context){
        String json = CacheAPI.getObjectJson(context, "saveProgressProductionUP", true);
        return json!=null?new Gson().fromJson(json, HashMap.class):null;
    }

    public static void saveSecurity(Context context, int idDeliveryMan, String nameDeliveryman){
        Map<Integer, String> security = getSecurity(context);
        security.put(idDeliveryMan, nameDeliveryman);
        CacheAPI.saveObject(context, "saveSecurity",security, true);
    }

    public static Map<Integer,String> getSecurity(Context context){
        String json = CacheAPI.getObjectJson(context, "saveSecurity", true);
        return json!=null?new Gson().fromJson(json, HashMap.class):new HashMap<Integer, String>();
    }

    public static void eraseSecurity(Context context){
        CacheAPI.saveObject(context, "saveSecurity",new HashMap<>(), true);
    }


}
