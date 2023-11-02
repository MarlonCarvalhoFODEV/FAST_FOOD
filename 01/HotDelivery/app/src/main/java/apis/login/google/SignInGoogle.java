package apis.login.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.internal.PlatformServiceClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import apis.login.LoginInterface;

public class SignInGoogle implements View.OnClickListener {

    private Context context;
    private SignInButton btnGoogle;
    private LoginInterface loginInterface;
    private GoogleSignInAccount account;
    private GoogleSignInClient gsic;
    private static final int GOOGLE_SIGN_IN = 0;

    public SignInGoogle(Context context) {
        this.context = context;
    }

    public SignInGoogle(LoginInterface logInterface) {
        this.loginInterface = logInterface;
    }

    public void setButtonGoogle(SignInButton button) {
        this.context = button.getContext();
        this.btnGoogle = button;
        this.btnGoogle.setOnClickListener(this);
    }

    private void signInGoogle() {
        loginInterface.onSignInloading(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsic = GoogleSignIn.getClient(context, gso);

        Intent signInIntent = gsic.getSignInIntent();
        ((Activity) context).startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public boolean isLogged() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        this.account = account;
        return (account != null);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            this.account = account;
            this.loginInterface.onSignInloading(false);
            this.loginInterface.onSignInSucess(account);
        } catch (ApiException e) {
            this.loginInterface.onSignInloading(false);
            this.loginInterface.onSignInFail(e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void logout() {
        if (gsic != null) {
            gsic.signOut().addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        signInGoogle();
    }
}
