package apis.userbuyer;

import java.io.Serializable;

public class BCard implements Serializable {

    private String id , number, CVV, holderName, expirationDate, brand, cardHash, firstDigits, lastDigits;
    private int idCustomer, paymentMethod ;
    private boolean onClickBuy ;

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getHolderName() {
        return holderName;
    } 

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCardHash() {
        return cardHash;
    }

    public void setCardHash(String cardHash) {
        this.cardHash = cardHash;
    }

    public String getFirstDigits() {
        return firstDigits;
    }

    public void setFirstDigits(String firstDigits) {
        this.firstDigits = firstDigits;
    }

    public String getLastDigits() {
        return lastDigits;
    }

    public void setLastDigits(String lastDigits) {
        this.lastDigits = lastDigits;
    }

    public boolean isOnClickBuy() {
        return onClickBuy=(id != null || id.length()>0);
    }

    public static class PaymentMethod{
        public static final int CREDIT_CARD = 0;
        public static final int DEBIT_CARD = 1;
        public static final int BILLET = 2;
        public static final int WALLET = 3;
        public static final int PAYPAL = 4;
        public static final int BITCOIN = 5;
    }
}
