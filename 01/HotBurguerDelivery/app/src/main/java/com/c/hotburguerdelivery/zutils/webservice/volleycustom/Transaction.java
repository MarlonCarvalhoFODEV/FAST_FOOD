package com.c.hotburguerdelivery.zutils.webservice.volleycustom;

/**
 * Created by Marlon on 12/04/2018.
 */

public interface Transaction {

    public RequestData getRequestData();

    public void doBefore();

    public void doAfter(String response);

}
