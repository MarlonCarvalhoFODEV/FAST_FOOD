package com.c.hotburguerdelivery.itens;

import java.io.Serializable;

public class SearchProduct implements Serializable {
    private int idLastProduct;
    private String title, categorie;

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
}