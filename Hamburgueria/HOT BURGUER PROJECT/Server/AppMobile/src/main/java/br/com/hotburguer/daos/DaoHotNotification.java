package br.com.hotburguer.daos;

import apis.onesignal.APIOneSignal;
import apis.onesignal.OneSignalResponse;
import apis.onesignal.notification.OneSignalFilter;
import apis.onesignal.notification.OneSignalNotification;
import apis.onesignal.notification.OneSignalSegment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DaoHotNotification {

    private static final String  appIDDeliveryman = "5627bc3c-23eb-40bb-b45b-6c27b8dd639f";
    private static final String apiKeyDeliveryman = "NmIxOGM5ZGItNjI5OS00NjNlLTkzNTUtMzM3NmQxOTBmYzU5";

    private static final String  appIDHotBurguer = "98c46fd1-2bd2-4226-bd83-480ec194b796";
    private static final String apiKeyHotBurguer = "ZjhiY2M1M2QtYmM2MC00Nzk2LWJmMTItNzViYjNhMTFiZDJi";

    private static final String  appIDHotManager = "1c8783ed-0e16-487c-b5fd-9af6bc200b90";
    private static final String apiKeyHotManager = "ZTc4ZjY1MzMtMjBhNy00MDA3LWI5MmQtMzk2NTc4YjU1NmQ2";


    private final APIOneSignal oneSignalHotBurguer;
    private final APIOneSignal oneSignalHotDelivery;
    private final APIOneSignal oneSignalHotManager;

    public DaoHotNotification() {
        this.oneSignalHotBurguer = new APIOneSignal(appIDHotBurguer, apiKeyHotBurguer);
        this.oneSignalHotDelivery = new APIOneSignal(appIDDeliveryman, apiKeyDeliveryman);
        this.oneSignalHotManager = new APIOneSignal(appIDHotManager, apiKeyHotManager);
    }

    public OneSignalResponse notifyClient(String title, String subTitle, String message, ArrayList<String> idClients, JSONObject data) {
        OneSignalNotification notification = new OneSignalNotification();

        notification.setLanguage(OneSignalNotification.Languages.ENGLISH);
        notification.setTitle(title);
        notification.setSubTitle(subTitle);
        notification.setMessage(message);
        //not.getSegments().setSegmentBoth(OneSignalSegment.Type.INCLUDED_SEGMENT);
        notification.getAppearances().setAndroidSmallIcon("ic_notification_full");
        notification.getAppearances().setAndroidLedColor("FA730E");
        notification.getAppearances().setAndroidAccentColor("FA730E");

        OneSignalFilter filter = new OneSignalFilter(notification);
        //filter.setIncludePlayerIds(Arrays.asList("42"));
        filter.setIncludeExternalUserIds(idClients);

        if (data != null)
            notification.setData(data);

        return oneSignalHotBurguer.getNotificationManager().sendNotification(notification);
    }

    public synchronized OneSignalResponse notifyDeliveryMan(String title, String subTitle, String message, int idUser, JSONObject data) {
        OneSignalNotification notification = new OneSignalNotification();

        notification.setLanguage(OneSignalNotification.Languages.ENGLISH);
        notification.setTitle(title);
        notification.setSubTitle(subTitle);
        notification.setMessage(message);

        notification.getAppearances().setAndroidSmallIcon("ic_notification");
        notification.getAppearances().setAndroidLedColor("FA730E");
        notification.getAppearances().setAndroidAccentColor("FA730E");

        OneSignalFilter filter = new OneSignalFilter(notification);
        //filter.setIncludePlayerIds(Arrays.asList("42"));
        if(idUser > 0)
            filter.setIncludeExternalUserIds(Arrays.asList(String.valueOf(idUser)));
        else
            notification.getSegments().setSegmentBoth(OneSignalSegment.Type.INCLUDED_SEGMENT);


        if (data != null)
            notification.setData(data);

        return oneSignalHotDelivery.getNotificationManager().sendNotification(notification);
    }

    public synchronized OneSignalResponse notifyDeliveryMan(String title, String subTitle, String message, ArrayList<String> idsDeliverymans, JSONObject data) {
        OneSignalNotification notification = new OneSignalNotification();

        notification.setLanguage(OneSignalNotification.Languages.ENGLISH);
        notification.setTitle(title);
        notification.setSubTitle(subTitle);
        notification.setMessage(message);

        notification.getAppearances().setAndroidSmallIcon("ic_notification");
        notification.getAppearances().setAndroidLedColor("FA730E");
        notification.getAppearances().setAndroidAccentColor("FA730E");

        OneSignalFilter filter = new OneSignalFilter(notification);
        //filter.setIncludePlayerIds(Arrays.asList("42"));
        if(idsDeliverymans != null && idsDeliverymans.size() > 0)
            filter.setIncludeExternalUserIds(idsDeliverymans);
        else
            notification.getSegments().setSegmentBoth(OneSignalSegment.Type.INCLUDED_SEGMENT);


        if (data != null)
            notification.setData(data);

        return oneSignalHotDelivery.getNotificationManager().sendNotification(notification);
    }

    public OneSignalResponse notifyManager(String title, String subTitle, String message, int idUser, String data) {
        OneSignalNotification notification = new OneSignalNotification();

        notification.setLanguage(OneSignalNotification.Languages.ENGLISH);
        notification.setTitle(title);
        notification.setSubTitle(subTitle);
        notification.setMessage(message);
        //not.getSegments().setSegmentBoth(OneSignalSegment.Type.INCLUDED_SEGMENT);
        notification.getAppearances().setAndroidSmallIcon("ic_notification");
        notification.getAppearances().setAndroidLedColor("FA730E");
        notification.getAppearances().setAndroidAccentColor("FA730E");

        OneSignalFilter filter = new OneSignalFilter(notification);
        //filter.setIncludePlayerIds(Arrays.asList("42"));
        filter.setIncludeExternalUserIds(Arrays.asList(String.valueOf(idUser)));

        if (data != null)
            notification.setData(new JSONObject(data));

        return oneSignalHotManager.getNotificationManager().sendNotification(notification);
    }

    public boolean notifyAll(String title, String subTitle, String message, int idUser, String data) {
        OneSignalNotification notification = new OneSignalNotification();

        notification.setLanguage(OneSignalNotification.Languages.ENGLISH);
        notification.setTitle(title);
        notification.setSubTitle(subTitle);
        notification.setMessage(message);
        //not.getSegments().setSegmentBoth(OneSignalSegment.Type.INCLUDED_SEGMENT);
        notification.getAppearances().setAndroidSmallIcon("ic_notification");
        notification.getAppearances().setAndroidLedColor("FA730E");
        notification.getAppearances().setAndroidAccentColor("FA730E");

        OneSignalFilter filter = new OneSignalFilter(notification);
        //filter.setIncludePlayerIds(Arrays.asList("42"));
        filter.setIncludeExternalUserIds(Arrays.asList(String.valueOf(idUser)));

        if (data != null)
            notification.setData(new JSONObject(data));

        oneSignalHotBurguer.getNotificationManager().sendNotification(notification);
        oneSignalHotDelivery.getNotificationManager().sendNotification(notification);
        oneSignalHotManager.getNotificationManager().sendNotification(notification);

        return true ;
    }

    public boolean cancelAllNotifications() {

        oneSignalHotBurguer.getNotificationManager().cancelNotification("");
        oneSignalHotDelivery.getNotificationManager().cancelNotification("");
        oneSignalHotManager.getNotificationManager().cancelNotification("");

        return true ;
    }

    public class BuilderNotification {

        private OneSignalNotification notification = new OneSignalNotification();
        private OneSignalFilter filter = new OneSignalFilter(notification);

        public OneSignalNotification setTitle(String title){
            notification.setTitle(title);
            return notification ;
        }

        public OneSignalNotification setSubTitle(String subTitle){
            notification.setSubTitle(subTitle);
            return notification ;
        }

        public OneSignalNotification setMessage(String message){
            notification.setMessage(message);
            return notification ;
        }

        public OneSignalNotification setAndroidAppearenceSmallIcon(String nameIcon){
            notification.getAppearances().setAndroidSmallIcon(nameIcon);
            return notification ;
        }

        public OneSignalNotification setAndroidAppearencLedColor(String color){
            notification.getAppearances().setAndroidLedColor(color);
            return notification ;
        }

        public OneSignalNotification setAndroidAppearencLedAccentColor(String color){
            notification.getAppearances().setAndroidAccentColor(color);
            return notification ;
        }

        public OneSignalNotification setData(String json){
            notification.setData(new JSONObject(json));
            return notification ;
        }

        public OneSignalNotification setFilterExternalID(String id){
            filter.setIncludeExternalUserIds(Arrays.asList(String.valueOf(id)));
            return notification ;
        }
    }

    public class TypeNotification{
        public class Client{

        }
        public class Deliveryman{
            public static final int NEW_RUNNING = 101 ;
            public static final int MESSAGE = 102 ;
            public static final int NOTIFICATION = 103 ;
        }
        public class Manager{

        }
    }
}
