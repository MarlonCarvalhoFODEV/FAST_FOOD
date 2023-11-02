package com.c.hotburguerdelivery.apis.userbuyer;

import java.io.Serializable;

public class BAddress implements Serializable {
 
    private int shippingAddress, feeDelivery;
    private String city,state , country, neighborhood , street , zipCode, streetNumber, complement ;
    private boolean expedited ;

    public int getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(int shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public int getFeeDelivery() {
        return feeDelivery;
    }

    public void setFeeDelivery(int feeDelivery) {
        this.feeDelivery = feeDelivery;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country.toLowerCase();
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public boolean isExpedited() {
        return expedited;
    }

    public void setExpedited(boolean expedited) {
        this.expedited = expedited;
    }
}
