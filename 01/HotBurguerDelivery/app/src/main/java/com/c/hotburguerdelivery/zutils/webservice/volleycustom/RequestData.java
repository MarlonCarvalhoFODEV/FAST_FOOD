package com.c.hotburguerdelivery.zutils.webservice.volleycustom;

import android.content.Context;

import com.comprazencasa.colaborador.R;


/**
 * Created by Marlon on 12/04/2018.
 */

public class RequestData {

    private int requestType ;
    private String urlConection ;
    private Object object ;

    public RequestData(Context context, int requestType, String method, Object object){
        this.requestType = requestType ;
        this.urlConection =  context.getResources().getString(R.string.connectionWs)+method;
        this.object = object ;
    }

    public RequestData(Context context, int requestType, int idStringUrl, String method, Object object){
        this.requestType = requestType ;
        this.urlConection =  context.getResources().getString(idStringUrl)+method;
        this.object = object ;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getUrlConection() {
        return urlConection;
    }

    public void setUrlConection(String urlConection) {
        this.urlConection = urlConection;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
