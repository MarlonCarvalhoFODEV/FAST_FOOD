package apis.pagarme.balance;

import me.pagar.model.Balance;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Recipient;

public class BalanceManager {

    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/balance/";

    public BalanceManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public Balance getBalanceAccount(String idPrincipalReceiver) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Recipient().find(idPrincipalReceiver).balance();
    }

    public void getHistoricOperations(){}

    public void getHistoricOperation(){}
}
