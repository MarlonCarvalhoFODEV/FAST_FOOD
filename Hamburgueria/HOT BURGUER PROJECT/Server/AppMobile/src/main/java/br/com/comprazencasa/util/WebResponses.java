/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class WebResponses implements Serializable {
    private int idUser, idBuyer, idDeliveryMan, idCoupon, comunication ;
    private String idOrder, idUserS, jsonObject, message;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdUserS() {
        return idUserS;
    }

    public void setIdUserS(String idUserS) {
        this.idUserS = idUserS;
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

    public int getComunication() {
        return comunication;
    }

    public void setComunication(int comunication) {
        this.comunication = comunication;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdCoupon() {
        return idCoupon;
    }

    public void setIdCoupon(int idCoupon) {
        this.idCoupon = idCoupon ;
    }
}