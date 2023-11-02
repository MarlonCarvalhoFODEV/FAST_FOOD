package apis.encrypt;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptHash {

    public static void main(String[] args){

    }

    public String generateHash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean hashEquals(String password, String hash2){
        return BCrypt.checkpw(password, hash2);
    }

}
