package apis.encrypt;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static String chaveSimetricaDefault = MD5.encrypt("5554744558984447789663224455887744555".getBytes());

    public static byte[] encrypt(String mensagem) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        SecretKey key = new SecretKeySpec(chaveSimetricaDefault.getBytes(),"AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(mensagem.getBytes("UTF-8"));
    }

    public static byte[] decrypt(byte[] mensagemEncriptada) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        SecretKey key = new SecretKeySpec(chaveSimetricaDefault.getBytes(),"AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(mensagemEncriptada);
    }

    public static String encryptToAESJson(Object object) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] msgEncrypted = encrypt(new Gson().toJson(object));

        return new Gson().toJson(msgEncrypted);
    }

    public static String decryptToJson(String msgCryptogra) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] mensagemEncriptada = new Gson().fromJson(msgCryptogra, byte[].class);
        byte[] msgDecrypted = decrypt(mensagemEncriptada);
        return new String(msgDecrypted, "UTF-8");
    }

    public final void setKey(String key){
        if(key != null){
            chaveSimetricaDefault = MD5.encrypt(key.getBytes());
        }
    }
}
