package br.com.hotburguer.itens;

import java.io.Serializable;

public class HotDayOperation implements Serializable {
    private int idStore, idDay, inetervalScheduler ;
    private String dayName,openingHour, closingHour ;
    private boolean operating, operatingHour ;

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getIdDay() {
        return idDay;
    }

    public void setIdDay(int idDay) {
        this.idDay = idDay;
    }

    public int getInetervalScheduler() {
        return inetervalScheduler;
    }

    public void setInetervalScheduler(int inetervalScheduler) {
        this.inetervalScheduler = inetervalScheduler;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public boolean isOperating() {
        return operating;
    }

    public void setOperating(boolean operating) {
        this.operating = operating;
    }

    public boolean isOperatingHour() {
        return operatingHour;
    }

    public void setOperatingHour(boolean operatingHour) {
        this.operatingHour = operatingHour;
    }
}
