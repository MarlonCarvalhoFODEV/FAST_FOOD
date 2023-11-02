package com.hotburguer.activitys.a1_splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.hotburguer.R;
import com.hotburguer.activitys.a2_register.RegisterActivity;
import com.hotburguer.activitys.a5_home.HomeBurguerActivity;
import com.hotburguer.activitys.a6_trackorder.TrackOrderActivity;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogInformative;
import com.hotburguer.fragments.payment.Payment;
import com.hotburguer.itens.HotOrder;
import com.hotburguer.itens.HotUser;
import com.hotburguer.utils.CacheApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.login.facebook.SignInFacebook;

import static com.hotburguer.acustom.CustomApplication.getUser;
import static com.hotburguer.acustom.CustomApplication.setUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CustomApplication.hideSystemUI(this);

        if(CustomApplication.TEST_MODE){
            nextActiviy(3500);
        }
        else {
            checkUpdate();
        }
    }

    private void animateLogo() {
        YoYo.with(Techniques.FadeInLeft)
                .duration(1000)
                .repeat(0)
                .playOn(findViewById(R.id.iv_logo_splash));

    }

    private void nextActiviy(int timeMills) {
        boolean logged = CustomApplication.getSocialLogin().isLogged();
        registerUser();

        //animateLogo();
        //animateText();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //animeLogoOut();
                //animateBackOut();
            }
        }, 2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //animateTextOut();
                //animateBackOut();
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String obJson = CacheAPI.getObjectJson(SplashActivity.this, HotOrder.class.getSimpleName(), true);

                HotOrder order = null;
                if (obJson != null)
                    order = new Gson().fromJson(obJson, HotOrder.class);

                if (order != null && order.getId() > 0 && order.getStatuspayment() != null &&
                        (order.getStatuspayment().equals(Payment.Status.PAID)
                                || order.getStatuspayment().equals(Payment.Status.AUTHORIZED))) {

                    Intent intent = new Intent(SplashActivity.this, TrackOrderActivity.class);
                    intent.putExtra("order_in_process", order);
                    startActivity(intent);
                    finish();
                } else {
                    if (!logged) {
                        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, HomeBurguerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, timeMills);
    }

    private void showDialogUpdate() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 0.0000001f;
        params.bottomMargin = 16;
        params.leftMargin = 16;
        params.rightMargin = 16;

        DialogInformative dialog = new DialogInformative(this);
        dialog.setTitle("Nova Atualização");
        dialog.setMessage("Baixe a nova versão disponível");
        dialog.setCancelableTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setPadding(16, true);
        dialog.addButton(
                "ATUALIZAR",
                Color.WHITE, getResources().getDrawable(R.drawable.rounded_button_secundary),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
        dialog.show();
    }

    private final void checkUpdate() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("checkUpdate()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.GET);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(getString(R.string.domain_connection));
            requestServer.setController(WebController.System.CONTROLLER);
            requestServer.setFunction(WebController.System.Functions.CHECK_UPDATE);
            requestServer.addUrlParam("app_version", version);
            //requestServer.setObjectTransport(map);
            requestServer.request(this, new InternetRequisitionResponse() {
                @Override
                public void onLoading(boolean loading) {

                }

                @Override
                public void onSucess(Object object) {
                    if (object != null) {
                        JSONObject jo = null;
                        try {
                            jo = new JSONObject(object.toString());
                            jo = jo.getJSONObject("map");

                            boolean asUpdate = jo.getBoolean("as_update");
                            if (asUpdate) {
                                showDialogUpdate();
                            } else {
                                nextActiviy(3500);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            nextActiviy(4500);
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    nextActiviy(4500);
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registerUser() {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        GoogleSignInAccount googAc = CustomApplication.getSocialLogin().getSignInGoogle().getAccount();
        SignInFacebook.FacebookAccount faceAc = CustomApplication.getSocialLogin().getSignInFacebook().getAccount();

        String json = CacheAPI.getObjectJson(SplashActivity.this, HotUser.class.getSimpleName(), true);

        if (json == null) {
            if (googAc != null || faceAc != null) {
                Map<String, String> map = new HashMap<>();
                map.put("email", googAc != null ? googAc.getEmail() : faceAc.getEmail());
                map.put("name", googAc != null ? googAc.getDisplayName() : faceAc.getName());
                map.put("photo", googAc != null && googAc.getPhotoUrl() != null ? googAc.getPhotoUrl().toString() : faceAc != null && faceAc.getPhotoUrl() != null ? faceAc.getPhotoUrl() : null);
                map.put("id_android", android_id);
                map.put("social_media", (googAc != null ? "google" : "facebook"));

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("registerUser()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(WebController.User.CONTROLLER);
                requestServer.setFunction(WebController.User.Functions.NEW_USER);
                requestServer.setObjectTransport(map);
                requestServer.request(this, new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {

                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            JSONObject ob = null;
                            try {
                                ob = new JSONObject(object.toString());
                                ob = ob.getJSONObject("map");

                                HotUser userResponse = new Gson().fromJson(object.toString(), HotUser.class);
                                userResponse.setDeviceID(android_id);
                                userResponse.setName(map.get("name"));
                                userResponse.setEmail(map.get("email"));
                                userResponse.setSocialLogin(map.get("social_media"));
                                userResponse.setId(ob.getInt("result"));
                                userResponse.setSecret(ob.getString("secret"));
                                userResponse.setIdCustomer(ob.getInt("id_customer"));

                                if (userResponse != null && userResponse.getId() > 0) {
                                    setUser(userResponse);
                                    CacheApp.saveAccountUser(SplashActivity.this, userResponse);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            HotUser user = new Gson().fromJson(json, HotUser.class);
            setUser(user);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CustomApplication.hideSystemUI(this);
    }
}