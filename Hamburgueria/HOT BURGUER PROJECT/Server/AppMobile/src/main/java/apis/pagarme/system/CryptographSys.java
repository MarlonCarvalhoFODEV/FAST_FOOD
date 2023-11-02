package apis.pagarme.system;

import java.security.PrivateKey;
import java.security.PublicKey;

public class CryptographSys {

    private InternRSA rsa = new InternRSA();

    public InternRSA getRsa() {
        return rsa;
    }

    public class InternRSA{
        private PrivateKey privateKey ;
        private PublicKey publicKey ;

        private PrivateKey exPrivateKey ;
        private PublicKey exPublicKey ;

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(PrivateKey privateKey) {
            this.privateKey = privateKey;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(PublicKey publicKey) {
            this.publicKey = publicKey;
        }

        public PrivateKey getExPrivateKey() {
            return exPrivateKey;
        }

        public void setExPrivateKey(PrivateKey exPrivateKey) {
            this.exPrivateKey = exPrivateKey;
        }

        public PublicKey getExPublicKey() {
            return exPublicKey;
        }

        public void setExPublicKey(PublicKey exPublicKey) {
            this.exPublicKey = exPublicKey;
        }
    }
}
