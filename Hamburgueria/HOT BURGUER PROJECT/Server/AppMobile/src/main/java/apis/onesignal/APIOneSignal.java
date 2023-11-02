package apis.onesignal;

import apis.onesignal.notification.NotificationManager;

public class APIOneSignal {

    private String urlRoot = "https://onesignal.com/api/v1/";
    private final NotificationManager notificationManager ;

    public APIOneSignal(String appID, String restApiKey) {
        this.notificationManager = new NotificationManager(urlRoot, appID, restApiKey);
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
}
