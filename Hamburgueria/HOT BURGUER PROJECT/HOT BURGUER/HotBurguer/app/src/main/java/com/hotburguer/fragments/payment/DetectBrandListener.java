package com.hotburguer.fragments.payment;

import android.text.Editable;
import android.text.TextWatcher;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hotburguer.R;

public class DetectBrandListener implements TextWatcher {

    private FMT_Payment.Controller checkoutViews ;
    private int idResourcerDefined = -1 ;

    public DetectBrandListener(FMT_Payment.Controller checkoutViews){
        this.checkoutViews = checkoutViews ;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        BrandCard brand = detectBrandCard(s.toString());
        if(brand.getIdIcon() != -1 && brand.getIdIcon() != idResourcerDefined){
            idResourcerDefined = brand.getIdIcon() ;

            checkoutViews.getIvIcon().setImageResource(brand.getIdIcon());

            YoYo.with(Techniques.FlipInX)
                    .duration(500)
                    .repeat(0)
                    .playOn(checkoutViews.getIvIcon());

            checkoutViews.getCardData().setBrand(brand.getBrand());

        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static BrandCard detectBrandCard(String cardNumber){
        try {
            cardNumber = cardNumber.replace(" ","");

            boolean visa = cardNumber.length() >=1 && cardNumber.substring(0, 1).equals("4");
            if(visa) return new BrandCard( R.drawable.ic_card_visa,"VISA");

            boolean diners = cardNumber.length() >=3 && (cardNumber.substring(0, 3).equals("301") || cardNumber.substring(0, 3).equals("305")
                    || cardNumber.substring(0, 2).equals("36") || cardNumber.substring(0, 2).equals("38"));
            if(diners) return new BrandCard(R.drawable.ic_card_diners_club, "DINERS CLUB");

            boolean elo = cardNumber.length() >=6 && (cardNumber.substring(0, 6).equals("636368") || cardNumber.substring(0, 6).equals("438935")
                    || cardNumber.substring(0, 6).equals("504175") || cardNumber.substring(0, 6).equals("451416")
                    || cardNumber.substring(0, 6).equals("509048") || cardNumber.substring(0, 6).equals("509067")
                    || cardNumber.substring(0, 6).equals("509049") || cardNumber.substring(0, 6).equals("509069")
                    || cardNumber.substring(0, 6).equals("509050") || cardNumber.substring(0, 6).equals("509074")
                    || cardNumber.substring(0, 6).equals("509068") || cardNumber.substring(0, 6).equals("509040")
                    || cardNumber.substring(0, 6).equals("509045") || cardNumber.substring(0, 6).equals("509051")
                    || cardNumber.substring(0, 6).equals("509046") || cardNumber.substring(0, 6).equals("509066")
                    || cardNumber.substring(0, 6).equals("509042") || cardNumber.substring(0, 6).equals("509047")
                    || cardNumber.substring(0, 6).equals("509052") || cardNumber.substring(0, 6).equals("509043")
                    || cardNumber.substring(0, 6).equals("509064") || cardNumber.substring(0, 6).equals("509040"))
                    || cardNumber.length() >=5 && cardNumber.substring(0, 5).equals("36297") || cardNumber.length() >=4 && (cardNumber.substring(0, 4).equals("5067")
                    || cardNumber.substring(0, 4).equals("4576") || cardNumber.substring(0, 4).equals("4011"));
            if(elo) return new BrandCard(R.drawable.ic_card_elo,"ELO");

            boolean mastercard = cardNumber.length() >=1 && cardNumber.substring(0, 1).equals("5");
            if(mastercard && !elo) return new BrandCard(R.drawable.ic_card_master_card, "MASTERCARD");

            boolean amex = cardNumber.length() >=2 && (cardNumber.substring(0, 2).equals("34") || cardNumber.substring(0, 2).equals("37"));
            if(amex) return new BrandCard(R.drawable.ic_card_american_express, "AMERICAN EXPRESS");

            boolean discover = cardNumber.length() >= 4 && cardNumber.substring(0, 4).equals("6011") || cardNumber.length() >=3 && cardNumber.substring(0, 3).equals("622")
                    || cardNumber.substring(0, 2).equals("64") || cardNumber.substring(0, 2).equals("65");
            if(discover) return new BrandCard(R.drawable.ic_card_discover, "DISCOVER");

            boolean aura = cardNumber.length() >=2 && cardNumber.substring(0, 2).equals("50");
            if(aura) return new BrandCard(R.drawable.ic_card_aura,"AURA");

            boolean jcb = cardNumber.length() >=2 && cardNumber.substring(0, 2).equals("35");
            if(jcb) return new BrandCard(R.drawable.ic_card_jbc,"JBC");

            boolean hipercard = cardNumber.length() >=2 && cardNumber.substring(0, 2).equals("38") || cardNumber.substring(0, 2).equals("60");
            if(hipercard) return new BrandCard(R.drawable.ic_card_hiper_card,"HIPERCARD");

        }catch (StringIndexOutOfBoundsException e){

        }

        return new BrandCard(R.drawable.ic_card, "CARD");
    }

    public static BrandCard detectIconCardFromBrand(String brand){
        try {
            boolean visa = brand.equalsIgnoreCase("VISA");
            if(visa) return new BrandCard( R.drawable.ic_card_visa,"VISA");

            boolean diners = brand.equalsIgnoreCase("DINERS CLUB");
            if(diners) return new BrandCard(R.drawable.ic_card_diners_club, "DINERS CLUB");

            boolean elo = brand.equalsIgnoreCase("ELO");
            if(elo) return new BrandCard(R.drawable.ic_card_elo,"ELO");

            boolean mastercard = brand.equalsIgnoreCase("MASTERCARD");
            if(mastercard) return new BrandCard(R.drawable.ic_card_master_card, "MASTERCARD");

            boolean amex = brand.equalsIgnoreCase("AMERICAN EXPRESS");
            if(amex) return new BrandCard(R.drawable.ic_card_american_express, "AMERICAN EXPRESS");

            boolean discover = brand.equalsIgnoreCase("DISCOVER");
            if(discover) return new BrandCard(R.drawable.ic_card_discover, "DISCOVER");

            boolean aura = brand.equalsIgnoreCase("AURA");
            if(aura) return new BrandCard(R.drawable.ic_card_aura,"AURA");

            boolean jcb = brand.equalsIgnoreCase("JBC");
            if(jcb) return new BrandCard(R.drawable.ic_card_jbc,"JBC");

            boolean hipercard = brand.equalsIgnoreCase("HIPERCARD");
            if(hipercard) return new BrandCard(R.drawable.ic_card_hiper_card,"HIPERCARD");

        }catch (StringIndexOutOfBoundsException e){

        }

        return new BrandCard(R.drawable.ic_card, "CARD");
    }

    public static class BrandCard{
        private int idIcon ;
        private String brand ;

        public BrandCard(int idIcon, String brand){
            this.idIcon = idIcon ;
            this.brand = brand ;
        }

        public int getIdIcon() {
            return idIcon;
        }

        public void setIdIcon(int idIcon) {
            this.idIcon = idIcon;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }
}