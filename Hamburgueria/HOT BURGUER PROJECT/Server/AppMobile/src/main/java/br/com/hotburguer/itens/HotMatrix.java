package br.com.hotburguer.itens;

import br.com.comprazencasa.geo.LatLng;

import java.io.Serializable;

public class HotMatrix implements Serializable {

    private LAddress address1, address2 ;
    private LatLng location ;
    private double distance ;

    public LAddress getAddress1() {
        return address1;
    }

    public void setAddress1(LAddress address1) {
        this.address1 = address1;
    }

    public LAddress getAddress2() {
        return address2;
    }

    public void setAddress2(LAddress address2) {
        this.address2 = address2;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
