package com.hotdelivery.aplication;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.hotdelivery.R;
import com.hotdelivery.itens.deliveryman.HotDeliveryMan;
import com.onesignal.OneSignal;

import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.login.APILogin;
import apis.login.facebook.SignInFacebook;

public class CustomApplication extends Application {

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

        loginSocial = new APILogin(this);

        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
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
}
