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
public class City {
    private int id,idState, ddd, IBGE ;
    private String uf,name, neighborhood, cep, initials, areaMinCEP, areaMaxCEP;
    private double latitude, longitude, altitude ;
    
    private final List<Street> streets ;
    
    public City(){
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

    public int getIdState() {
        return idState;
    }

    public void setIdState(int idState) {
        this.idState = idState;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public int getIBGE() {
        return IBGE;
    }

    public void setIBGE(int IBGE) {
        this.IBGE = IBGE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getAreaMinCEP() {
        return areaMinCEP;
    }

    public void setAreaMinCEP(String areaMinCEP) {
        this.areaMinCEP = areaMinCEP;
    }

    public String getAreaMaxCEP() {
        return areaMaxCEP;
    }

    public void setAreaMaxCEP(String areaMaxCEP) {
        this.areaMaxCEP = areaMaxCEP;
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
    
    public String getUf() {
        return uf ;
    }
}
