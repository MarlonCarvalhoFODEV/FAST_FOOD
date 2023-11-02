package br.com.hotbdelivery.aplication;

import android.util.Log;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.hotbdelivery.activitys.a3_dashboard.interfaces.IDashboard;
import br.com.hotbdelivery.utils.CacheApp;

public class CustomNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    private static final int NEW_RUNNING = 1 ;

    private IDashboard dashboardInterface ;

    public CustomNotificationOpenedHandler(){}

    public CustomNotificationOpenedHandler(IDashboard dashIterface){
        dashboardInterface = dashIterface ;
    }


    @Override
    public void notificationOpened(OSNotificationOpenResult result) {

        JSONObject data = result.notification.payload.additionalData;

        if(data != null && data.has("type")){
            decodeNotificationData(data);
        }
    }

    private final void decodeNotificationData(JSONObject data){
        int typeData = 0;
        try {
            typeData = data.getInt("type");
            switch (typeData){
                case NEW_RUNNING:{
                    CustomApplication.setDataNotification(data);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
