package com.c.hotburguerdelivery.Collaborator;

import java.io.Serializable;

public class CollaboratorAddress implements Serializable {
    private int id, residenceNumber;
    private String state, city, neighborhood, street, complement;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResidenceNumber() {
        return residenceNumber;
    }

    public void setResidenceNumber(int residenceNumber) {
        this.residenceNumber = residenceNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }
}
