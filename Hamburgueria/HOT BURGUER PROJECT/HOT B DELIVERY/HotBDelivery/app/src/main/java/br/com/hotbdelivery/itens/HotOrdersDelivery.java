package br.com.hotbdelivery.itens;

import android.util.Log;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import apis.location.LAddress;

public class HotOrdersDelivery implements Serializable {

    private int idDeliveryman, countDelivery;
    private double amount;

    private DirectionsRoute route;
    private Point pointOrigin, pointFinal;

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

    public DirectionsRoute getRoute() {
        return route;
    }

    public void setRoute(DirectionsRoute route) {
        this.route = route;
    }

    public Point getPointOrigin() {
        return pointOrigin;
    }

    public void setPointOrigin(Point pointOrigin) {
        this.pointOrigin = pointOrigin;
    }

    public Point getPointFinal() {
        return pointFinal;
    }

    public void setPointFinal(Point pointFinal) {
        this.pointFinal = pointFinal;
    }
}
