package apis.pagarme.transfer;

import java.io.Serializable;

public class TTransfer implements Serializable {

    private int countPage, page, amount ;
    private String idRecipient, idTransfer ;

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }

    public String getIdTransfer() {
        return idTransfer;
    }

    public void setIdTransfer(String idTransfer) {
        this.idTransfer = idTransfer;
    }
}
