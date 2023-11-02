package br.com.hotbmanager.itens;

import android.util.Log;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

import apis.finance.FinanceAPI;

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

    public synchronized double getTotalDistance() {
        double distance = 0;

        for (HotOrderManager g : this) {
            distance += g.getDistance();
        }

        return FinanceAPI.format(distance, 2);
    }

    public String getCodeOrders() {
        StringBuilder sb = new StringBuilder();

        int counte = 0;
        for (HotOrderManager m : this) {

            if (counte == 0) {
                counte++;
                sb.append(m.getIdOrder());
            } else {
                sb.append(" • ");
                sb.append(m.getIdOrder());
            }
        }

        return sb.toString();
    }

    public int[] getIdOrders() {
        int[] ids = new int[size()];
        for (int i = 0; i < size(); i++)
            ids[i] = get(i).getIdOrder();

        return ids;
    }

    @Override
    public boolean add(HotOrderManager e) {
        boolean result = super.add(e);
        this.typeGroup = size();
        return result;//To change body of generated methods, choose Tools | Templates.
    }
}