package br.com.hotbdelivery.itens;

import java.io.Serializable;

public class HotHistoricRegister implements Serializable {

    private int id, countDelivery ;
    private double amount ;
    private String dayName, date, month ;
    private boolean haveMore ;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public boolean isHaveMore() {
        return haveMore;
    }

    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }
}
