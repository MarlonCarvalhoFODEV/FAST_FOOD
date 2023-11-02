package apis.pagarme.posback;

import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Transaction;

public class PostbackManager {
    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/model/model_id/postbacks";

    public PostbackManager(String apiKey) {
        this.API_KEY = apiKey;
    }

    public Transaction resendPostback(int idTransaction, String idPostback, String typeReference) throws PagarMeException {
        PagarMe.init(API_KEY);

        Transaction tx = new Transaction().find(idTransaction);
        tx.postbackRedeliver(idPostback);

        return tx ;
    }

    public void getListPostback(){}

    public void getPostback(){}
}
