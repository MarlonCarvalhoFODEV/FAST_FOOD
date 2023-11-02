package apis.pagarme.divisionrule;

import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.SplitRule;
import me.pagar.model.Transaction;

import java.util.Collection;

public class DivisionRuleManager {
    private final String API_KEY ;
    private final String URL_ROOT = "https://api.pagar.me/1/transactions/transaction_id/split_rules/split_rule_id";

    public DivisionRuleManager(String apiKey){
        this.API_KEY = apiKey;
    }

    public SplitRule getDivisionRule(DDivisionRule div) throws PagarMeException {
        PagarMe.init(API_KEY);
        Transaction trx = new Transaction().find(div.getIdTransaction());
        return trx.splitRules(div.getIdSplitRule());
    }

    public Collection<SplitRule> getSplitRules(int idTransaction) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new Transaction().find(idTransaction).splitRules();
    }
}
