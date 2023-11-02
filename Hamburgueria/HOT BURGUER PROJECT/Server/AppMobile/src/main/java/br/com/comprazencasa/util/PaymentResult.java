package br.com.comprazencasa.util;

import java.io.Serializable;

public class PaymentResult implements Serializable {
    private String idTransaction, statusPayment;

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }
}
