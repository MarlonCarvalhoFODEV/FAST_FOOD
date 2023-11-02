package apis.pagarme.userbuyer;

import java.io.Serializable;

public class BDocument implements Serializable {

    private String CPF, RG, CNPJ;
    private int type ;

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String get(){
        switch (type){
            case 0:return CPF;
            case 1:return CNPJ;
            case 2:return RG;
            default: return CPF ;
        }
    }

    public static class Type{
        public static final int CPF = 0;
        public static final int CNPJ = 1;
        public static final int RG = 2;
    }
}
