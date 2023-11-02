package com.c.hotburguerdelivery.itens;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GroupOrders extends ArrayList<HotOrder> implements Serializable {

    public static final int SINGLE_ORDER = 1 ;
    public static final int DOUBLE_ORDER = 2 ;
    public static final int TRIPLE_ORDER = 3 ;
    public static final int QUADRUPLE_ORDER = 4 ;
    public static final int QUINTUPLE_ORDER = 5 ;
    public static final int SEXTUPLE_ORDER = 6 ;
    public static final int SEPTUPLE_ORDER = 7 ;
    public static final int OCTUPLE_ORDER = 8 ;
    public static final int NONUPLE_ORDER = 9 ;
    public static final int DECUPLE_ORDER = 10 ;

    private int typeGroup ;

    public GroupOrders(HotOrder orderR) {
        add(orderR);
        this.typeGroup = size();
    }

    public GroupOrders() {}

    public HotOrder getNearestOrder(){
        return get(0);
    }

    public ArrayList<Integer> getIdUsers(){
        ArrayList array = new ArrayList();
        for (HotOrder o : this){
            array.add(o.getIdUser());
        }
        return array;
    }

    public String[] getIdOrders(){
        StringBuilder ids = new StringBuilder();
        for (HotOrder o : this){
            ids.append(o.getId());
            ids.append(",");
        }
        return ids.toString().split(",");
    }

    public int getTypeGroup() {
        return typeGroup;
    }

    @Override
    public boolean add(HotOrder e) {
        boolean result = super.add(e);
        this.typeGroup = size();
        return  result ;//To change body of generated methods, choose Tools | Templates.
    }

    public double getPurchaseAll(){
        double purchaseAll = 0.0;
        for (HotOrder o : this){
            purchaseAll += Double.valueOf(o.getOrderPrice());
        }

        DecimalFormat dec = new DecimalFormat("0.##");
        return Double.valueOf(dec.format(purchaseAll).replace(",","."));
    }
}