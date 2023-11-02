/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.geo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marlon
 */
public class Neighborhood {
    private int id, idCity ;
    private String name ;
    private double latitude, longitude ;
    
    private final List<Street> streets ;

    public Neighborhood(){
        this.streets = new ArrayList<>();
    }

    public Neighborhood(int id,int idCity,String name,double latitude,double longitude){
        this.id = id ;
        this.idCity = idCity ;
        this.name = name ;
        this.latitude = latitude ;
        this.longitude = longitude ;
        this.streets = new ArrayList<>();
    }
    
    public List<Street> getStreets() {
        return streets;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    
}
