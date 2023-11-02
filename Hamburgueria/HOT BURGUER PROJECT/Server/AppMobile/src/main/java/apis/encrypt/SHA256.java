package apis.encrypt;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256 {

    public static final byte[] encrypt(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest.digest(text.getBytes(StandardCharsets.UTF_8));
    }

    public static final String encryptBase64(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return Base64.toBase64String(hash);
    }

    public static String SecureStringHasher(String algo) {
        HashFunction hf ;
        byte[] salt ;

        int saltSize;
        switch (algo.toLowerCase()) {
            case "sha256":
                hf = Hashing.sha256();
                saltSize = 256;
                break;
            case "sha348":
                hf = Hashing.sha384();
                saltSize = 384;
                break;
            case "sha512":
                hf = Hashing.sha512();
                saltSize = 512;
                break;
            default:
                hf = Hashing.sha256();
                saltSize = 256;
        }
        salt = SecureRandom.getSeed(saltSize);

        return Base64.toBase64String(salt);
    }

}
