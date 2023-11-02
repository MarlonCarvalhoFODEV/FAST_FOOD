package com.hotdelivery.activitys.a1_splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.hotdelivery.R;
import com.hotdelivery.activitys.a2_register.RegisterActivity;
import com.hotdelivery.aplication.CustomApplication;
import com.hotdelivery.itens.deliveryman.HotDeliveryMan;
import com.hotdelivery.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import apis.androidid.APIDeviceID;
import apis.cache.CacheAPI;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Utils.SystemUI.hideSystemUI(this);

        nextActiviy(2500);
    }

    private void nextActiviy(int timeMills) {
        //final boolean logged = CustomApplication.getSocialLogin().isLogged();
        //registerUser();

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

//                String obJson = CacheAPI.getObjectJson(SplashActivity.this, HotOrder.class.getSimpleName(),  true);

                String jsonAccount = CacheAPI.getObjectJson(SplashActivity.this, HotDeliveryMan.class.getSimpleName(), true);
                HotDeliveryMan ac = new Gson().fromJson(jsonAccount, HotDeliveryMan.class);

                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                int step = 0 ;
                if(ac == null ){
                    step = 1 ;
                    intent.putExtra("step", step);
                }
                else if(ac.getId() > 0 && ac.getIdRecipient() == null){
                    step = 2 ;
                    intent.putExtra("step", step);
                }
                else if(ac.getId() > 0 && ac.getIdRecipient() != null){
                    step = 3 ;
                    intent.putExtra("step", step);
                }
                else if(ac.getId() > 0 && ac.getIdRecipient() != null && ac.getStatus().equals(HotDeliveryMan.Status.ACTIVE)){
                    step = 4 ;
                    intent.putExtra("step", step);
                }

                startActivity(intent);
                finish();
            }
        }, timeMills);
    }
}