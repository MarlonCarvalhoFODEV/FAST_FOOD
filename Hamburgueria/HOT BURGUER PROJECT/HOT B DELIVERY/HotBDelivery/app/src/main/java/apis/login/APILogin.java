package apis.login;

import android.content.Context;

import com.google.android.gms.common.SignInButton;

import apis.login.facebook.SignInFacebook;
import apis.login.google.SignInGoogle;

public class APILogin {

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

    public class Type{
        public static final String FACEBOOK = "facebook";
        public static final String GOOGLE = "google";
    }
}
