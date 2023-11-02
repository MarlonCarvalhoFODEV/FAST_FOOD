package apis.pagarme.transaction;

import java.io.Serializable;

public class Payment implements Serializable {

    public static final int PAID = 0;
    public static final int PROCESSING = 1;
    public static final int CHARGEBACK = 2;
    public static final int REFUSED = 3;
    public static final int PENDING_REFUND = 4;
    public static final int WAITING_PAYMENT = 5;
    public static final int REFUNDED = 6;
    public static final int AUTHORIZED = 7;
    public static final int POSTBACK_URL = 8;

    public static final int REQUEST_CODE = 413;
    public static final int RESULT_CODE = 444;

    private int id, idIcon, status;
    private String msgAlert, msgComplement, backgroundColor;
    private boolean blocked ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsgAlert() {
        return msgAlert;
    }

    public void setMsgAlert(String msgAlert) {
        this.msgAlert = msgAlert;
    }

    public String getMsgComplement() {
        return msgComplement;
    }

    public void setMsgComplement(String msgComplement) {
        this.msgComplement = msgComplement;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
