package apis.pagarme.recipient;

import apis.internet.WebJson;
import apis.pagarme.transaction.HistoricRegister;
import me.pagar.RecipientStatus;
import me.pagar.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class RecipientManager {
    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/recipients/";

    public RecipientManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public Recipient createRecipient(RRecipient neoRecipient, BankAccount account) throws PagarMeException {
        PagarMe.init(API_KEY);

        Recipient recipient = new Recipient();
        recipient.setAnticipatableVolumePercentage(neoRecipient.getAnticipateVolPercentage());
        recipient.setAutomaticAnticipationEnabled(neoRecipient.isAutomaticAnticipation());
        //recipient.setBankAccountId(account.getId());
        recipient.setBankAccount(account);
        recipient.setTransferDay(neoRecipient.getDayTransfer());
        recipient.setTransferEnabled(neoRecipient.isAutomaticTransfer());
        recipient.setTransferInterval(decodeTransferInterval(neoRecipient.getTransferIntervalType()));

        return recipient.save();
    }

    public Collection<Recipient> getRecipients(int countPage, int page) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new Recipient().findCollection(countPage, page);
    }

    public Recipient getRecipient(String idRecipient) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new Recipient().find(idRecipient);
    }

    public Recipient updateRecipient(RRecipient r) throws PagarMeException {
        PagarMe.init(API_KEY);
        Recipient recipient = new Recipient().find(r.getIdRecipient());

        recipient.setAnticipatableVolumePercentage(r.getAnticipateVolPercentage());
        recipient.setAutomaticAnticipationEnabled(r.isAutomaticAnticipation());
        recipient.setTransferDay(r.getDayTransfer());
        recipient.setTransferEnabled(r.isTransfer());
        recipient.setTransferInterval(decodeTransferInterval(r.getTransferIntervalType()));
        return recipient.save();
    }

    public Balance getBalanceRecipient(String idRecipient) throws PagarMeException {
        PagarMe.init(API_KEY);

        Recipient recipient = new Recipient().find(idRecipient);
        return recipient.balance();
    }

    public void getOneMovimentationRecipient(String idRecipient) throws JSONException {
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append(idRecipient);
        url.append("/balance/operations/?api_key=");
        url.append(API_KEY);

        String json = new WebJson().getJson(url.toString(), null);
        JSONArray array = new JSONArray(json);

        ArrayList<HistoricRegister> historic = new ArrayList<>();

        HistoricRegister reg ;
        for(int i=0; i < array.length(); i++){
            reg = new HistoricRegister((JSONObject) array.get(i));
            historic.add(reg);
        }

    }

    private static final me.pagar.model.Recipient.TransferInterval decodeTransferInterval(int code) {
        switch (code) {
            case RRecipient.Type.DAiLY:
                return me.pagar.model.Recipient.TransferInterval.DAILY;
            case RRecipient.Type.MONTHLY:
                return me.pagar.model.Recipient.TransferInterval.MONTHLY;
            case RRecipient.Type.WEEKLY:
                return me.pagar.model.Recipient.TransferInterval.WEEKLY;
            default:
                return me.pagar.model.Recipient.TransferInterval.MONTHLY;
        }
    }

    private static final me.pagar.model.Recipient.TransferInterval decodeTransferInterval(String type) {
        switch (type) {
            case RRecipient.TypeS.DAILY:
                return me.pagar.model.Recipient.TransferInterval.DAILY;
            case RRecipient.TypeS.MONTHLY:
                return me.pagar.model.Recipient.TransferInterval.MONTHLY;
            case RRecipient.TypeS.WEEKLY:
                return me.pagar.model.Recipient.TransferInterval.WEEKLY;
            default:
                return me.pagar.model.Recipient.TransferInterval.MONTHLY;
        }
    }
}
