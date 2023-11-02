package br.com.hotbdelivery.deliveryman;

import java.io.Serializable;

import apis.location.LAddress;
import apis.userbuyer.BAccountBank;
import apis.userbuyer.BContact;
import apis.userbuyer.BDocument;

public class HotDeliveryMan implements Serializable {

    private int id ;
    private String idRecipient, idDevice, hashKey, name, email, dateOfBirth, socialLogin, status ;
    private boolean statusOperation ;
    private BAccountBank bankAccount ;
    private BDocument document ;
    private BContact contact ;
    private LAddress address ;
    private Motorcycle motorcycle  ;

    public HotDeliveryMan(){
        bankAccount = new BAccountBank();
        document = new BDocument();
        contact = new BContact();
        address = new LAddress();
        motorcycle = new Motorcycle();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStatus() {
        return status;
    }

    public boolean isStatusOperation() {
        return statusOperation;
    }

    public void setStatusOperation(boolean statusOperation) {
        this.statusOperation = statusOperation;
    }

    public BAccountBank getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BAccountBank bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BDocument getDocument() {
        return document;
    }

    public void setDocument(BDocument document) {
        this.document = document;
    }

    public BContact getContact() {
        return contact;
    }

    public void setContact(BContact contact) {
        this.contact = contact;
    }

    public LAddress getAddress() {
        return address;
    }

    public void setAddress(LAddress address) {
        this.address = address;
    }

    public Motorcycle getMotorcycle() {
        return motorcycle;
    }

    public void setMotorcycle(Motorcycle motorcycle) {
        this.motorcycle = motorcycle;
    }

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSocialLogin() {
        return socialLogin;
    }

    public void setSocialLogin(String socialLogin) {
        this.socialLogin = socialLogin;
    }

    public class Status{
        public static final String WAITING = "waiting";
        public static final String INACTIVE = "inactive";
        public static final String ACTIVE = "active";
    }
}
