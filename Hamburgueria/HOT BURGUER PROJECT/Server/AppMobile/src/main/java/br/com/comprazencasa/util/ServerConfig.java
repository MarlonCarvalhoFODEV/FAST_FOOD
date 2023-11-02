package br.com.comprazencasa.util;

import br.com.config.server.BaseLocation;
import br.com.config.server.OpeningDate;
import br.com.config.server.OpeningHour;

import java.io.Serializable;

public class ServerConfig implements Serializable {
    private int limitOrders ;
    private boolean releaseMakeWish ;
    private BaseLocation location ;
    private OpeningDate openingDate ;
    private OpeningHour openingHour ;

    public ServerConfig(){
        this.location = new BaseLocation();
        this.openingDate = new OpeningDate();
        this.openingHour = new OpeningHour();
    }

    public BaseLocation getLocation() {
        return location;
    }

    public void setLocation(BaseLocation location) {
        this.location = location;
    }

    public OpeningDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(OpeningDate openingDate) {
        this.openingDate = openingDate;
    }

    public OpeningHour getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(OpeningHour openingHour) {
        this.openingHour = openingHour;
    }

    public boolean isReleaseMakeWish() {
        return releaseMakeWish;
    }

    public void setReleaseMakeWish(boolean releaseMakeWish) {
        this.releaseMakeWish = releaseMakeWish;
    }

    public int getLimitOrders() {
        return limitOrders;
    }

    public void setLimitOrders(int limitOrders) {
        this.limitOrders = limitOrders;
    }


}
