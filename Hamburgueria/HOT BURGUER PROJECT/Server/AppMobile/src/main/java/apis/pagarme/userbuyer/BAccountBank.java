package apis.pagarme.userbuyer;

import me.pagar.BankAccountType;

public class BAccountBank {
    private int id, type=-1;
    private String name ;
    private String agency ;
    private String agencyDv ;
    private String account ;
    private String accountDv ;
    private String bankCode ;
    private String legalName ;
    private String typeName ;
    private BDocument document ;
    private boolean defaultAccount ;

    public BAccountBank(){
        this.document = new BDocument();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        this.type = type;
        this.typeName = Type.getTypeName(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getAgencyDv() {
        return agencyDv;
    }

    public void setAgencyDv(String agencyDv) {
        this.agencyDv = agencyDv;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountDv() {
        return accountDv;
    }

    public void setAccountDv(String accountDv) {
        this.accountDv = accountDv;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTypeName() {
        return typeName;
    }

    public BDocument getDocument() {
        return document;
    }

    public void setDocument(BDocument document) {
        this.document = document;
    }

    public boolean isDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public static class Type{
        public static final int CURRENT = 0 ;
        public static final int CURRENT_JOINT = 1 ;
        public static final int SAVINGS = 2 ;
        public static final int SAVINGS_JOINT = 3 ;

        public static String getTypeName(int type){
            switch (type){
                case 0 :return "CORRENTE";
                case 1 :return "CORRENTE CONJUNTA";
                case 2 :return "POUPANÇA";
                case 3 :return "POUPANÇA CONJUNTA";
                default:return "Não Definido";
            }
        }

        public static int getType(String typeName){
            switch (typeName){
                case "CORRENTE":return 0;
                case "CORRENTE CONJUNTA":return 1;
                case "POUPANÇA":return 2;
                case "POUPANÇA CONJUNTA":return 3;
                default:return -1;
            }
        }

    }
}
