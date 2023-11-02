package com.c.hotburguerdelivery.zutils.webservice.volleycustom;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.comprazencasa.colaborador.R;
import com.comprazencasa.colaborador.application.CustomApplication;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marlon on 12/04/2018.
 */

public class VolleyCon {

    private Transaction mTransaction ;
    private RequestQueue mRequestQueue ;
    private Context context ;

    public VolleyCon(Context context, Transaction transaction){
        this.context = context ;
        this.mTransaction = transaction ;
        try {
            this.mRequestQueue = ((CustomApplication) ((Activity) context).getApplication()).getRequestQueue();
        }catch(ClassCastException e){
            this.mRequestQueue = ((CustomApplication)context).getRequestQueue();
        }
    }

    public void executeJSONObj(){
        mTransaction.doBefore();
        callByJsonRequest(mTransaction.getRequestData());
    }

    public void executeString(){
        mTransaction.doBefore();
        callByJsonString(mTransaction.getRequestData());
    }

    public synchronized void callByJsonRequest(RequestData requestData){

        JSONObject jObj = null;
        try {
            jObj = new JSONObject(new Gson().toJson(requestData.getObject()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final StringBuilder url = new StringBuilder(requestData.getUrlConection());

        JsonObjectRequest jor = new JsonObjectRequest(requestData.getRequestType(), url.toString(), jObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mTransaction.doAfter(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mTransaction.doAfter(null);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createHeader();
            }
        };

        jor.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(jor);
    }

    public synchronized void callByJsonString(final RequestData requestData){

        StringRequest request = new StringRequest(Request.Method.GET, requestData.getUrlConection(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mTransaction.doAfter(response);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTransaction.doAfter(null);
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("object",new Gson().toJson(requestData.getObject()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type","x-www-form-urlencoded");
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    private HashMap<String, String> createHeader(){
        HashMap<String, String> params = new HashMap<>();
        String creds = String.format("%s:%s",context.getString(R.string.kkeyu),context.getString(R.string.kkeyp));
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        params.put("Content-Type","application/json; charset=utf-8");
        params.put("Accept", "application/json");

        return params;
    }
}
