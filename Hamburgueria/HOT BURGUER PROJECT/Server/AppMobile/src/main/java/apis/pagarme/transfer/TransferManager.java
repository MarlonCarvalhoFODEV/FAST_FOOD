package apis.pagarme.transfer;

import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Transfer;

import java.util.Collection;

public class TransferManager {
    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/transfers";

    public TransferManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public Transfer createTransfer(String idRecipient, int amount) throws PagarMeException {
        PagarMe.init(API_KEY);

        Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setRecipientId(idRecipient);
        return transfer.save();
    }

    public Collection<Transfer> getAllTransfer(int countPage, int page) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Transfer().findCollection(countPage, page);
    }

    public Transfer getTransfer(String idTransfer) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new Transfer().find(idTransfer);
    }

    public Transfer cancelTransfer(String idTransfer) throws PagarMeException {
        PagarMe.init(API_KEY);

        Transfer transfer = new Transfer().find(idTransfer);
        return transfer.cancel();
    }
}
