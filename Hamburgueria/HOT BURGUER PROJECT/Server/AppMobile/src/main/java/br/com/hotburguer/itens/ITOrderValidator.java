package br.com.hotburguer.itens;

import apis.date.DateAPI;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ITOrderValidator implements Serializable {
    private int idOrder, idUser, idStore, idTransaction ;
    double feeInstalment, feeDelivery ;
    private String statusPayment, idDevice ;
    private Date createdDate, expirationDate ;
    private boolean blocked ;

    public ITOrderValidator(int idOrder, int addHour, int addMinute, int addSeconds) {
        this.idOrder = idOrder;
        this.createdDate = GregorianCalendar.getInstance(Locale.getDefault()).getTime();
        this.expirationDate = DateAPI.getDateManager().addTimeFromDate(createdDate,addHour,addMinute,addSeconds);
    }

    public int getIdOrder() {
        return idOrder;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public double getFeeInstalment() {
        return feeInstalment;
    }

    public void setFeeInstalment(double feeInstalment) {
        this.feeInstalment = feeInstalment;
    }

    public double getFeeDelivery() {
        return feeDelivery;
    }

    public void setFeeDelivery(double feeDelivery) {
        this.feeDelivery = feeDelivery;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public boolean expiration(){
        return DateAPI.getDateManager().afterDateTime(createdDate, expirationDate);
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
