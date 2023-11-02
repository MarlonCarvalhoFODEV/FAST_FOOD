package apis.onesignal.notification;

public class OneSignalEmail {

    private OneSignalNotification notification ;

    public OneSignalEmail(OneSignalNotification notification) {
        this.notification = notification;
    }



    public class Params{
        public static final String BODY = "email_body";
        public static final String NAME = "email_from_name";
        public static final String ADDRESS = "email_from_address";
        public static final String SUBJECT = "email_subject";
    }
}
