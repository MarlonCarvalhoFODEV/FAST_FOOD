package apis.pagarme.refunds;

import apis.pagarme.transaction.MetaData;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Refund implements Serializable {

    private String id ;
    private String object ;
    private int amount ;
    private String type ;
    private String status ;
    private String chargeFeeRecipientID ;
    private String bankAccountID ;
    private String transactionID ;
    private String dateCreated ;
    private MetaData metaData ;

    public Refund(JSONObject jo) throws JSONException {
        id =jo.getString("id");
        object =jo.getString("object");
        amount =jo.getInt("amount");
        type =jo.getString("type");
        status =jo.getString("status");
        chargeFeeRecipientID =jo.getString("charge_fee_recipient_id");
        bankAccountID =jo.getString("bank_account_id");
        transactionID =jo.getString("transaction_id");
        dateCreated =jo.getString("date_created");

        JSONObject jodata = jo.getJSONObject("metadata");

        metaData = new MetaData();
        metaData.setIdProduct(jodata.getString("idProduto"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChargeFeeRecipientID() {
        return chargeFeeRecipientID;
    }

    public void setChargeFeeRecipientID(String chargeFeeRecipientID) {
        this.chargeFeeRecipientID = chargeFeeRecipientID;
    }

    public String getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(String bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
