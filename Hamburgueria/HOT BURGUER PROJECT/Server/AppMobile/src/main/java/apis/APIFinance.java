package apis;

public class APIFinance {
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

    public static double treatDecimal(double value){

        String val = String.valueOf(value);
        String first = val.substring(0,val.indexOf("."));
        String second = val.substring(val.indexOf(".")+1);

        int decimal = second.length()==1?((Integer.valueOf(second)*10)-1):Integer.valueOf(second);
        return Double.valueOf(first+"."+decimal);
    }
}
