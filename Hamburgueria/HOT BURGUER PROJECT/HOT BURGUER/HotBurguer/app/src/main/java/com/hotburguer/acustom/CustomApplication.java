package com.hotburguer.acustom;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;
import com.hotburguer.R;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.payment.FMT_Payment;
import com.hotburguer.itens.HotOrder;
import com.hotburguer.itens.HotProduct;
import com.hotburguer.itens.HotUser;
import com.hotburguer.utils.CacheApp;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import apis.androidid.APIDeviceID;
import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.login.APILogin;
import apis.userbuyer.BDocument;
import apis.userbuyer.BProduct;
import apis.userbuyer.UserBuyer;

public class CustomApplication extends Application {

    private static APILogin loginSocial;
    private static HotOrder order;
    private static HotUser user;
    private static CartShopping cartShopping;
    private static UserBuyer buyer;
    private static final InternetRequisitionAPI apiRequisition = new InternetRequisitionAPI();

    public static final boolean TEST_MODE = true;
    private static HotConfigurations hotconfig;
    ;

    @Override
    public void onCreate() {
        super.onCreate();

        String publicKeyBASE64 = getResources().getString(R.string.comprazencasa);
        InternetRequisition.CryptographRSA rsaCryptogrsph = new InternetRequisition.CryptographRSA(publicKeyBASE64);

        String otherPublicKeyBASE64 = getResources().getString(R.string.payment);
        InternetRequisition.CryptographRSA otherRsaCryptogrsph = new InternetRequisition.CryptographRSA(otherPublicKeyBASE64);

        String publicKeyBASE64AES = null;
        InternetRequisition.CryptographAES rsaCryptogrsphAES = new InternetRequisition.CryptographAES(publicKeyBASE64AES);

        user = CacheApp.getAccountUser(this);
        buyer = new UserBuyer();

        apiRequisition.setCryptographRSA(rsaCryptogrsph);
        apiRequisition.setOtherCryptographRSA(otherRsaCryptogrsph);
        apiRequisition.setCryptographAES(rsaCryptogrsphAES);
        apiRequisition.setAuthorization(generateAuthorization());

        order = new HotOrder();
        order.setIdDevice(APIDeviceID.getIdDevice(this));
        loginSocial = new APILogin(this);

        requestFeeDelivery();
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        FacebookSdk.fullyInitialize();
        FacebookSdk.sdkInitialize(this);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void requestFeeDelivery() {
        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("getFeeDelivery");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.GET);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(getString(R.string.domain_connection));
        requestServer.setController(WebController.Order.CONTROLLER);
        requestServer.setFunction(WebController.Order.Functions.GET_FEE_DELIVERY);
        //requestServer.setObjectTransport(order);
        requestServer.request(this, new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {

                    hotconfig = new Gson().fromJson(object.toString(), HotConfigurations.class);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static HotConfigurations getHotconfig() {
        return hotconfig;
    }

    public static CartShopping getCartShopping() {
        return cartShopping;
    }

    public static void setCartShopping(CartShopping cartShopping) {
        CustomApplication.cartShopping = cartShopping;
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
        if (user != null && user.getEmail() != null)
            CustomApplication.user = user;

        if (user != null && user.getId() > 0) {
            setExternalIDOneSignal(String.valueOf(user.getId()));
        }
    }

    public static final UserBuyer createBuyer() {
        if (user != null) {
            UserBuyer buyer = new UserBuyer();
            buyer.setId(user.getId());
            buyer.setIdCustomer(user.getIdCustomer());
            buyer.setName(user.getName());
            buyer.getContact().setEmail(user.getEmail());
            buyer.setBirthday(user.getBirthOfDate());

            buyer.getDocument().setCPF(user.getCpf());
            buyer.getDocument().setType(BDocument.Type.CPF);

            buyer.getAddressDefault().setCity(user.getAddress().getCity());
            buyer.getAddressDefault().setCountry("br");
            buyer.getAddressDefault().setNeighborhood(user.getAddress().getNeighborhood());
            buyer.getAddressDefault().setStreet(user.getAddress().getStreet());
            buyer.getAddressDefault().setState(user.getAddress().getState());
            buyer.getAddressDefault().setZipCode(user.getAddress().getCEP());
            buyer.getAddressDefault().setStreetNumber(String.valueOf(order.getAddress().getNumber()));
            buyer.getAddressDefault().setShippingAddress(0);
            //buyer.getAddressDefault().setFeeDelivery(user.getAddress().get);
            buyer.getAddressDefault().setComplement(user.getAddress().getComplement());

            //buyer.getContact().setEmail(ac.getEmail());
            buyer.getContact().getPhone().setDdi(user.getContact().getPhone().getDdi());
            buyer.getContact().getPhone().setDDD(user.getContact().getPhone().getDDD());
            buyer.getContact().getPhone().setNumber(user.getContact().getPhone().getNumber());
            buyer.setInstallments(1);

            BProduct product;
            for (HotProduct p : order.values()) {

                product = new BProduct();
                product.setId(p.getId());
                product.setName(p.getTitle());
                product.setCategory(p.getCategorie());
                product.setPrice((int) (p.getPerPrice() * 100.0));
                product.setQuantity(p.getCount());
                product.setTangible(p.isTangible());
                product.setWeight(p.isWeight());

                buyer.addProduct(product);

            }
            return buyer;
        }
        return null;
    }

    public static InternetRequisitionAPI getApiRequisition() {
        return apiRequisition;
    }

    public static void hideSystemUI(Activity activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        if (activity != null) {
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

    public static final void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static final boolean keyboardShow(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm.isAcceptingText();
    }

    private static void setExternalIDOneSignal(String externalUserId) {

// Setting External User Id with Callback Available in SDK Version 3.13.0+
        OneSignal.setExternalUserId(externalUserId, new OneSignal.OSExternalUserIdUpdateCompletionHandler() {
            @Override
            public void onComplete(JSONObject results) {
                // The results will contain push and email success statuses
                OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id done with results: " + results.toString());

                // Push can be expected in almost every situation with a success status, but
                // as a pre-caution its good to verify it exists
                try {
                    if (results.has("push") && results.getJSONObject("push").has("success")) {
                        boolean isPushSuccess = results.getJSONObject("push").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for push status: " + isPushSuccess);
                        Log.i("KSKDKSDKKSDKS", "SUCESS: " + isPushSuccess);
                    }

                    // Verify the email is set or check that the results have an email success status
                    if (results.has("email") && results.getJSONObject("email").has("success")) {
                        boolean isEmailSuccess = results.getJSONObject("email").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Sets external user id for email status: " + isEmailSuccess);
                        Log.i("KSKDKSDKKSDKS", "SUCESS: " + isEmailSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private final String generateAuthorization() {
        String creds = String.format("%s:%s", getString(R.string.domain_connection_user), getString(R.string.domain_connection_authorization));
        return "Basic " + Base64Utils.encode(creds.getBytes());
    }
}
