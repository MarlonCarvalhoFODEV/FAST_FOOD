package com.hotburguer.itens;

import java.io.Serializable;
import java.text.DecimalFormat;

public class HotProduct implements Serializable{

    private int id, idUser, idProduct, idStore, count, percentageDiscount, percentageInterest, instalments, countFeedbacks, countLikes, position ;
    private String title, description, categorie, status, recipe;
    private double inPrice, perPrice, perPriceMultiply, costPrice, iAdd, feeDeliveryman ;
    private boolean selected, haveMore, tangible, weight,  withInterest, disabled, liked, combo;

    public HotProduct(){}

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

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(int percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getInPrice() {
        return inPrice;
    }

    public void setInPrice(double inPrice) {
        this.inPrice = inPrice;
    }

    public double getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(double perPrice) {
        this.perPrice = perPrice;
    }

    public double getPerPriceMultiply() {
        double pCount = getPerPrice() * count;
        double pweiht = (getPerPrice()/1000)*count;
        return perPriceMultiply = formaterValue(isWeight()?pweiht:pCount) ;
    }

    private double formaterValue(double value){
        DecimalFormat df = new DecimalFormat("0.##");
        return Double.valueOf(df.format(value).replace(",","."));
    }

    public int getInstalments() {
        return instalments;
    }

    public void setInstalments(int instalments) {
        this.instalments = instalments;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        //if(categorie.equals(BUTCHERY) || categorie.equals(SALTY)) isWeight = true ;
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        perPriceMultiply = formaterValue(getPerPrice() * count);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    public boolean setWeight(boolean isWeight) {
        return this.weight = isWeight ;
    }

    public boolean isWeight() {
        return weight ;
    }

    public boolean isTangible() {
        return tangible;
    }

    public void setTangible(boolean tangible) {
        this.tangible = tangible;
    }

    public int getPercentageInterest() {
        return percentageInterest;
    }

    public void setPercentageInterest(int percentageInterest) {
        this.percentageInterest = percentageInterest;
    }

    public boolean isWithInterest() {
        return withInterest;
    }

    public void setWithInterest(boolean withInterest) {
        this.withInterest = withInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPerPriceMultiply(double perPriceMultiply) {
        this.perPriceMultiply = perPriceMultiply;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getiAdd() {
        return iAdd;
    }

    public void setiAdd(double iAdd) {
        this.iAdd = iAdd;
    }

    public double getFeeDeliveryman() {
        return feeDeliveryman;
    }

    public void setFeeDeliveryman(double feeDeliveryman) {
        this.feeDeliveryman = feeDeliveryman;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isCombo() {
        return combo;
    }

    public void setCombo(boolean combo) {
        combo = combo;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getCountFeedbacks() {
        return countFeedbacks;
    }

    public void setCountFeedbacks(int countFeedbacks) {
        this.countFeedbacks = countFeedbacks;
    }

    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public class Categories{

        public static final String SNACKS = "snacks";
        public static final String DRINKS = "drinks";

        public class SubCategorie{
            public static final String BURGUERS = "burguers";
            public static final String PIZZA = "pizza";
            public static final String FRIES = "fries";
        }
    }

}
