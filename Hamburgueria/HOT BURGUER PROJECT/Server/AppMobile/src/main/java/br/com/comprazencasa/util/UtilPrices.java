/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

import br.com.comprazencasa.database.ConnectionDB_APP;
import br.com.comprazencasa.itens.ITCoupon;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.market.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Set;

/**
 *
 * @author Marlon
 */
public class UtilPrices{

    public synchronized static double formatPrice(double price){
        DecimalFormat dec = new DecimalFormat("0.##");
        return Double.valueOf(dec.format(price).replace(",","."));
    }

    public synchronized static double calculateValueIntetest(Pedido order, int parcels){
        double valDelivery = getFeeFelivery(order.getValorCompra())/(1-decodeParcelsToInterest(parcels))-getFeeFelivery(order.getValorCompra());
        double valOrder = order.getValorCompra() + valDelivery;
        double valOrderFinal = (valOrder/(1-decodeParcelsToInterest(parcels)));
        valOrderFinal += valDelivery ;
        return formatPrice((valOrderFinal));
    }

    public synchronized static double calculateValueFinal(Pedido order, Set<ITCoupon> coupons, int parcels){
        double valOrder = calculateValueIntetest(order, parcels);
        valOrder = calculateValueFeeDelivery(valOrder);
        double discount = getTotalDiscount(order, coupons);
        return formatPrice((valOrder-discount));
    }

    public synchronized static double calculateValueFinalNoFeeDelivery(Pedido order, Set<ITCoupon> coupons, int parcels){
        double valOrder = calculateValueIntetest(order, parcels);
        double discount = getTotalDiscount(order, coupons);
        return formatPrice((valOrder-discount));
    }

    public synchronized static double getTotalDiscount(Pedido order, Set<ITCoupon> coupons){
        double discount = 0 ;
        for(ITCoupon coupon : coupons){
            discount += calculateDiscount(order, coupon);
        }
        return formatPrice(discount) ;
    }

    private synchronized static double calculateDiscount(Pedido order, ITCoupon coupon){

        double discount = 0;
        for(ITProduct p : order.values()){

            if(coupon.getDiscountLimit() != 0 && discount < coupon.getDiscountLimit()
                    || coupon.getDiscountLimit() == 0){
                // Desconta se for desconto direto do produto (TESTED OK)
                if(coupon.getIdProduct() == p.getId() && coupon.getTake() == 0){
                    if(coupon.isPercentual() && coupon.getTitle().contains("%")){
                        discount += (coupon.getDiscount()/100)*(p.getPerPrice()*p.getCount());
                    } else if(!coupon.isPercentual() && !coupon.getTitle().contains("%")){
                        discount += p.getPerPrice()-(coupon.getDiscount());
                    }

                } else if( coupon.isPercentual() && coupon.getCategorie().equalsIgnoreCase(p.getCategorie()) &&
                        p.getCount() > 20){

                    discount += (coupon.getDiscount()/100.0)*(p.getPerPrice()*(p.getCount()/1000.0));
                }
                // Desconta se o cupom for desconto de produtos de categoria (TESTED OK)
                else if(coupon.getCategorie() != null && !coupon.getCategorie().isEmpty()){
                    if( coupon.isPercentual() && coupon.getCategorie().equals(p.getCategorie())){
                        discount += (coupon.getDiscount()/100)*(p.getPerPrice()*p.getCount());
                    }
                    else if(!coupon.isPercentual() && coupon.getCategorie().equals(p.getCategorie())){
                        discount += p.getPerPrice()-(coupon.getDiscount());
                    }
                }
                // Desconta se for Leve e Pague (TESTED OK)
                else if(p.getCount() >= coupon.getTake() && coupon.getTake() != 0){

                    int counterX = 0 ;
                    for(int i=1; i <= p.getCount(); i++){
                        // a cada 3,4 ou 5... => coupon.getTake()
                        if((i % coupon.getTake() == 0)) {
                            counterX++;
                        }
                    }
                    discount += (coupon.getDiscount()*counterX);
                }
            }

            if(discount >= coupon.getDiscountLimit()){
                discount = coupon.getDiscountLimit();
            }
        }

        return discount ;
    }

    public synchronized static double calculateValueFeeDelivery(double valOrder){
        valOrder += deliveryChargeScheme(valOrder);
        return formatPrice(valOrder);
    }


    public synchronized static double getFeeFelivery(double valOrder){
        return deliveryChargeScheme(valOrder);
    }

    private synchronized static double deliveryChargeScheme(double priceOrder){
        if(priceOrder < 20){
            return 6.99 ;
        }
        else if(priceOrder >= 20 && priceOrder < 40){
            return  6.49;
        }
        else if(priceOrder >= 40 && priceOrder < 60){
            return  5.99;
        }
        else if(priceOrder >= 60 && priceOrder < 80){
            return  5.49;
        }
        else if(priceOrder >= 80 && priceOrder < 100){
            return  4.99;
        }
        else if(priceOrder >= 100){
            return  4.49;
        }

        return 4.49;
    }

    private synchronized static double decodeParcelsToInterest(int parcels){

        double otherFee = 4.99 ;

        switch (parcels){
            case 1: return 0 ;
            case 2: return ((2.39 + otherFee)/100);
            case 3: return ((4.78 + otherFee)/100);
            case 4: return ((7.17 + otherFee)/100);
            case 5: return ((8.99 + otherFee)/100);
            default: return 0;
        }
    }

    public synchronized static double decodeParcels(int parcels){

        switch (parcels){
            case 1: return 0 ;
            case 2: return ((2.39)/100);
            case 3: return ((4.78)/100);
            case 4: return ((7.17)/100);
            case 5: return ((8.99)/100);
            default: return 0;
        }
    }

    public synchronized static double getFeeTimeReceiver(){
        return (4.99/100) ;
    }

    public synchronized static double decodeFeeParcels(int parcels){
        return decodeParcelsToInterest(parcels);
    }

    public synchronized static double calculateRates(double czc, double credit,double productPrice){
        return  formatPrice((productPrice/(1-((czc + credit)/100)))) ;
    }

    public static synchronized RateServices getLastRate(){

        try {
            String sql = "SELECT * FROM rate_services ORDER BY date DESC LIMIT 1" ;
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            RateServices rate = null;
            if(rs.next()){
                rate = new RateServices();
                rate.setId(rs.getInt("id"));
                rate.setRateComprazemCasa(rs.getDouble("rate_per_czc"));
                rate.setRatePercentualCredit(rs.getDouble("rate_per_credit"));
            }
            pst.close();
            con.close();
            return rate ;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null ;
    }

    public static void recalculateValueOrder(Pedido pedido){

        Connection con = ConnectionDB_APP.connect();
        PreparedStatement pst = null ;

        if(con != null){

            String sql = "" ;
            try {
                pst = con.prepareStatement(sql);

                pst.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
