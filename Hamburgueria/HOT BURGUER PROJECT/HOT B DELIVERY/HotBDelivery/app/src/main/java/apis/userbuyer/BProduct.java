package apis.userbuyer;

import java.io.Serializable;

public class BProduct implements Serializable {
 
    private int id, percentualDiscount, quantity=1;
    private String name, category;
    private int price, valueDiscount ;
    private boolean tangible, isWeight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPercentualDiscount() {
        return percentualDiscount;
    }

    public void setPercentualDiscount(int percentualDiscount) {
        this.percentualDiscount = percentualDiscount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if(isWeight){
            this.price = (((price/1000)*100)*quantity);

        } else {
            this.price = price ;
        }
    }

    public double getValueDiscount() {
        return valueDiscount;
    }

    public void setValueDiscount(int valueDiscount) {
        this.valueDiscount = valueDiscount;
    }

    public int getAmount(){
        return Integer.valueOf(quantity * price);
    }

    public int getAmount(int shippingFee){
        return Integer.valueOf((quantity * price)+shippingFee);
    }

    public boolean isTangible() {
        return tangible;
    }

    public void setTangible(boolean tangible) {
        this.tangible = tangible;
    }

    public boolean isWeight() {
        return isWeight;
    }

    public void setWeight(boolean weight) {
        isWeight = weight;
    }
}
