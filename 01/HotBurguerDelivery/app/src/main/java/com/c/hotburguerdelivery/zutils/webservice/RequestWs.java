package com.c.hotburguerdelivery.zutils.webservice;

import android.content.Context;

import com.android.volley.Request;
import com.comprazencasa.colaborador.zutils.PopVerifyConnection;
import com.comprazencasa.colaborador.zutils.webservice.volleycustom.RequestData;
import com.comprazencasa.colaborador.zutils.webservice.volleycustom.Transaction;
import com.comprazencasa.colaborador.zutils.webservice.volleycustom.VolleyCon;
import com.google.gson.Gson;

public class RequestWs implements Transaction {

    private Context context;
    private String method ;
    private String url ;
    private Object object;
    private WebResponseIT webResponseIT;
    private int typeRequest ;


    public RequestWs(Context context, WebResponseIT webResponseIT){
        this.context = context ;
        this.webResponseIT = webResponseIT;
    }

    public synchronized void request(int typeRequest, StringBuilder method, Object object) {
        this.method = method.toString();
        this.object = object;
        this.typeRequest = typeRequest;

        if(PopVerifyConnection.haveConnectionNetwork(context) == true){
            new VolleyCon(context,this).executeJSONObj();
        } else {
            webResponseIT.interfaceController(null);
        }
    }

    public synchronized void request(int typeRequest, StringBuilder method) {
        this.typeRequest = typeRequest;
        this.url = method.toString() ;
        if(PopVerifyConnection.haveConnectionNetwork(context) == true){
            new VolleyCon(context,this).executeString();
        } else {
            webResponseIT.interfaceController(null);
        }
    }

    @Override
    public RequestData getRequestData() {
        switch (typeRequest){
            case Request.Method.POST:{
                return new RequestData(context, Request.Method.POST, method, object);
            }
            case Request.Method.GET:{
                return new RequestData(context, Request.Method.GET, method, object);
            }
            case Request.Method.PUT:{
                return new RequestData(context, Request.Method.PUT, method, object);
            }
            case Request.Method.DELETE:{
                return new RequestData(context, Request.Method.DELETE, method, object);
            }
        }
        return null ;
    }

    @Override
    public void doBefore() { }

    @Override
    public void doAfter(String response) {
        WebResponses pop = null ;
        if(PopVerifyConnection.haveConnectionNetwork(context) == true && response != null){
            pop = new Gson().fromJson(response, WebResponses.class);

            webResponseIT.webResponse(pop);
        }else if(PopVerifyConnection.haveConnectionNetwork(context) == true && response == null){
            webResponseIT.interfaceController(pop);
        }

    }
}