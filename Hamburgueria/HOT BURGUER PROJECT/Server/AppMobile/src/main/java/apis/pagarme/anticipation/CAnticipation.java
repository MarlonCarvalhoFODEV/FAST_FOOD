package apis.pagarme.anticipation;

import apis.pagarme.userbuyer.BAccountBank;
import me.pagar.model.BankAccount;

import java.io.Serializable;

public class CAnticipation implements Serializable {

    private String idRecipient ;
    private int requestAmount, plusDays, page, countPage;
    private boolean build ;
    private int timeFrame ;

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public int getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(int requestAmount) {
        this.requestAmount = requestAmount;
    }

    public int getPlusDays() {
        return plusDays;
    }

    public void setPlusDays(int plusDays) {
        this.plusDays = plusDays;
    }

    public boolean isBuild() {
        return build;
    }

    public void setBuild(boolean build) {
        this.build = build;
    }

    public int getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(int timeFrame) {
        this.timeFrame = timeFrame;
    }

    public static class TimeFrameType{
        public static final int START = 0 ;
        public static final int END = 1 ;
    }
}
