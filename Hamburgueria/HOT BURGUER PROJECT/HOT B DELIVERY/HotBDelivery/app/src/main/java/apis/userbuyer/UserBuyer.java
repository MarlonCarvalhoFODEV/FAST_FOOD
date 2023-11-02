package apis.userbuyer;


import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;


public class UserBuyer implements Serializable {

    private int id, idCustomer, idStore, installments ;
    private String name, birthday ;
    private byte[] infSecurity, keyOrder;;
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
        this.products = new ArrayList<>();
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

    public byte[] getInfSecurity() {
        return infSecurity;
    }

    public void setInfSecurity(byte[] infSecurity) {
        this.infSecurity = infSecurity;
    }

    public byte[] getKeyOrder() {
        return keyOrder;
    }

    public void setKeyOrder(byte[] keyOrder) {
        this.keyOrder = keyOrder;
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

    public ArrayList<Integer> getCoupons() {
        return coupons;
    }

    public boolean validateCustomer(Context c){
        boolean externalID = getId() > 0 ;
        boolean name = getName() != null && !getName().isEmpty();
        boolean email = getContact().getEmail() != null && !getContact().getEmail().isEmpty();
        boolean country = getAddressDefault().getCountry() != null && !getAddressDefault().getCountry().isEmpty();
        boolean docType = getDocument().getType() > -1 ;
        boolean doc = getDocument().get() != null && !getDocument().get().isEmpty();
        boolean phone = getContact().getPhone().getFullNumber() != null && !getContact().getPhone().getFullNumber().isEmpty();
        boolean itens = getProducts().size() > 0;

        if(externalID){
            if(name){
                if(email){
                    if(country){
                        if(docType){
                            if(doc){
                                if(phone){
                                    if(itens){
                                        return true ;
                                    } else {
                                        Toast.makeText(c, "Necessário itens no carrinho!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(c, "Necessário telefone!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(c, "Necessário documento!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(c, "Necessário tipo de DOC", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(c, "Necessário país!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(c, "Necessário email!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(c, "Necessário nome!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(c, "Necessário ID!", Toast.LENGTH_LONG).show();
        }
        return false ;
    }

    public int sizeProducts(){
        int size = 0;
        for(BProduct p : products){
            size += p.isWeight()?1:p.getQuantity();
        }
        return size;
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
