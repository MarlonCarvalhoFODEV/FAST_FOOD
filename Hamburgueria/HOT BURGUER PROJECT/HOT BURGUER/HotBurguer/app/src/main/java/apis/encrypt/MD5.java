package apis.encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 { 

    public static String encrypt(byte[] bytes){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

            BigInteger hash = new BigInteger(1, md.digest(bytes));
            return hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null ;
    }

}
