package br.com.comprazencasa.itens;

import java.io.Serializable;

public class ITCoupon implements Serializable {

    private int id, idProduct, countDayForExpiration, take, pay, idUser ;
    private String title, rule, description, pathImgProduct, pathImgPromo, categorie, expirationDate;
    private boolean haveMore, selected, percentual ;
    private double discount, discountLimit, minimumPurchase ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getCountDayForExpiration() {
        return countDayForExpiration;
    }

    public void setCountDayForExpiration(int countDayForExpiration) {
        this.countDayForExpiration = countDayForExpiration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title ;
    }

    public String getPathImgPromo() {
        return pathImgPromo;
    }

    public void setPathImgPromo(String pathImgPromo) {
        this.pathImgPromo = pathImgPromo;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String productName) {
        this.description = productName;
    }

    public String getPathImgProduct() {
        return pathImgProduct;
    }

    public void setPathImgProduct(String pathImgProduct) {
        this.pathImgProduct = pathImgProduct;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPercentual() {
        return percentual;
    }

    public void setPercentual(boolean percentual) {
        this.percentual = percentual;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscountLimit() {
        return discountLimit;
    }

    public void setDiscountLimit(double discountLimit) {
        this.discountLimit = discountLimit;
    }

    public double getMinimumPurchase() {
        return minimumPurchase;
    }

    public void setMinimumPurchase(double minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }
}
