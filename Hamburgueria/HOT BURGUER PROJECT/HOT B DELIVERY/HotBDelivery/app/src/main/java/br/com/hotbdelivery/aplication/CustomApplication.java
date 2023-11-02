package br.com.hotbdelivery.aplication;

import android.app.Application;
import android.util.Log;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.util.Base64Utils;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.WebJson;
import apis.login.APILogin;
import br.com.hotbdelivery.R;
import br.com.hotbdelivery.deliveryman.HotDeliveryMan;

public class CustomApplication extends Application {

    private static JSONObject dataNotification ;
    private static HotDeliveryMan account ;
    private static APILogin loginSocial ;
    private static final InternetRequisitionAPI apiRequisition = new InternetRequisitionAPI();

    @Override
    public void onCreate() {
        super.onCreate();

        String publicKeyBASE64 = getResources().getString(R.string.comprazencasa);
        InternetRequisition.CryptographRSA rsaCryptogrsph = new InternetRequisition.CryptographRSA(publicKeyBASE64);

        String publicKeyBASE64AES = null ;
        InternetRequisition.CryptographAES rsaCryptogrsphAES = new InternetRequisition.CryptographAES(publicKeyBASE64AES);

        account = new HotDeliveryMan();

        apiRequisition.setCryptographRSA(rsaCryptogrsph);
        apiRequisition.setCryptographAES(rsaCryptogrsphAES);
        apiRequisition.setAuthorization(generateAuthorization());

        loginSocial = new APILogin(this);

        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new CustomNotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
    }

    public static HotDeliveryMan getAccount() {
        return account;
    }

    public static void setAccount(HotDeliveryMan account) {
        CustomApplication.account = account;
    }

    public static APILogin getSocialLogin() {
        return loginSocial;
    }

    public static InternetRequisitionAPI getApiRequisition() {
        return apiRequisition;
    }

    public static JSONObject getDataNotification() {
        return dataNotification;
    }

    public static void setDataNotification(JSONObject data) {
        dataNotification = data;
    }

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", getString(R.string.domain_connection_user), getString(R.string.domain_connection_authorization));
        return "Basic " + Base64Utils.encode(creds.getBytes());
    }
}
