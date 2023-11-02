package com.c.hotburguerdelivery.apis.login.facebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.c.hotburguerdelivery.apis.login.LoginInterface;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class SignInFacebook implements FacebookCallback<LoginResult> {

    private Context context;
    private LoginButton btnFacebook;
    private LoginInterface loginInterface;
    private FacebookAccount account;
    private static final int FACEBOOK_SIGN_IN = 1;

    private CallbackManager callbackManager;

    public SignInFacebook(Context context) {
        this.context = context;
    }

    public SignInFacebook(LoginInterface logInterface) {
        this.loginInterface = logInterface;
        this.callbackManager = CallbackManager.Factory.create();
        this.context = loginInterface.getContext();
    }

    public void setButtonFacebook(LoginButton button) {
        this.context = button.getContext();
        this.btnFacebook = button;
        this.btnFacebook.setReadPermissions("name", "email");
        this.btnFacebook.registerCallback(callbackManager, this);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FACEBOOK_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean isLogged() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        setAccount(loginResult);
        loginInterface.onSignInloading(false);
        loginInterface.onSignInSucess(loginResult);
    }

    @Override
    public void onCancel() {
        loginInterface.onSignInloading(false);
        loginInterface.onSignInSucess(null);
    }

    @Override
    public void onError(FacebookException error) {
        loginInterface.onSignInloading(false);
        loginInterface.onSignInFail(error);
    }

    private final void setAccount(LoginResult loginResult) {
        GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject me, GraphResponse response) {
                if (response.getError() != null) {
                    // handle error
                } else {
                    String user_lastname = me.optString("last_name");
                    String user_firstname = me.optString("first_name");
                    String user_email = response.getJSONObject().optString("email");

                    String email = me.optString("email");
                    String id = me.optString("id");

                    account = new FacebookAccount();
                    account.setEmail(email);
                    account.setName(email != null ? email : user_firstname);
                }
            }
        }).executeAsync();
    }

    public FacebookAccount getAccount() {
        return account;
    }

    public class FacebookAccount {
        public String name, email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
