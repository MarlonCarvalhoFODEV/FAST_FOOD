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
public class SearchProduct implements Serializable{
    private int idLastProduct ;
    private double perPriceLastProduct ;
    private String title, categorie, subCategorie;

    public int getIdLastProduct() {
        return idLastProduct;
    }

    public void setIdLastProduct(int idLastProduct) {
        this.idLastProduct = idLastProduct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getSubCategorie() {
        return subCategorie;
    }

    public void setSubCategorie(String subCategorie) {
        this.subCategorie = subCategorie;
    }

    public void setPerPriceLastProduct(double perPriceLastProduct) {
        this.perPriceLastProduct = perPriceLastProduct;
    }

    public double getPerPriceLastProduct() {
        return perPriceLastProduct;
    }
}
