package com.c.hotburguerdelivery.apis.login;

import android.content.Context;

import com.c.hotburguerdelivery.apis.login.facebook.SignInFacebook;
import com.c.hotburguerdelivery.apis.login.google.SignInGoogle;

public class APILogin {

    private LoginInterface loginInterface ;
    private SignInGoogle signInGoogle ;
    private SignInFacebook signInFacebook ;

    public APILogin(Context context) {
        signInGoogle = new SignInGoogle(context);
        signInFacebook = new SignInFacebook(context);
    }

    public APILogin(LoginInterface loginInterface) {
        signInGoogle = new SignInGoogle(loginInterface);
        signInFacebook = new SignInFacebook(loginInterface);
    }

    public void setLoginInterface(LoginInterface loginInterface) {
        this.loginInterface = loginInterface;
        signInGoogle = new SignInGoogle(loginInterface);
        signInFacebook = new SignInFacebook(loginInterface);
    }

    public SignInGoogle getSignInGoogle() {
        return signInGoogle;
    }

    public SignInFacebook getSignInFacebook() {
        return signInFacebook;
    }

    public boolean isLogged(){
        return signInGoogle.isLogged() || signInFacebook.isLogged();
    }
}
