package apis.pagarme.recipient;

import java.io.Serializable;

public class RRecipient implements Serializable {

    /*
    *  idRecipient -> ID do recebedor
    *  idBank -> ID do banco do recebedor
    *  dayTransfer - > Dia no qual o recebedor vai ser pago weekly: 1 a 5, onde 1 é segunda-feira e 5 é sexta-feiramonthly: 1 a 31 daily: 1
    *  transferIntervalType -> Frequência na qual o recebedor irá ser pago (daily, weekly, monthly)
    *  automaticTransfer -> Indica se o recebedor pode receber os pagamentos automaticamente
    *  anticipationVolPercentage - > Porcentagem do valor passível de antecipação para este recebedor.
    *  automaticAnticipation -> Se o recebedor está habilitado para receber automaticamente ou não o valor disponível para antecipação
    *  automaticAnticipationDays -> dias em que devemos criar as antecipações automáticas.
    * transfer -> se o recebedor está habilitado para receber ou não*/

    private String idRecipient, automaticAnticipationType, automaticAnticipationDays, transferIntervalType;
    private int idBankAccount, dayTransfer, anticipateVolPercentage, countPage, page;
    private boolean transfer, automaticTransfer, automaticAnticipation;

    public int getIdBankAccount() {
        return idBankAccount;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }

    public void setIdBankAccount(int idBankAccount) {
        this.idBankAccount = idBankAccount;
    }

    public int getDayTransfer() {
        return dayTransfer;
    }

    public void setDayTransfer(int dayTransfer) {
        this.dayTransfer = dayTransfer;
    }

    public int getAnticipateVolPercentage() {
        return anticipateVolPercentage;
    }

    public void setAnticipateVolPercentage(int anticipateVolPercentage) {
        this.anticipateVolPercentage = anticipateVolPercentage;
    }

    public String getTransferIntervalType() {
        return transferIntervalType;
    }

    public void setTransferIntervalType(String transferIntervalType) {
        this.transferIntervalType = transferIntervalType;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    public boolean isAutomaticTransfer() {
        return automaticTransfer;
    }

    public void setAutomaticTransfer(boolean automaticTransfer) {
        this.automaticTransfer = automaticTransfer;
    }

    public boolean isAutomaticAnticipation() {
        return automaticAnticipation;
    }

    public void setAutomaticAnticipation(boolean automaticAnticipation) {
        this.automaticAnticipation = automaticAnticipation;
    }

    public String isAutomaticAnticipationType() {
        return automaticAnticipationType;
    }

    public void setAutomaticAnticipationType(String automaticAnticipationType) {
        this.automaticAnticipationType = automaticAnticipationType;
    }

    public String getAutomaticAnticipationDays() {
        return automaticAnticipationDays;
    }

    public void setAutomaticAnticipationDays(String automaticAnticipationDays) {
        this.automaticAnticipationDays = automaticAnticipationDays;
    }

    public static class Type{
        public static final int DAiLY = 0 ;
        public static final int WEEKLY = 1 ;
        public static final int MONTHLY = 2 ;
    }

    public static class TypeS{
        public static final String DAILY = "daily" ;
        public static final String WEEKLY = "weekly" ;
        public static final String MONTHLY = "monthly" ;
    }
}
