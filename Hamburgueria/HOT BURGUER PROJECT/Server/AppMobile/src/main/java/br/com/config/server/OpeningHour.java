package br.com.config.server;

import java.io.Serializable;

import static br.com.comprazencasa.util.DataPadraoUtil.formataData;

public class OpeningHour implements Serializable {
    private int dayOfWeek ;
    private int startHour, startMinute, finalHour, finalMinute;

    private String initialHour, endHour;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = Integer.valueOf(formataData(startHour,"HH:mm:ss","HH"));
        this.startMinute = Integer.valueOf(formataData(startHour,"HH:mm:ss","mm"));
        this.initialHour = formataData(startHour,"HH:mm:ss","HH:mm") ;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getFinalHour() {
        return finalHour;
    }

    public void setFinalHour(String finalHour) {
        this.finalHour = Integer.valueOf(formataData(finalHour,"HH:mm:ss","HH"));
        this.finalMinute = Integer.valueOf(formataData(finalHour,"HH:mm:ss","mm"));
        this.endHour = formataData(finalHour,"HH:mm:ss","HH:mm") ; ;
    }

    public int getFinalMinute() {
        return finalMinute;
    }

    public String getInitialHour() {
        return initialHour;
    }

    public String getEndHour() {
        return endHour;
    }
}