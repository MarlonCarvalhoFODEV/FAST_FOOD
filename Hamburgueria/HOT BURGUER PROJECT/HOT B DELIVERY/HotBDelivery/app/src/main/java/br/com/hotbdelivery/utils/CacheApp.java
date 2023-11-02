package br.com.hotbdelivery.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apis.cache.CacheAPI;
import br.com.hotbdelivery.aplication.CustomApplication;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;
import br.com.hotbdelivery.itens.HotDelivery;
import br.com.hotbdelivery.itens.HotOrdersDelivery;
import br.com.hotbdelivery.itens.HotRunning;

public class CacheApp {

    public static void saveAccount(Context context, HotDeliveryMan account){
        CacheAPI.saveObject(context, HotDeliveryMan.class.getSimpleName(),account, true);
        CustomApplication.setAccount(account);
    }

    public static HotDeliveryMan getAccount(Context context){
        String json = CacheAPI.getObjectJson(context, HotDeliveryMan.class.getSimpleName(), true);
        return new Gson().fromJson(json, HotDeliveryMan.class);
    }

    public static void saveRunning(Context context, HotRunning running){
        CacheAPI.saveObject(context, HotRunning.class.getSimpleName(),running, true);
    }

    public static HotRunning getRunning(Context context){
        String json = CacheAPI.getObjectJson(context, HotRunning.class.getSimpleName(),true);
        return new Gson().fromJson(json, HotRunning.class);
    }

    public static void saveDataNotificationNeoRunning(Context context, JSONObject data){
        CacheAPI.saveObject(context, "running_from_notification",data, true);
    }

    public static JSONObject getDataNotificationNeoRunning(Context context){
        String json = CacheAPI.getObjectJson(context, "running_from_notification",true);
        return new Gson().fromJson(json, JSONObject.class);
    }
}
