package com.hotburguer.acustom;

import androidx.annotation.Nullable;

import com.hotburguer.itens.HotProduct;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart  extends HashMap<Integer, HotProduct> implements Serializable {
    private double valorCompra ;

    public double addItem(HotProduct item) {
        put(item.getId(), item);
        DecimalFormat df = new DecimalFormat("0.##");

        String price = df.format(valorCompra += Double.valueOf(item.getPerPriceMultiply()));

        return valorCompra = Double.valueOf(price.replace(",","."));
    }

    public HotProduct getProduct(@Nullable int idProduct) {
        return get(idProduct);
    }

    public void removeItem(HotProduct item, boolean directClick) {
        DecimalFormat df = new DecimalFormat("0.##");

        valorCompra -= item.getPerPriceMultiply();
        valorCompra = Double.valueOf(df.format(valorCompra).replace(",","."));

        if(item.getCount() > 1 || item.getCount() > 50)
            addItem(item);

        if(containsKey(item.getId()) && (item.getCount()==0 || item.getCount()==50)
                || directClick == true)
            remove(item.getId());
    }

    public int getSize(){
        int size = 0;
        for(HotProduct p : values()){
            size += p.getCount();
        }
        return size;
    }

    public void clearCart(){
        clear();
        valorCompra = 0.00 ;
    }

    public double getPurchaseValue() {
        if (getSize() >= 1) {
            valorCompra = 0;
            for (HotProduct p : values()) {
                valorCompra += p.getPerPriceMultiply();
            }

            DecimalFormat df = new DecimalFormat("0.##");
            return (valorCompra = Double.valueOf(df.format(valorCompra).replace(",", ".")));
        } else {
            valorCompra = 0;
            return valorCompra;
        }
    }

    public void setPurchaseValue(double valorCompra){
        this.valorCompra = valorCompra ;
    }

    public double addPurchaseValue(double valor) {
        valorCompra = 0 ;
        for(HotProduct p : values()){
            valorCompra += p.getPerPriceMultiply();
        }
        valorCompra += valor ;
        DecimalFormat df = new DecimalFormat("0.##");

        return (valorCompra = Double.valueOf(df.format(valorCompra).replace(",",".")));
    }

    public Map<String, Integer> getCountProductFromCategorie(){
        Map<String, Integer> map = new HashMap<>();

        List<HotProduct> products = new ArrayList<>(values());
        for(HotProduct p : products){
            if(!map.containsKey(p.getCategorie()))
                map.put(p.getCategorie(), p.getCount());
            else {
                int count = map.get(p.getCategorie());
                count += p.getCount();
                map.put(p.getCategorie(), count);
            }
        }

        return map ;
    }
}