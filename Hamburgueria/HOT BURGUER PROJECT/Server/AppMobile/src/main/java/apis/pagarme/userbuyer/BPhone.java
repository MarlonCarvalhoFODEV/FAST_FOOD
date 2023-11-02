package apis.pagarme.userbuyer;

import java.io.Serializable;

public class BPhone implements Serializable {

    private int DDD, type, ddi;
    private String number;

    public BPhone() {
    }

    public BPhone(int ddi, int ddd, String number, int type) {
        this.ddi = ddi;
        this.DDD = ddd;
        this.number = number;
        this.type = type;
    }

    public int getDDD() {
        return DDD;
    }

    public String getDDDString() {
        return String.valueOf(DDD);
    }

    public void setDDD(int DDD) {
        this.DDD = DDD;
    }

    public int getDdi() {
        return ddi;
    }

    public void setDdi(int ddi) {
        this.ddi = ddi;
    }

    public String getDdiString() {
        return String.valueOf(ddi);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getFullNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(ddi);
        sb.append(DDD);
        sb.append(number);
        return sb.toString()
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
                .replace(" ", "");
    }

    public void setFullNumber(String fullNumber) {
        if(fullNumber != null){
            fullNumber = fullNumber
                    .replace(" ", "")
                    .replace("-", "")
                    .replace("(", "")
                    .replace(")", "");
            try {
                String ddi = fullNumber.substring(1, 3);
                String ddd = fullNumber.substring(fullNumber.indexOf("(" + 1), fullNumber.indexOf(")"));
                String number = fullNumber.substring(fullNumber.indexOf(")") + 1);

                this.ddi = Integer.valueOf(ddi);
                this.DDD = Integer.valueOf(ddd);
                this.number = number;
            } catch (StringIndexOutOfBoundsException e) {
                String ddi = fullNumber.substring(1, 3);
                String ddd = fullNumber.substring(3, 5);
                String number = fullNumber.substring(5);

                this.ddi = Integer.valueOf(ddi);
                this.DDD = Integer.valueOf(ddd);
                this.number = number;
            }
        }
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static class Type {
        public static final int CEL = 0;
        public static final int TEL = 1;
    }
}
