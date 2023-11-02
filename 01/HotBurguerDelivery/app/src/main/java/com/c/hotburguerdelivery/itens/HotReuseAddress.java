package com.c.hotburguerdelivery.itens;

import com.c.hotburguerdelivery.apis.location.LAddress;

import java.io.Serializable;

public class HotReuseAddress implements Serializable {

    private int idUser, idOrder, position ;
    private LAddress address ;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LAddress getAddress() {
        return address;
    }

    public void setAddress(LAddress address) {
        this.address = address;
    }
}
