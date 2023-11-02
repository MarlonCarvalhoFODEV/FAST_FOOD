package apis.pagarme.refunds;

import apis.internet.WebJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RefundsManager {

    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/refunds/";

    public RefundsManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public ArrayList<Refund> getRefunds(int idTransaction, String dateCreated, String dateUpdated) throws JSONException {
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append("?api_key=");
        url.append(API_KEY);
        url.append("&transaction_id=");
        url.append(idTransaction);
        url.append("&date_created=");
        url.append(dateCreated);
        url.append("&date_updated=");
        url.append(dateUpdated);

        String json = new WebJson().getJson(url.toString(), null);
        JSONArray array = new JSONArray(json);

        ArrayList<Refund> refunds = new ArrayList<>();

        Refund ref ;
        for(int i=0; i < array.length(); i++){
            JSONObject ob = new JSONObject(array.get(i).toString());

            ref = new Refund(ob);
            refunds.add(ref);
        }

        return refunds ;
    }

    public void cancelRefundBillet(int idRefund){
        // Só é possivel cancelar o estorno se o status for pending_refund
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append("?api_key=");
        url.append(API_KEY);
        url.append("&refund_id=");
        url.append(idRefund);

        String json = new WebJson().getJson(url.toString(), null);
    }

}
