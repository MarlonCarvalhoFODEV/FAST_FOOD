package apis.pagarme.userbuyer;


import java.io.Serializable;
import java.util.ArrayList;


public class UserBuyer implements Serializable {

    private int id, idCustomer, idStore, installments ;
    private double feeInstallment ;
    private String name, birthday ;
    private byte[] infSecurity, keyOrder;
    private BDocument document ;
    private BContact contact ;
    private BAddress defaultAddress ;
    private ArrayList<Integer> coupons ;
    private ArrayList<BProduct> products ;
    private ArrayList<BAddress> addresses ;
    private BCard card ;

    public UserBuyer(){
        this.document = new BDocument();
        this.contact = new BContact();
        this.defaultAddress = new BAddress();
        this.coupons = new ArrayList<>();
        this.addresses = new ArrayList<>();
        this.card = new BCard();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public double getFeeInstallment() {
        return feeInstallment;
    }

    public void setFeeInstallment(double feeInstallment) {
        this.feeInstallment = feeInstallment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public byte[] getKeyOrder() {
        return keyOrder;
    }

    public void setKeyOrder(byte[] keyOrder) {
        this.keyOrder = keyOrder;
    }

    public byte[] getInfSecurity() {
        return infSecurity;
    }

    public void setInfSecurity(byte[] infSecurity) {
        this.infSecurity = infSecurity;
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

    public void setDefaultAddress(BAddress defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public BAddress getAddressDefault() {
        return defaultAddress ;
    }

    public BAddress getAddress(int index) {
        return addresses.get(index);
    }

    public void addAddresses(BAddress address) {
        this.addresses.add(address);
    }

    public BCard getCard() {
        return card;
    }

    public void setCard(BCard card) {
        this.card = card;
    }

    public void addProduct(BProduct product){
        products.add(product);
    }

    public ArrayList<BProduct> getProducts() {
        return products;
    }

    public int getAmount(){
        int amount = 0;
        for(BProduct p : products){
            amount += p.getAmount();
        }
        //amount += getAddressDefault().getFeeDelivery();
        return amount ;
    }

    public int getAmount(boolean isDeliveryProductTangible){
        int amount = 0;
        for(BProduct p : products){
            amount += p.getAmount();
        }
        if(isDeliveryProductTangible)
            amount += getAddressDefault().getFeeDelivery();

        return amount ;
    }
}
