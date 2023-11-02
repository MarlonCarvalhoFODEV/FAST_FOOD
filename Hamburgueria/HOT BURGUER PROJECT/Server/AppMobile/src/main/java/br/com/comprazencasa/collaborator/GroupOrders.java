/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.collaborator;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Marlon
 */
public class GroupOrders extends ArrayList<ITOrderBuyer> implements Serializable{

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
    
    public GroupOrders(ITOrderBuyer orderR) {
        add(orderR);
        this.typeGroup = size();
    }
    
    public GroupOrders() {}
    
    public ITOrderBuyer getNearestOrder(){
        return get(0);
    }

    public int getTypeGroup() {
        return typeGroup;
    }
    
    @Override
    public boolean add(ITOrderBuyer e) {
        boolean result = super.add(e);
        this.typeGroup = size();
        return  result ;//To change body of generated methods, choose Tools | Templates.
    }
}
