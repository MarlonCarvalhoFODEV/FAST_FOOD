package apis.pagarme.divisionrule;

import java.io.Serializable;

public class DDivisionRule implements Serializable {

    private String idTransaction, idSplitRule ;

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getIdSplitRule() {
        return idSplitRule;
    }

    public void setIdSplitRule(String idSplitRule) {
        this.idSplitRule = idSplitRule;
    }
}
