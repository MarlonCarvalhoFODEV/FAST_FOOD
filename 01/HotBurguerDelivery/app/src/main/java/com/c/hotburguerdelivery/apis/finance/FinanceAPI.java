package com.c.hotburguerdelivery.apis.finance;

public class FinanceAPI {

    public static double calculateFeePercentual(double value, double fee){
        return (value+(value*(fee/100)));
    }

    public static double calculateFeePercentualReal(double value, double fee){
        double dif = (value/(1.0-(fee/100.0))) - value;
        double percentualDif = ruleOfThree(value, dif) / 100;
        return  (value+(value*percentualDif));
    }

    public static double ruleOfThree(double value, double x){
        return x * 100 / value ;
    }

    public static double format(double value, int countDecimal){
        String formated = String.format("%."+countDecimal+"f", value);
        return Double.valueOf(formated.replace(",","."));
    }

}
