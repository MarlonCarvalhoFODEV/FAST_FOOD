package apis.pagarme.transaction;

import java.io.Serializable;

public class Parcel implements Serializable {

    private int installment ;
    private int amount ;
    private int installment_amount ;

    public Parcel(){ }

    public Parcel(int installment, int amount, int installmentAmount){
        this.installment = installment;
        this.amount = amount ;
        this.installment_amount = installmentAmount ;
    }

    public int getInstallment() {
        return installment;
    }

    public void setInstallment(int installment) {
        this.installment = installment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(int installment_amount) {
        this.installment_amount = installment_amount;
    }
}
