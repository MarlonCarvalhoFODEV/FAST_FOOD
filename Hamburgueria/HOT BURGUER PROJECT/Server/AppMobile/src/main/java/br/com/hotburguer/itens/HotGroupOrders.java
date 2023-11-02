package br.com.hotburguer.itens;

import apis.APIFinance;

import java.io.Serializable;
import java.util.ArrayList;

public class HotGroupOrders extends ArrayList<HotOrderManager> implements Serializable {

    private int typeGroup;

    public HotGroupOrders(HotOrderManager orderR) {
        add(orderR);
        this.typeGroup = size();
    }

    public HotGroupOrders() {
    }

    public HotOrderManager getNearestOrder() {
        return get(0);
    }

    public int getTypeGroup() {
        return typeGroup;
    }

    public int getIdDeliveryman(){
        if(size() > 0)
            return get(0).getIdDeliveryman();
        return 0 ;
    }

    public String getNameDeliveryman() {
        if (size() > 0)
            return get(0).getDeliverymanName();
        return "(Sem nome)";
    }

    public synchronized double getTotalDistance(){
        double distance = 0 ;

        for(HotOrderManager g : this) {
            distance += g.getDistance();
        }

        return APIFinance.format(distance, 2);
    }

    @Override
    public boolean add(HotOrderManager e) {
        boolean result = super.add(e);
        this.typeGroup = size();
        return result;//To change body of generated methods, choose Tools | Templates.
    }
}