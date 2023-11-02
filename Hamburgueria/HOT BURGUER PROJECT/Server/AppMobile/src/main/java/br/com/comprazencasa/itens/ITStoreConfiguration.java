package br.com.comprazencasa.itens;

import apis.pagarme.userbuyer.BAccountBank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ITStoreConfiguration implements Serializable {

    private int idStore, timeMinDelivery, timeMaxDelivery ;
    private double feeDelivery, averageRating, distanceInKM ;
    private String dateCreated, dateLastUpdated ;

    private Map<Integer,ITDayOperation> daysOperations ;
    private BAccountBank accountBank ;
    private ITPaymentConfiguration paymentConfiguration ;

    public ITStoreConfiguration(){
        this.daysOperations = new HashMap<>();
        this.paymentConfiguration = new ITPaymentConfiguration();
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getTimeMinDelivery() {
        return timeMinDelivery;
    }

    public void setTimeMinDelivery(int timeMinDelivery) {
        this.timeMinDelivery = timeMinDelivery;
    }

    public int getTimeMaxDelivery() {
        return timeMaxDelivery;
    }

    public void setTimeMaxDelivery(int timeMaxDelivery) {
        this.timeMaxDelivery = timeMaxDelivery;
    }

    public double getFeeDelivery() {
        return feeDelivery;
    }

    public void setFeeDelivery(double feeDelivery) {
        this.feeDelivery = feeDelivery;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getDistanceInKM() {
        return distanceInKM;
    }

    public void setDistanceInKM(double distanceInKM) {
        this.distanceInKM = distanceInKM;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(String dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public Map<Integer, ITDayOperation> getDaysOperations() {
        return daysOperations;
    }

    public void setDaysOperations(Map<Integer, ITDayOperation> daysOperations) {
        this.daysOperations = daysOperations;
    }

    public BAccountBank getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(BAccountBank accountBank) {
        this.accountBank = accountBank;
    }

    public ITPaymentConfiguration getPaymentConfiguration() {
        return paymentConfiguration;
    }

    public void setPaymentConfiguration(ITPaymentConfiguration paymentConfiguration) {
        this.paymentConfiguration = paymentConfiguration;
    }
}
