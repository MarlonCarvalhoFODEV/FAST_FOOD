package com.c.hotburguerdelivery.acitivtys.a1_splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.c.hotburguerdelivery.R;
import com.c.hotburguerdelivery.acitivtys.a2_register_activity.RegisterActivity;
import com.c.hotburguerdelivery.acitivtys.bdelivery.DeliveryManActivity;
import com.c.hotburguerdelivery.apis.cache.CacheAPI;
import com.c.hotburguerdelivery.apis.internet.InternetRequisition;
import com.c.hotburguerdelivery.apis.internet.InternetRequisitionAPI;
import com.c.hotburguerdelivery.apis.internet.InternetRequisitionResponse;
import com.c.hotburguerdelivery.apis.login.facebook.SignInFacebook;
import com.c.hotburguerdelivery.application.CustomApplication;
import com.c.hotburguerdelivery.itens.HotOrder;
import com.c.hotburguerdelivery.itens.HotUser;
import com.c.hotburguerdelivery.zutils.webservice.controller.WebController;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.c.hotburguerdelivery.application.CustomApplication.setUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        nextActiviy(2500);
    }

    private void nextActiviy(int timeMills) {
        final boolean logged = CustomApplication.getSocialLogin().isLogged();
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

                String obJson = CacheAPI.getObjectJson(SplashActivity.this, HotOrder.class.getSimpleName(),  true);

                HotOrder order = null ;
                if(obJson != null)
                    order = new Gson().fromJson(obJson, HotOrder.class);

                if(order != null && order.getId() > 0){

                    /*Intent intent = new Intent(SplashActivity.this, TrackOrderActivity.class);
                    intent.putExtra("order_in_process",order);
                    startActivity(intent);
                    finish();*/
                } else {
                    if (!logged) {
                        Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, DeliveryManActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, timeMills);
    }

    private final void registerUser() {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        GoogleSignInAccount googAc = CustomApplication.getSocialLogin().getSignInGoogle().getAccount();
        SignInFacebook.FacebookAccount faceAc = CustomApplication.getSocialLogin().getSignInFacebook().getAccount();

        String json = CacheAPI.getObjectJson(SplashActivity.this, HotUser.class.getSimpleName(), true);

        if(json == null){
            if (googAc != null || faceAc != null) {

                Map<String, String> map = new HashMap<>();
                map.put("email", googAc != null ? googAc.getEmail() : faceAc.getEmail());
                map.put("name", googAc != null ? googAc.getDisplayName() : faceAc.getName());
                map.put("id_android", android_id);
                map.put("social_media", (googAc != null ? "google" : "facebook"));

                InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
                requestServer.setIdRequisition("registerUser()");
                requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
                requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
                requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
                requestServer.setDomain(getString(R.string.domain_connection));
                requestServer.setController(WebController.Collaborator.CONTROLLER);
                requestServer.setFunction(WebController.Collaborator.Functions.LOGIN);
                requestServer.setObjectTransport(map);
                requestServer.request(this, new InternetRequisitionResponse() {
                    @Override
                    public void onLoading(boolean loading) {

                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {
                            HotUser userResponse = new Gson().fromJson(object.toString(), HotUser.class);

                            if (userResponse != null && userResponse.getId() > 0) {
                                setUser(userResponse);
                                CacheAPI.saveObject(SplashActivity.this, HotUser.class.getSimpleName(), userResponse, true);
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        } else {
            HotUser user = new Gson().fromJson(json, HotUser.class);
            setUser(user);
        }

    }

}