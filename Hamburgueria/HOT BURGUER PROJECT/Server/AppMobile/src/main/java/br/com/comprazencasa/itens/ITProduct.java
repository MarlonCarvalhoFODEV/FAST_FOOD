/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.itens;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marlon
 */

public class ITProduct  implements Serializable, Comparable<String>{

    public static final String MORNING = "Morning"; //Matinais
    public static final String COOKIES_SUGARPLUM = "Cookies & Sugarplum"; // Biscoitos
    public static final String CLEANING_PERFUMERY = "Cleaning Perfumery"; // Biscoitos

    public static final String BUTCHERY = "Butchery"; //Açougue
    public static final String DRINKS = "Drinks"; //Bebida
    public static final String BABY = "Baby"; //Bebida
    public static final String COLD_DAIRY = "Cold & Dairy";
    public static final String BASIC_FOOD = "Basic Food" ;
    public static final String SALTY = "Salty" ;
    public static final String HORTIFRUTI = "Hortifruti" ;

    private int id, idProduct, idStore, count=1, percentageDiscount, percentageInterest, instalments ;
    private String orderID, pathImg, namePromo, title, categorie, subCategorie, url, status;
    private double inPrice, perPrice,perPriceMultiply ;
    private boolean selected, haveMore, isFlavors, tangible, weight,  withInterest;
    private ITFlavor flavor ;
    private Map<Integer,ITFlavor> flavors = new HashMap<>();

    public ITProduct (){}

    public ITProduct (String title, double price, int count){
        this.title = title ;
        this.perPrice = price ;
        this.count = count ;
        this.flavors = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public String getNamePromo() {
        return namePromo;
    }

    public void setNamePromo(String namePromo) {
        this.namePromo = namePromo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ITFlavor getFlavor() {
        return flavor;
    }

    public void setFlavor(ITFlavor flavor) {
        this.flavor = flavor;
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

    public String getSubCategorie() {
        return subCategorie;
    }

    public void setSubCategorie(String subCategorie) {
        this.subCategorie = subCategorie;
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

    public boolean isFlavors() {
        return isFlavors;
    }

    public boolean isTangible() {
        return tangible;
    }

    public void setTangible(boolean tangible) {
        this.tangible = tangible;
    }

    public void setFlavors(boolean flavors) {
        isFlavors = flavors;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Map<Integer,ITFlavor> getFlavors(){
        return flavors ;
    }

    public boolean containFlavor(ITFlavor it){
        for (ITFlavor f : flavors.values()){
            if(f.getFlavor().equals(it.getFlavor())) return true ;
        }

        return false ;
    }

    public ITFlavor getFlavor(ITFlavor item){

        for (ITFlavor i : flavors.values()) if(i.getFlavor().equals(item.getFlavor())) return i ;

        return null ;
    }

    public void removeFlavor(ITFlavor item) {
        if(item.getCount() != 1){
            item.setCount(item.getCount()-1);
            flavors.put(item.getId(), item);
        } else {
            item.setCount(item.getCount()-1);
            flavors.put(item.getId(), item);
            flavors.remove(item.getId());
        }
    }

    public void clearFlavors(){
        this.flavors.clear();
    }

    @Override
    public int compareTo(String o) {
    
        if (getTitle().equalsIgnoreCase(o)) {
            return 0;
        }
       return -1;
    }
}
