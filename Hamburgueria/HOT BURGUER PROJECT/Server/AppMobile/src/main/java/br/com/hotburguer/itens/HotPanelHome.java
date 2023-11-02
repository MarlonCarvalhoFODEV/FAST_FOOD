package br.com.hotburguer.itens;

import java.io.Serializable;

public class HotPanelHome implements Serializable {

    private int idUser, id, typeView, delivered, offerAccepteds, offerRejecteds ;
    private String title, idDevice, idRecipient, publicIP ;
    private double amount ;
    private boolean enabled ;

    public HotPanelHome(){}

    public HotPanelHome(int typeView) {
        this.typeView = typeView;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public int getOfferAccepteds() {
        return offerAccepteds;
    }

    public void setOfferAccepteds(int offerAccepteds) {
        this.offerAccepteds = offerAccepteds;
    }

    public int getOfferRejecteds() {
        return offerRejecteds;
    }

    public void setOfferRejecteds(int offerRejecteds) {
        this.offerRejecteds = offerRejecteds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }

    public String getPublicIP() {
        return publicIP;
    }

    public void setPublicIP(String publicIP) {
        this.publicIP = publicIP;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public class Type{
        public static final int PERFORMANCE_DAYLY = 0 ;
        public static final int PERFORMANCE_WEEKLY = 1 ;
        public static final int PERFORMANCE_MONTHLY = 2 ;
        public static final int PERFORMANCE_YEARLY = 3;
    }
}
