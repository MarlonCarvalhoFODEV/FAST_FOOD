package apis.pagarme.system;

import apis.encrypt.Cryptograph;
import apis.pagarme.transaction.Payment;

public class SystemConfigurations {

    private int id;
    private String idStoreRecipient ;
    private CryptographSys cryptographSys ;
    private PaymentSys paymentSys ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdStoreRecipient() {
        return idStoreRecipient;
    }

    public void setIdStoreRecipient(String idStoreRecipient) {
        this.idStoreRecipient = idStoreRecipient;
    }

    public CryptographSys getCryptographSys() {
        return cryptographSys;
    }

    public void setCryptographSys(CryptographSys cryptographSys) {
        this.cryptographSys = cryptographSys;
    }

    public PaymentSys getPaymentSys() {
        return paymentSys;
    }

    public void setPaymentSys(PaymentSys paymentSys) {
        this.paymentSys = paymentSys;
    }
}
