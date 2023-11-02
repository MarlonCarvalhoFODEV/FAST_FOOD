/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class RateServices implements Serializable{
    
    private int id ;
    private double rateComprazemCasa,ratePercentualCredit ;

    public RateServices(){}
    
    public RateServices(double czc, double mp){
        this.rateComprazemCasa = czc ;
        this.ratePercentualCredit = mp ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
    public double getRateComprazemCasa() {
        return rateComprazemCasa;
    }

    public void setRateComprazemCasa(double rateComprazemCasa) {
        this.rateComprazemCasa = rateComprazemCasa;
    }

    public double getRatePercentualCredit() {
        return ratePercentualCredit;
    }

    public void setRatePercentualCredit(double ratePercentualCredit) {
        this.ratePercentualCredit = ratePercentualCredit;
    }
}
