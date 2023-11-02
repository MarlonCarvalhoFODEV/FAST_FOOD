package apis.promotion;

import apis.APIFinance;
import br.com.comprazencasa.itens.ITCoupon;
import br.com.comprazencasa.itens.ITProduct;
import br.com.comprazencasa.market.Pedido;

import java.util.Set;

public class PromotionAPI {

    public static double caculateDiscount(Pedido order, Set<ITCoupon> coupons){
        double discount = 0 ;
        for(ITCoupon coupon : coupons){

            for(ITProduct product : order.values()){
                discount += discountTakeAndPay(product, coupon);
                discount += discountCategorie(product, coupon);
                discount += discountProduct(product, coupon);
            }

        }
        return APIFinance.format(discount,2) ;
    }

    public static double discountTakeAndPay(ITProduct p, ITCoupon coupon){
        double discount = 0 ;
        if(p.getCount() >= coupon.getTake() && coupon.getTake() != 0){

            int counterX = 0 ;
            for(int i=1; i <= p.getCount(); i++){
                // a cada 3,4 ou 5... => coupon.getTake()
                if((i % coupon.getTake() == 0)) {
                    counterX++;
                }
            }
            discount += (coupon.getDiscount()*counterX);
        }

        if(discount >= coupon.getDiscountLimit()){
            discount = coupon.getDiscountLimit();
        }

        return APIFinance.format(discount,2);
    }

    public static double discountCategorie(ITProduct p, ITCoupon coupon){
        double discount = 0 ;
        if(coupon.getCategorie() != null && !coupon.getCategorie().isEmpty()){
            if( coupon.isPercentual() && coupon.getCategorie().equals(p.getCategorie())){
                discount += (coupon.getDiscount()/100)*(p.getPerPrice()*p.getCount());
            }
            else if(!coupon.isPercentual() && coupon.getCategorie().equals(p.getCategorie())){
                discount += p.getPerPrice()-(coupon.getDiscount());
            }
        }
        if(discount >= coupon.getDiscountLimit()){
            discount = coupon.getDiscountLimit();
        }

        return APIFinance.format(discount,2);
    }

    public static double discountProduct(ITProduct p, ITCoupon coupon){
        double discount = 0;
        if(coupon.getIdProduct() == p.getId() && coupon.getTake() == 0){
            if(coupon.isPercentual() && coupon.getTitle().contains("%")){
                discount += (coupon.getDiscount()/100)*(p.getPerPrice()*p.getCount());
            } else if(!coupon.isPercentual() && !coupon.getTitle().contains("%")){
                discount += p.getPerPrice()-(coupon.getDiscount());
            }
        }

        if(discount >= coupon.getDiscountLimit()){
            discount = coupon.getDiscountLimit();
        }

        return APIFinance.format(discount,2);
    }

}
