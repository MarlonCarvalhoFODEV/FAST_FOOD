package br.com.comprazencasa.itens.trasaction;

import apis.pagarme.userbuyer.BCard;
import apis.pagarme.userbuyer.BProduct;

import java.io.Serializable;
import java.util.ArrayList;

public class ITTransaction implements Serializable {

    private int idBuyer ;
    private BCard card ;
    private ArrayList<BProduct> products ;

    public int getIdBuyer() {
        return idBuyer;
    }

    public void setIdBuyer(int idBuyer) {
        this.idBuyer = idBuyer;
    }

    public BCard getCard() {
        return card;
    }

    public void setCard(BCard card) {
        this.card = card;
    }

    public ArrayList<BProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<BProduct> products) {
        this.products = products;
    }
}
