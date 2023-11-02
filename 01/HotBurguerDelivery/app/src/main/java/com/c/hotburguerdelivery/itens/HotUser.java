package com.c.hotburguerdelivery.itens;

import com.c.hotburguerdelivery.apis.location.LAddress;
import com.c.hotburguerdelivery.apis.userbuyer.BCard;
import com.c.hotburguerdelivery.apis.userbuyer.BContact;
import com.c.hotburguerdelivery.apis.userbuyer.BDocument;

import java.io.Serializable;

public class HotUser implements Serializable {

    private int id,idCustomer;
    private String name, email, birthOfDate, socialLogin, cpf, deviceID;
    private BCard card ;
    private BDocument document ;
    private BContact contact ;
    private LAddress address ;
    private boolean status ;

    public HotUser(){
        card = new BCard();
        document = new BDocument();
        contact = new BContact();
        address = new LAddress();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(String birthOfDate) {
        this.birthOfDate = birthOfDate;
    }

    public String getSocialLogin() {
        return socialLogin;
    }

    public void setSocialLogin(String socialLogin) {
        this.socialLogin = socialLogin;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public BCard getCard() {
        return card;
    }

    public void setCard(BCard card) {
        this.card = card;
    }

    public BDocument getDocument() {
        return document;
    }

    public BContact getContact() {
        return contact ;
    }

    public LAddress getAddress() {
        return address;
    }

    public void setAddress(LAddress address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
