package apis.pagarme.transaction;

import me.pagar.model.Customer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Billet implements Serializable {

    private String object;
    private String status;
    private String refuseReason;
    private String statusReason;
    private String acquirerResponseCode;
    private String acquireName;
    private String acquireID;
    private String authorizationCode;
    private String softDescription;
    private int tid;
    private int nsu;
    private String dateCreated;
    private String dateUpdated;
    private int amount;
    private int authorizedAmount;
    private int paidAmount;
    private int refundedAmount;
    private int installments;
    private int id;
    private int cost;
    private String cardOlderName;
    private String cardLastDigits;
    private String cardFirstdigits;
    private String cardBrand;
    private String cardPinMode;
    private String postbackUrl;
    private String paymentMethod;
    private String captureMethod;
    private String antifraudScore;
    private String billetUrl;
    private String billetBarcode;
    private String billetExpirationDate;
    private String referer;
    private String ip;
    private String subscriptionID;
    private String phone;
    private String address;
    private Customer customer;

    public Billet(JSONObject json) throws JSONException {
        object = json.getString("object");
        status = json.getString("status");
        statusReason = json.getString("status_reason");
        acquirerResponseCode = json.getString("acquirer_response_code");
        acquireName = json.getString("acquire_name");
        acquireID = json.getString("acquire_id");
        authorizationCode = json.getString("authorization_code");
        softDescription = json.getString("soft_description");
        tid = json.getInt("tid");
        nsu = json.getInt("nsu");
        dateCreated = json.getString("date_created");
        dateUpdated = json.getString("date_updated");
        amount = json.getInt("amount");
        authorizedAmount = json.getInt("authorized_amount");
        paidAmount = json.getInt("paid_amount");
        refundedAmount = json.getInt("refunded_amount");
        installments = json.getInt("installments");
        id = json.getInt("id");
        cost = json.getInt("cost");
        cardOlderName = json.getString("card_holder_name");
        cardLastDigits = json.getString("card_last_digits");
        cardFirstdigits = json.getString("card_first_digits");
        cardBrand = json.getString("card_brand");
        cardPinMode = json.getString("card_pin_mode");
        postbackUrl = json.getString("postback_url");
        paymentMethod = json.getString("payment_method");
        captureMethod = json.getString("capture_method");
        antifraudScore = json.getString("antifraud_score");
        acquireName = json.getString("acquire_name");
        billetUrl = json.getString("boleto_url");
        billetBarcode = json.getString("boleto_barcode");
        billetExpirationDate = json.getString("boleto_expiration_date");
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public String getAcquirerResponseCode() {
        return acquirerResponseCode;
    }

    public void setAcquirerResponseCode(String acquirerResponseCode) {
        this.acquirerResponseCode = acquirerResponseCode;
    }

    public String getAcquireName() {
        return acquireName;
    }

    public void setAcquireName(String acquireName) {
        this.acquireName = acquireName;
    }

    public String getAcquireID() {
        return acquireID;
    }

    public void setAcquireID(String acquireID) {
        this.acquireID = acquireID;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getSoftDescription() {
        return softDescription;
    }

    public void setSoftDescription(String softDescription) {
        this.softDescription = softDescription;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getNsu() {
        return nsu;
    }

    public void setNsu(int nsu) {
        this.nsu = nsu;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(int authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(int refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCardOlderName() {
        return cardOlderName;
    }

    public void setCardOlderName(String cardOlderName) {
        this.cardOlderName = cardOlderName;
    }

    public String getCardLastDigits() {
        return cardLastDigits;
    }

    public void setCardLastDigits(String cardLastDigits) {
        this.cardLastDigits = cardLastDigits;
    }

    public String getCardFirstdigits() {
        return cardFirstdigits;
    }

    public void setCardFirstdigits(String cardFirstdigits) {
        this.cardFirstdigits = cardFirstdigits;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardPinMode() {
        return cardPinMode;
    }

    public void setCardPinMode(String cardPinMode) {
        this.cardPinMode = cardPinMode;
    }

    public String getPostbackUrl() {
        return postbackUrl;
    }

    public void setPostbackUrl(String postbackUrl) {
        this.postbackUrl = postbackUrl;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCaptureMethod() {
        return captureMethod;
    }

    public void setCaptureMethod(String captureMethod) {
        this.captureMethod = captureMethod;
    }

    public String getAntifraudScore() {
        return antifraudScore;
    }

    public void setAntifraudScore(String antifraudScore) {
        this.antifraudScore = antifraudScore;
    }

    public String getBilletUrl() {
        return billetUrl;
    }

    public void setBilletUrl(String billetUrl) {
        this.billetUrl = billetUrl;
    }

    public String getBilletBarcode() {
        return billetBarcode;
    }

    public void setBilletBarcode(String billetBarcode) {
        this.billetBarcode = billetBarcode;
    }

    public String getBilletExpirationDate() {
        return billetExpirationDate;
    }

    public void setBilletExpirationDate(String billetExpirationDate) {
        this.billetExpirationDate = billetExpirationDate;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
