package com.c.hotburguerdelivery.apis.login;

import android.content.Context;

public interface LoginInterface {

    public void isLogged(boolean logged);

    public Context getContext();

    public void onSignInloading(boolean show);

    public void onSignInSucess(Object object);

    public void onSignInFail(Exception e);

}
