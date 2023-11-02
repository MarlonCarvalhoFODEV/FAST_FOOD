package br.com.comprazencasa.products;

import org.json.JSONArray;
import org.json.JSONObject;

public class PaymentFULL extends JSONObject{

    public void setValue(String key, String value){
        put(key,value);
    }

    public void addObject(String name, JSONObject newObject){
        put(name, newObject);
    }


}
