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
public class State {
    private int id ;
    private String uf, name ;
    
    private final List<City> citys ;
    
    public State(){
        this.citys = new ArrayList<>();
    }

    public State(int id, String uf, String name){
        this.id = id ;
        this.uf = uf ;
        this.name = name ;
        this.citys = new ArrayList<>();
    }
    
    public List<City> getCitys() {
        return citys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
