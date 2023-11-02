package com.hotburguer.activitys.a2_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hotburguer.R;
import com.hotburguer.activitys.a5_home.HomeBurguerActivity;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;
import com.hotburguer.fragments.dialogs.DialogInformative;
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
import apis.login.LoginInterface;
import apis.login.facebook.SignInFacebook;

import static com.hotburguer.acustom.CustomApplication.getUser;
import static com.hotburguer.acustom.CustomApplication.setUser;

public class RegisterActivity extends AppCompatActivity implements LoginInterface, View.OnClickListener {
    private TextView tvTermsAndPolitics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.tb_register);
        setSupportActionBar(toolbar);

        tvTermsAndPolitics = findViewById(R.id.tv_terms_and_politics);
        tvTermsAndPolitics.setOnClickListener(this);

        CustomApplication.hideSystemUI(this);
        CustomApplication.getSocialLogin().setLoginInterface(this);
        CustomApplication.getSocialLogin().getSignInGoogle().setButtonGoogle(findViewById(R.id.btn_google_sign));
        CustomApplication.getSocialLogin().getSignInFacebook().setButtonFacebook(findViewById(R.id.btn_facebook_sign));

        isLogged(CustomApplication.getSocialLogin().isLogged());
    }

    private void showProgress(boolean show) {
        View view = findViewById(R.id.fl_progress);
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CustomApplication.hideSystemUI(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CustomApplication.getSocialLogin().getSignInGoogle().onActivityResult(requestCode, resultCode, data);
        CustomApplication.getSocialLogin().getSignInFacebook().onActivityResult(requestCode, resultCode, data);
    }

    private HotUser hotUserTest(){
        HotUser user = new HotUser();
        user.setId(5565);
        user.setDeviceID("00000000");
        user.setName("User Testing");
        user.setEmail("usertesting@gmail.com");
        user.setIdCustomer(54555);
        user.setSecret("secret");

        return user;
    }

    private void openHomeBurguer() {
        Intent intent = new Intent(this, HomeBurguerActivity.class);
        startActivity(intent);
        finish();
    }

    private void openHomeBurguer(int delay) {
        if(delay > 0){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(()->{
                openHomeBurguer();
            },delay);
        }
        else{
            openHomeBurguer();
        }
    }

    private void termsAndPolitics() {
        WebView webView = new WebView(getContext());
        webView.loadUrl("file:///android_asset/terms_and_politic.html");

        DialogInformative dialog = new DialogInformative(getContext());
        dialog.setTitle("Termos e\nPolitica de Privacidade");
        dialog.setMessage("");
        dialog.setCustomView(webView);
        dialog.addButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void registerNewUser2() {
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        GoogleSignInAccount googAc = CustomApplication.getSocialLogin().getSignInGoogle().getAccount();
        SignInFacebook.FacebookAccount faceAc = CustomApplication.getSocialLogin().getSignInFacebook().getAccount();

        String json = CacheAPI.getObjectJson(this, HotUser.class.getSimpleName(), true);
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
                        showProgress(loading);
                    }

                    @Override
                    public void onSucess(Object object) {
                        if (object != null) {

                            try {
                                JSONObject ob = new JSONObject(object.toString());
                                ob = ob.getJSONObject("map");

                                boolean deviceRegistered = ob.has("device_registered")?ob.getBoolean("device_registered"):false;

                                if(!deviceRegistered){


                                    int result = ob.getInt("result");
                                    HotUser user = new HotUser();
                                    user.setId(result);
                                    user.setDeviceID(android_id);
                                    user.setName(googAc != null ? googAc.getDisplayName() : faceAc.getName());
                                    user.setEmail(googAc != null ? googAc.getEmail() : faceAc.getEmail());
                                    user.setIdCustomer(ob.getInt("id_customer"));
                                    user.setSecret(ob.getString("secret"));

                                    if (user != null && user.getId() > 0) {
                                        setUser(user);
                                        CacheApp.saveAccountUser(RegisterActivity.this, user);

                                        openHomeBurguer();
                                    } else {
                                        CustomApplication.getSocialLogin().logout();
                                        Toast.makeText(RegisterActivity.this, "Erro", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    CustomApplication.getSocialLogin().logout();
                                    Toast.makeText(RegisterActivity.this, "Você pode ter apenas 1 conta!", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                CustomApplication.getSocialLogin().logout();
                                Toast.makeText(RegisterActivity.this, "Erro", Toast.LENGTH_LONG).show();
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
            if (user != null && user.getId() > 0) {
                setUser(user);
                openHomeBurguer(1000);

            } else {
                Log.i("KKKFDKSFDKSKFDS","LAST ELSING ");
            }
        }
    }

    @Override
    public void isLogged(boolean logged) {
        if (logged && getUser().getId() > 0) {
            openHomeBurguer();
        }
    }

    @Override
    public Context getContext() {
        return RegisterActivity.this;
    }

    @Override
    public void onSignInloading(boolean show) {
        showProgress(show);
    }

    @Override
    public void onSignInSucess(Object object) {
        /*if(object.getClass().getSimpleName().equals(GoogleSignInAccount.class.getSimpleName())){
            registerNewUser();
            openHomeBurguer();
        } 
        else if(object.getClass().getSimpleName().equals(LoginResult.class.getSimpleName())){
            registerNewUser();
            openHomeBurguer();
        }*/

        if(!CustomApplication.TEST_MODE){
            registerNewUser2();
        }
        else{
            HotUser userTest = hotUserTest();

            if (userTest != null && userTest.getId() > 0) {
                setUser(userTest);
                CacheApp.saveAccountUser(RegisterActivity.this, userTest);

                openHomeBurguer(1000);
            } else {
                CustomApplication.getSocialLogin().logout();
                Toast.makeText(RegisterActivity.this, "Erro", Toast.LENGTH_LONG).show();
            }
        }

        //openHomeBurguer();
    }

    @Override
    public void onSignInFail(Exception e) {
        if(!CustomApplication.TEST_MODE) {
            CustomApplication.getSocialLogin().logout();
        }
        else{
            HotUser userTest = hotUserTest();

            if (userTest != null && userTest.getId() > 0) {
                setUser(userTest);
                CacheApp.saveAccountUser(RegisterActivity.this, userTest);

                openHomeBurguer(1000);
            } else {
                CustomApplication.getSocialLogin().logout();
                Toast.makeText(RegisterActivity.this, "Erro", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        termsAndPolitics();
    }
}