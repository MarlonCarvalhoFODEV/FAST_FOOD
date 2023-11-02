package apis.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hotdelivery.R;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import apis.encrypt.AES;

import static android.content.Context.MODE_PRIVATE;

public class CacheAPI {

    public static  void saveObject(Context context, String id, Object object, boolean cryptography){
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_db),MODE_PRIVATE);
        if(!cryptography)
            sp.edit().putString(id, new Gson().toJson(object)).apply();
        else if(object != null) {
            try {
                sp.edit().putString(id, AES.encryptToAESJson(object)).apply();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getObjectJson(Context context, String id, boolean cryptography){
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_db), MODE_PRIVATE);
        String objectJsonAES =  sp.getString(id,id);

        boolean isJson = objectJsonAES.contains("{")
                && objectJsonAES.contains("}")
                && objectJsonAES.contains(":");

        if(!cryptography && (!cryptography && isJson)){
            return objectJsonAES ;
        } else if(objectJsonAES != null){
            try {
                try {
                    return AES.decryptToObject(objectJsonAES);
                } catch (JsonSyntaxException e){
                    return null ;
                }
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }

        return null ;
    }

    public static boolean haveObject(Context context, String id){
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sp_db),MODE_PRIVATE);
        return (sp.getString(id,id) != null);
    }

}
