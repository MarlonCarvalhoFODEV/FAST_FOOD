package br.com.hotburguer.itens;

import java.util.ArrayList;

public class HotOrdersDelivery {

    private int idDeliveryman, countDelivery;
    private double amount;


    private int[] idOrders;
    private ArrayList<LAddress> address;

    public int getCountDelivery() {
        return countDelivery;
    }

    public void setCountDelivery(int countDelivery) {
        this.countDelivery = countDelivery;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIdDeliveryman() {
        return idDeliveryman;
    }

    public void setIdDeliveryman(int idDeliveryman) {
        this.idDeliveryman = idDeliveryman;
    }

    public int[] getIdOrders() {

        int[] ids = new int[address.size()];

        for (int i = 0; i < address.size(); i++) {
            if (i > 0)
                ids[i] = address.get(i).getId();
        }

        idOrders = ids;
        return idOrders;
    }

    public ArrayList<LAddress> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<LAddress> address) {
        this.address = address;
        int[] ids = new int[address.size()];

        for (int i = 0; i < address.size(); i++)
            ids[i] = address.get(i).getId();

    }

}
