package apis.userbuyer;

public class BAccountBank {
    private String agency ;
    private String agencyDv ;
    private String account ;
    private String accountDv ;
    private String bankCode ;
    private String legalName ;
    private BDocument document ; 

    public BAccountBank(){
        this.document = new BDocument();
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

    public BDocument getDocument() {
        return document;
    }

    public void setDocument(BDocument document) {
        this.document = document;
    }
}
