package com.c.hotburguerdelivery.zutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PopVerifyConnection {

    public static boolean haveConnectionNetwork(Context c){

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnected()){
            return true ;
        }else {
            return false ;
        }
    }
}
