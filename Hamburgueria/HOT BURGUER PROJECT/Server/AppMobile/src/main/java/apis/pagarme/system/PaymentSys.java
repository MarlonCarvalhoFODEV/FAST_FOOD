package apis.pagarme.system;

import apis.pagarme.recipient.RRecipient;

public class PaymentSys {

    private String systemId ;
    private int commission;
    private RRecipient recipient ;

    public PaymentSys(){
        this.recipient = new RRecipient();
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public RRecipient getRecipient() {
        return recipient;
    }
}
