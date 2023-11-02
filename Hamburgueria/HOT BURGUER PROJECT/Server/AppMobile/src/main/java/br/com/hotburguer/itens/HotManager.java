package br.com.hotburguer.itens;

import java.io.Serializable;

public class HotManager implements Serializable {

    private int id, countDelivery ;
    private double totalBilling, thisYearBilled, thisMonthBilled, thisWeekBilled, thisDayBilled ;
    private String name, dateCreated ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountDelivery() {
        return countDelivery;
    }

    public void setCountDelivery(int countDelivery) {
        this.countDelivery = countDelivery;
    }

    public double getTotalBilling() {
        return totalBilling;
    }

    public void setTotalBilling(double totalBilling) {
        this.totalBilling = totalBilling;
    }

    public double getThisYearBilled() {
        return thisYearBilled;
    }

    public void setThisYearBilled(double thisYearBilled) {
        this.thisYearBilled = thisYearBilled;
    }

    public double getThisMonthBilled() {
        return thisMonthBilled;
    }

    public void setThisMonthBilled(double thisMonthBilled) {
        this.thisMonthBilled = thisMonthBilled;
    }

    public double getThisWeekBilled() {
        return thisWeekBilled;
    }

    public void setThisWeekBilled(double thisWeekBilled) {
        this.thisWeekBilled = thisWeekBilled;
    }

    public double getThisDayBilled() {
        return thisDayBilled;
    }

    public void setThisDayBilled(double thisDayBilled) {
        this.thisDayBilled = thisDayBilled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
