package com.c.hotburguerdelivery.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.c.hotburguerdelivery.itens.HotUser;
import com.facebook.FacebookSdk;
import com.c.hotburguerdelivery.R;
import com.c.hotburguerdelivery.itens.HotOrder;
import com.c.hotburguerdelivery.itens.HotProduct;

import com.c.hotburguerdelivery.apis.internet.InternetRequisition;
import com.c.hotburguerdelivery.apis.internet.InternetRequisitionAPI;
import com.c.hotburguerdelivery.apis.login.APILogin;
import com.c.hotburguerdelivery.apis.userbuyer.BDocument;
import com.c.hotburguerdelivery.apis.userbuyer.BProduct;
import com.c.hotburguerdelivery.apis.userbuyer.UserBuyer;
import com.onesignal.OneSignal;

public class CustomApplication extends Application {

    private static APILogin loginSocial ;
    private static HotOrder order;
    private static HotUser user;
    private static UserBuyer buyer ;
    private static final InternetRequisitionAPI apiRequisition = new InternetRequisitionAPI();

    @Override
    public void onCreate() {
        super.onCreate();

        String publicKeyBASE64 = getResources().getString(R.string.comprazencasa);
        InternetRequisition.CryptographRSA rsaCryptogrsph = new InternetRequisition.CryptographRSA(publicKeyBASE64);

        String publicKeyBASE64AES = null ;
        InternetRequisition.CryptographAES rsaCryptogrsphAES = new InternetRequisition.CryptographAES(publicKeyBASE64AES);

        buyer = new UserBuyer();

        apiRequisition.setCryptographRSA(rsaCryptogrsph);
        apiRequisition.setCryptographAES(rsaCryptogrsphAES);

        order = new HotOrder();
        loginSocial = new APILogin(this);

        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        FacebookSdk.sdkInitialize(this);

    }

    public static APILogin getSocialLogin() {
        return loginSocial;
    }

    public static HotOrder getOrder() {
        return order;
    }

    public static void setOrder(HotOrder order) {
        CustomApplication.order = order;
    }

    public static UserBuyer getBuyer() {
        return buyer;
    }

    public static HotUser getUser() {
        return user;
    }

    public static void setUser(HotUser user) {
        CustomApplication.user = user;
    }

    public static InternetRequisitionAPI getApiRequisition() {
        return apiRequisition;
    }

    public static void hideSystemUI(Activity activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if(activity != null){
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }


    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    public static void showSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static final void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static final boolean keyboardShow(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

       return imm.isAcceptingText();
    }

}
