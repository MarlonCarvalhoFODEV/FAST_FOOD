package com.hotdelivery.fragments.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.gson.Gson;
import com.hotdelivery.R;
import com.hotdelivery.activitys.a2_register.RegisterInterface;
import com.hotdelivery.fragments.dialogs.DialogInformative;
import com.hotdelivery.itens.deliveryman.HotDeliveryMan;
import com.hotdelivery.utils.EndPointMapping;
import com.hotdelivery.utils.UIController;

import apis.androidid.APIDeviceID;
import apis.cache.CacheAPI;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.internet.InternetRequisitionResponse;
import apis.login.APILogin;
import apis.login.LoginInterface;
import apis.login.facebook.SignInFacebook;

import com.hotdelivery.aplication.CustomApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

public class FMT_01_REG_EMAIL extends Fragment implements LoginInterface {

    private RegisterInterface registerInterface;
    private Controller controller;

    public FMT_01_REG_EMAIL(RegisterInterface interfaceReg) {
        this.registerInterface = interfaceReg;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //CustomApplication.getSocialLogin().setLoginInterface(this);

        View view = inflater.inflate(R.layout.fmt_reg_1_email, null);
        controller = new Controller(view, this);

        return view;
    }

    public Controller getController() {
        return controller;
    }

    private void saveEmail(HotDeliveryMan account) {
        registerInterface.showLoading(true);
        JSONObject userAccount = new JSONObject();

        try {
            userAccount.put("name", account.getName());
            userAccount.put("email", account.getEmail());
            userAccount.put("id_device", account.getIdDevice());
            userAccount.put("hash_key", account.getHashKey());
            userAccount.put("social_login", account.getSocialLogin());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
        requestServer.setIdRequisition("saveEmail()");
        requestServer.setTypeRequest(InternetRequisition.TypeRequest.POST);
        requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.RSA);
        requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
        requestServer.setDomain(getString(R.string.domain_connection));
        requestServer.setController(EndPointMapping.DeliveryMan.CONTROLLER);
        requestServer.setFunction(EndPointMapping.DeliveryMan.Functions.NEW_EMAIL);
        requestServer.setObjectTransport(userAccount);
        requestServer.request(getContext(), new InternetRequisitionResponse() {
            @Override
            public void onLoading(boolean loading) {
            }

            @Override
            public void onSucess(Object object) {
                if (object != null) {
                    HotDeliveryMan obj = new Gson().fromJson(object.toString(), HotDeliveryMan.class);
                    int id = obj.getId();

                    if (id > 0 || CustomApplication.getSocialLogin().isLogged()) {

                        HotDeliveryMan faccount = new HotDeliveryMan();
                        faccount.setId(id);
                        faccount.setIdDevice(account.getIdDevice());
                        faccount.setIdRecipient(obj.getIdRecipient());
                        faccount.setName(account.getName());
                        faccount.setEmail(account.getEmail());
                        faccount.setHashKey(account.getHashKey());

                        if (id > 0) {
                            CacheAPI.saveObject(getContext(), HotDeliveryMan.class.getSimpleName(), faccount, true);
                            CustomApplication.setAccount(faccount);
                        } else {
                            String jsonObject = CacheAPI.getObjectJson(getContext(), HotDeliveryMan.class.getSimpleName(), true);
                            HotDeliveryMan account = new Gson().fromJson(jsonObject, HotDeliveryMan.class);
                            if (account != null)
                                CustomApplication.setAccount(account);
                        }

                        registerInterface.showLoading(false);

                        if(faccount == null ){
                            registerInterface.nextStep(1);
                        }
                        else if(faccount.getId() > 0 && faccount.getIdRecipient() == null){
                            registerInterface.nextStep(2);
                        }
                        else if(faccount.getId() > 0 && faccount.getIdRecipient() != null){
                            registerInterface.nextStep(3);
                        }
                        else if(faccount.getId() > 0 && faccount.getIdRecipient() != null && faccount.getStatus().equals(HotDeliveryMan.Status.ACTIVE)){
                            /*
                                Intent intent = new Intent(this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            */
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void isLogged(boolean logged) {
        if (logged) {
            HotDeliveryMan userAccount = CustomApplication.getAccount();
            userAccount.setIdDevice(APIDeviceID.getIdDevice(getContext()));
            userAccount.setHashKey(APIDeviceID.getHashKey(getContext(), "com.hotdelivery"));

            GoogleSignInAccount gAc = CustomApplication.getSocialLogin().getSignInGoogle().getAccount();
            SignInFacebook.FacebookAccount fAc = CustomApplication.getSocialLogin().getSignInFacebook().getAccount();

            if (gAc != null && gAc.getClass().getSimpleName().equals(GoogleSignInAccount.class.getSimpleName())
                    && gAc.getDisplayName() != null) {
                userAccount.setEmail(gAc.getEmail());
                userAccount.setName(gAc.getDisplayName());
                userAccount.setSocialLogin(APILogin.Type.GOOGLE);
            } else if (fAc != null && fAc.getClass().getSimpleName().equals(SignInFacebook.FacebookAccount.class.getSimpleName())) {

                userAccount.setEmail(fAc.getEmail());
                userAccount.setName(fAc.getName());
                userAccount.setSocialLogin(APILogin.Type.FACEBOOK);
            }

            if (userAccount != null && userAccount.getName() != null && userAccount.getEmail() != null) {
                CustomApplication.setAccount(userAccount);
                saveEmail(userAccount);
            }
        }
    }

    @Override
    public void onSignInloading(boolean show) {
        registerInterface.showLoading(show);
    }

    @Override
    public void onSignInSucess(Object object) {

        HotDeliveryMan userAccount = CustomApplication.getAccount();
        userAccount.setIdDevice(APIDeviceID.getIdDevice(getContext()));
        userAccount.setHashKey(APIDeviceID.getHashKey(getContext(), "com.hotdelivery"));

        if (object != null && object.getClass().getSimpleName().equals(GoogleSignInAccount.class.getSimpleName())) {
            GoogleSignInAccount signInAccount = (GoogleSignInAccount) object;

            userAccount.setEmail(signInAccount.getEmail());
            userAccount.setName(signInAccount.getDisplayName());
            userAccount.setSocialLogin(APILogin.Type.GOOGLE);

        } else if (object != null && object.getClass().getSimpleName().equals(SignInFacebook.FacebookAccount.class.getSimpleName())) {
            SignInFacebook.FacebookAccount faceAccount = (SignInFacebook.FacebookAccount) object;
            userAccount.setEmail(faceAccount.getEmail());
            userAccount.setName(faceAccount.getName());
            userAccount.setSocialLogin(APILogin.Type.FACEBOOK);
        }

        if (userAccount != null && userAccount.getName() != null && userAccount.getEmail() != null) {
            CustomApplication.setAccount(userAccount);
            saveEmail(userAccount);
        }
    }

    @Override
    public void onSignInFail(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        CustomApplication.getSocialLogin().getSignInGoogle().onActivityResult(requestCode, resultCode, data);
        CustomApplication.getSocialLogin().getSignInFacebook().onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class Controller implements View.OnClickListener {

        private TextView tvTermsAndPolitics ;
        private SignInButton btnSigninGoogle;
        private LoginButton btnSigninFacebook;

        public Controller(View view, LoginInterface loginInterface) {
            btnSigninGoogle = view.findViewById(R.id.btn_google_sign);
            btnSigninFacebook = view.findViewById(R.id.login_button);
            btnSigninFacebook.setFragment(FMT_01_REG_EMAIL.this);

            tvTermsAndPolitics = view.findViewById(R.id.tv_terms_and_politics);
            tvTermsAndPolitics.setOnClickListener(this);

            CustomApplication.getSocialLogin().setLoginInterface(loginInterface);
            CustomApplication.getSocialLogin().getSignInGoogle().setButtonGoogle(btnSigninGoogle);
            CustomApplication.getSocialLogin().getSignInFacebook().setButtonFacebook(btnSigninFacebook);
            CustomApplication.getSocialLogin().getSignInFacebook().addPermission(SignInFacebook.Permissions.EMAIL);
            CustomApplication.getSocialLogin().getSignInFacebook().addPermission(SignInFacebook.Permissions.FIRST_NAME);
            CustomApplication.getSocialLogin().getSignInFacebook().addPermission(SignInFacebook.Permissions.LAST_NAME);
            CustomApplication.getSocialLogin().getSignInFacebook().addPermission(SignInFacebook.Permissions.ID);

            isLogged(CustomApplication.getSocialLogin().isLogged());
        }

        @Override
        public void onClick(View view) {
            WebView webView = new WebView(getContext());
            webView.loadUrl("file:///android_asset/terms_of_politics.html");

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
    }
}
