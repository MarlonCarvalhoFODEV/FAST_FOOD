package br.com.hotbdelivery.aplication;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationPayload;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.hotbdelivery.activitys.a3_dashboard.interfaces.IDashboard;

public class CustomNotificationReceiverHandler implements OneSignal.NotificationReceivedHandler {

    private static final int NEW_RUNNING = 1 ;

    private IDashboard dashboardInterface ;

    public CustomNotificationReceiverHandler(){}

    public CustomNotificationReceiverHandler(IDashboard dashIterface){
        dashboardInterface = dashIterface ;
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        if(notification != null){
            OSNotificationPayload payload = notification.payload ;

           if(payload != null){
               JSONObject data = payload.additionalData;
               if(data != null && data.has("type")){
                   decodeNotificationData(data);
               }
           }
        }
    }

    private final void decodeNotificationData(JSONObject data){
        int typeData = 0;

        try {
            typeData = data.getInt("type");
            switch (typeData){
                case NEW_RUNNING:{
                    dashboardInterface.notifyNewRunnig(data);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
