package br.com.hotbdelivery.itens;

import android.util.Log;

import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apis.internet.InternetRequisition;
import apis.location.LAddress;
import br.com.hotbdelivery.aplication.CustomApplication;

public class HotRunning implements Serializable {

    private int countDelivery ;
    private double totalValue, originLat, originLng, finalLat, finalLng;
    private Map<Integer,HotDelivery> deliverys ;
    private ArrayList<LAddress> addresses ;

    public HotRunning(){
        this.deliverys = new HashMap<>();
        this.addresses = new ArrayList<>();
    }

    public int getCountDelivery() {
        return countDelivery;
    }

    public void setCountDelivery(int countDelivery) {
        this.countDelivery = countDelivery;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }

    public double getFinalLat() {
        return finalLat;
    }

    public void setFinalLat(double finalLat) {
        this.finalLat = finalLat;
    }

    public double getFinalLng() {
        return finalLng;
    }

    public void setFinalLng(double finalLng) {
        this.finalLng = finalLng;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public Map<Integer,HotDelivery> getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(ArrayList<HotDelivery> deliverys) {
        for(HotDelivery del : deliverys) {
            this.deliverys.put(del.getIdOrder(), del);
            this.addresses.add(del.getAddressDelivery());
        }
    }

    public List<Integer> getIdsOrdes(){
        List<Integer> ids = new ArrayList<>();

        for(HotDelivery del : deliverys.values())
            ids.add(del.getId());

        return ids;
    }

    public int[] getIdOrders() {

        int[] ids = new int[deliverys.size()-1];

        int pos = 0;
        for(HotDelivery del : deliverys.values()) {
            if (del.getIdOrder() > 1) {
                ids[pos] = del.getIdOrder();
                pos++;
            }
        }
        return ids;
    }

    public ArrayList<LAddress> getAddresses(){
       return addresses ;
    }

    public HotDelivery getAddressFromOrderID(int idOrder) {
        for(HotDelivery del : deliverys.values())
                if(del.getIdOrder() == idOrder)
                    return del;
        return null;
    }
}
