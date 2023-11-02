/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.collaborator;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class ITOrderBuyer implements Serializable, Comparable<ITOrderBuyer>{

    public static final String STATUS_WAIT = "wait";
    public static final String STATUS_IN_PRODUCTION = "in_production";
    public static final String STATUS_FOR_DELIVERY = "for_delivery";
    public static final String STATUS_DELIVERY_ON_MY_WAY = "on_my_way";
    public static final String STATUS_FINALIZED = "finalized";

    private int idUser, idBuyer, idDeliveryMan, orderType = 1, intallment, paymentLocal;
    private boolean haveMore ;
    private String idOrder, status, clientName, clientEmail,emailBuyer, cel, companyFunction, cep, date, hour ;
    private String county, city, neighborhood, street, complements, purchaseValue ;
    private double distanceKm, latitude, longitude ;

    public ITOrderBuyer() {}

    public ITOrderBuyer(String idOrder, double distance, String cep) {
        this.idOrder = idOrder ;
        this.distanceKm = distance ;
        this.cep = cep ;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(int idBuyer) {
        this.idBuyer = idBuyer;
    }

    public int getIdDeliveryMan() {
        return idDeliveryMan;
    }

    public void setIdDeliveryMan(int idDeliveryMan) {
        this.idDeliveryMan = idDeliveryMan;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getIntallment() {
        return intallment;
    }

    public void setIntallment(int intallment) {
        this.intallment = intallment;
    }

    public int getPaymentLocal() {
        return paymentLocal;
    }

    public void setPaymentLocal(int paymentLocal) {
        this.paymentLocal = paymentLocal;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getEmailBuyer() {
        return emailBuyer;
    }

    public void setEmailBuyer(String emailBuyer) {
        this.emailBuyer = emailBuyer;
    }

    public String getCompanyFunction() {
        return companyFunction;
    }

    public void setCompanyFunction(String companyFunction) {
        this.companyFunction = companyFunction;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getComplements() {
        return complements;
    }

    public void setComplements(String complements) {
        this.complements = complements;
    }

    public String getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(String purchaseValue) {
        this.purchaseValue = purchaseValue;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    @Override
    public int compareTo(ITOrderBuyer o) {
        if (this.distanceKm < o.getDistanceKm()) {
            return -1;
        }
        if (this.distanceKm > o.getDistanceKm()) {
            return 1;
        }
       return 0;
    }
}
