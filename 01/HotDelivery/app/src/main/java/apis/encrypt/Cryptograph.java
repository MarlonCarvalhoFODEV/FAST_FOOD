package apis.encrypt;

import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cryptograph {
    private static final String ALGORITHM_AES = "AES";
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_RSA = "RSA";

    private static final byte[] keyValue =
    new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    public static String encrypt(String valueToEnc) throws Exception

    {

        Key key = generateKey();

//Get the instance for AES algorithm

        Cipher c = Cipher.getInstance(ALGORITHM_AES);

        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encValue = c.doFinal(valueToEnc.getBytes());

        String encryptedValue = Base64.getEncoder().encodeToString(encValue);

        return encryptedValue;

    }

    public static String decrypt(String encryptedValue) throws Exception

    {

        Key key = generateKey();

//Get the instance for AES algorithm

        Cipher c = Cipher.getInstance(ALGORITHM_AES);

        c.init(Cipher.DECRYPT_MODE, key);

        byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);

        byte[] decValue = c.doFinal(decordedValue);

        String decryptedValue = new String(decValue);

        return decryptedValue;

    }

    public static String decryptToJson(String jsonCrypetd) {
        try {
            String crypto = new Gson().fromJson(jsonCrypetd, String.class);
            return Cryptograph.decrypt(crypto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptToJson(Object object) {
        try {
            String jsonObject = new Gson().toJson(object);
            String crypto = Cryptograph.encrypt(jsonObject);

            return new Gson().toJson(crypto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Key generateKey() throws Exception

    {

        Key key = new SecretKeySpec(keyValue, ALGORITHM_AES);

//Below Shown is the code for key generation for DES algorithm

// SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);

// key = keyFactory.generateSecret(new DESKeySpec(keyValue));

        return key;

    }


}
