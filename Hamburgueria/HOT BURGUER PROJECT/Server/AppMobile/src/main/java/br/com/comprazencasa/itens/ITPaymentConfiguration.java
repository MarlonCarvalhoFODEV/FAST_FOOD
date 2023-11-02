package br.com.comprazencasa.itens;

import java.io.Serializable;

public class ITPaymentConfiguration implements Serializable {

    private int maxInstallments, maxWithoutFEE, minimumPurchaseInstallments, minimumPurchase ;
    private double feeDelivery,averageRating, distanceInKM, feeInstallments;

    public int getMaxInstallments() {
        return maxInstallments;
    }

    public void setMaxInstallments(int maxInstallments) {
        this.maxInstallments = maxInstallments;
    }

    public int getMaxWithoutFEE() {
        return maxWithoutFEE;
    }

    public void setMaxWithoutFEE(int maxWithoutFEE) {
        this.maxWithoutFEE = maxWithoutFEE;
    }

    public int getMinimumPurchaseInstallments() {
        return minimumPurchaseInstallments;
    }

    public void setMinimumPurchaseInstallments(int minimumPurchaseInstallments) {
        this.minimumPurchaseInstallments = minimumPurchaseInstallments;
    }

    public int getMinimumPurchase() {
        return minimumPurchase;
    }

    public void setMinimumPurchase(int minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
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

    public double getFeeInstallments() {
        return feeInstallments;
    }

    public void setFeeInstallments(double feeInstallments) {
        this.feeInstallments = feeInstallments;
    }
}
