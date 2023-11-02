package apis.encrypt;


import apis.pagarme.userbuyer.BCard;
import com.google.gson.Gson;
import org.springframework.util.Base64Utils;

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

    public static void main(String[] args) throws Exception {



        FileInputStream fin = new FileInputStream("src/private.cry");
        ObjectInputStream oin = new ObjectInputStream(fin);

        FileInputStream fin1 = new FileInputStream("src/public.cry");
        ObjectInputStream oin1 = new ObjectInputStream(fin1);

        PublicKey publicKey = (PublicKey) oin1.readObject();
        PrivateKey privateKey = (PrivateKey) oin.readObject();
        RSA rsa = new RSA(publicKey,privateKey, 1024);

        BCard card = new BCard();
        card.setBrand("Master Card");
        card.setId("12522344");
        card.setNumber("1452");
        card.setCVV("455");
        card.setExpirationDate("02-20");
        card.setPaymentMethod(BCard.PaymentMethod.CREDIT_CARD);


        String publiKeyBASE = Base64Utils.encodeToString(publicKey.getEncoded());
        KeyFactory factory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
        PublicKey pubRecover = factory.generatePublic(new X509EncodedKeySpec(publicKey.getEncoded()));

        rsa = new RSA(pubRecover,privateKey, 2048);
        byte[] cardJsonRSA = rsa.encrypt(new Gson().toJson(card));

        String cardJson =  rsa.decrypt(cardJsonRSA);
        BCard recoverCard = new Gson().fromJson(cardJson, BCard.class);
        System.out.println("CARD: "+recoverCard.getBrand());
    }

}
