package br.com.hotburguer.itens;

import java.io.Serializable;

public class HotDelivery implements Serializable {

    private int id, idOrder ;
    private String clientName ;
    private LAddress addressDelivery ;

    public HotDelivery() { }

    public HotDelivery(String clientName, LAddress addressDelivery) {
        this.idOrder = addressDelivery.getId() ;
        this.clientName = clientName ;
        this.idOrder = addressDelivery.getId();
        this.addressDelivery = addressDelivery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LAddress getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(LAddress addressDelivery) {
        this.addressDelivery = addressDelivery;
    }
}
