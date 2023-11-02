package br.com.hotburguer.itens;

import java.io.Serializable;

public class HotReuseAddress implements Serializable {

    private int idUser, idOrder ;
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

    public LAddress getAddress() {
        return address;
    }

    public void setAddress(LAddress address) {
        this.address = address;
    }
}
