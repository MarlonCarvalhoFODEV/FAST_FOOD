package br.com.hotburguer.itens;

import java.io.Serializable;
import java.util.ArrayList;

public class HotOrderManager implements Serializable {

    private int idOrder, idDeliveryman ;
    private String clientName, deliverymanName ;
    private double latitude, longitude, distance;
    private ArrayList<HotProduct> products ;

    public HotOrderManager() {
        this.products = new ArrayList<>();
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdDeliveryman() {
        return idDeliveryman;
    }

    public void setIdDeliveryman(int idDeliveryman) {
        this.idDeliveryman = idDeliveryman;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDeliverymanName() {
        return deliverymanName;
    }

    public void setDeliverymanName(String deliverymanName) {
        this.deliverymanName = deliverymanName;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<HotProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<HotProduct> products) {
        this.products = products;
    }
}
