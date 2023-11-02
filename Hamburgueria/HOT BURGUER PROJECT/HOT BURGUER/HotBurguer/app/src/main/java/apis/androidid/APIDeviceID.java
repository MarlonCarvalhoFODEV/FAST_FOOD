package apis.androidid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.hotburguer.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class APIDeviceID {

    public static String getIdDevice(Context c){
        return Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getHashKey(Context c, String packageRoot){
        try {
            PackageInfo info = c.getPackageManager().getPackageInfo(
                    packageRoot,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null ;
    }

}
