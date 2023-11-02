package br.com.comprazencasa.util;

public class IRecargaTeste {
    public static void main(String[] args) {

        /* Recharge parameters (CHANGE THIS) */
        String provider = IRecarga.providersList[1];
        String ddd = "21";
        String number = "992247808";
        String value = "10";

        IRecarga.makeRecharge(ddd, provider,number,value);
    }
}
