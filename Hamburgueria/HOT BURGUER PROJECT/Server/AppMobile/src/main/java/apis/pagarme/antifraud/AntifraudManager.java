package apis.pagarme.antifraud;

import me.pagar.model.AntifraudAnalysis;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Transaction;

import java.util.Collection;

public class AntifraudManager {
    private final String API_KEY ;
    private final String URL_ROOT = "https://api.pagar.me/1/transactions/";

    public AntifraudManager(String apiKey){
        this.API_KEY = apiKey;
    }

    public Collection<AntifraudAnalysis> returnAnaliseAntifraud(int idTransaction) throws PagarMeException {
        PagarMe.init(API_KEY);

         return new Transaction()
                .find(idTransaction).antifraudAnalysises();
    }
}
