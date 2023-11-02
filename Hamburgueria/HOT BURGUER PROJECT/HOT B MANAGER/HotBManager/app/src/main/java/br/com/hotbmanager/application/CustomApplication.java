package br.com.hotbmanager.application;

import android.app.Application;

import com.google.android.gms.common.util.Base64Utils;
import com.onesignal.OneSignal;

import apis.androidid.APIDeviceID;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.onesignal.APIOneSignal;
import br.com.hotbmanager.R;
import br.com.hotbmanager.application.manager.account.HotManagerAccount;

public class CustomApplication extends Application {

    private static HotManagerAccount account;
    private static final InternetRequisitionAPI apiRequisition = new InternetRequisitionAPI();

    @Override
    public void onCreate() {
        super.onCreate();

        String publicRsa = getResources().getString(R.string.comprazencasa);
        InternetRequisition.CryptographRSA rsaCryptogrsph = new InternetRequisition.CryptographRSA(publicRsa);

        String publicKeyBASE64AES = null;
        InternetRequisition.CryptographAES rsaCryptogrsphAES = new InternetRequisition.CryptographAES(publicKeyBASE64AES);

        String otherPublicKeyBASE64 = getResources().getString(R.string.other);
        InternetRequisition.CryptographRSA otherRsaCryptogrsph = new InternetRequisition.CryptographRSA(otherPublicKeyBASE64);

        account = new HotManagerAccount();
        account.setId(1);
        account.setIdDevice(APIDeviceID.getIdDevice(this));

        apiRequisition.setCryptographRSA(rsaCryptogrsph);
        apiRequisition.setOtherCryptographRSA(otherRsaCryptogrsph);
        apiRequisition.setCryptographAES(rsaCryptogrsphAES);
        apiRequisition.setAuthorization(generateAuthorization());

        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        APIOneSignal.setExternalID(String.valueOf(account.getId()));
    }

    public static HotManagerAccount getAccount() {
        return account;
    }

    public static void setAccount(HotManagerAccount account) {
        CustomApplication.account = account;
    }

    public static InternetRequisitionAPI getApiRequisition() {
        return apiRequisition;
    }

    public static final String getPassAdmin(){
        return "123";
    }

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", getString(R.string.domain_connection_user), getString(R.string.domain_connection_authorization));
        return "Basic " + Base64Utils.encode(creds.getBytes());
    }
}