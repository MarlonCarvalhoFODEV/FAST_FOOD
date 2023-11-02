package apis.pagarme.card;

import apis.internet.CURL;
import apis.internet.WebJson;
import apis.pagarme.userbuyer.BCard;
import com.google.gson.Gson;
import me.pagar.model.Card;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class CardManager {

    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/cards/";
    private final String URL_CARD_HASH = "https://api.pagar.me/1/transactions/card_hash_key?api_key=" ;

    public CardManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public Card createCard(String cardNumber, String cardHolderName, String dateExpiration, int CVV) throws PagarMeException {
        PagarMe.init(API_KEY);

        Card card = new Card();
        card.setNumber(cardNumber);
        card.setHolderName(cardHolderName);
        card.setExpiresAt(dateExpiration);
        card.setCvv(CVV);
        return card.save();
    }

    public Card createCard(int customerID, String cardNumber, String cardHolderName, String dateExpiration, String CVV) throws PagarMeException {
        PagarMe.init(API_KEY);

        Card card = new Card();
        card.setNumber(cardNumber);
        card.setHolderName(cardHolderName);
        card.setExpiresAt(dateExpiration);
        card.setCvv(Integer.valueOf(CVV));
        card.setCustomerId(customerID);

        return card.save();
    }

    public Card createCard(int idCustomer, String cardHash) throws PagarMeException {
        PagarMe.init(API_KEY);

        Card card = new Card();
        card.setCustomerId(idCustomer);
        card.setHash(cardHash);

        return card.save();
    }

    public ArrayList<BCard> getAllCards(int customerID) throws JSONException {
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append("?api_key=");
        url.append(API_KEY);
        url.append("&");
        url.append("customer_id=");
        url.append(customerID);

        String json = new WebJson().getJson(url.toString(), null);
        JSONArray array = new JSONArray(json);

        Map<String, BCard> cards = new HashMap<>();

        for(Object o : array){

            JSONObject jo = new JSONObject(o.toString());

            BCard card = new BCard();
            card.setId(jo.getString("id"));
            card.setBrand(jo.getString("brand"));
            card.setLastDigits(jo.getString("last_digits"));

            cards.put(card.getNumber()+card.getCVV()+card.getBrand(), card);
        }

        ArrayList<BCard> olders = new ArrayList<>();
        for(BCard c : cards.values())
            olders.add(c);

        return olders ;
    }

    public Card getCard(String cardId) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Card().find(cardId);
    }

    public String getPublicKeyToCardHash(){
        String publicKey =  new WebJson().getJson(URL_CARD_HASH+API_KEY);
        //publicKey = publicKey.substring(publicKey.indexOf("-\\n")+3, publicKey.indexOf("\\n-"));
        return publicKey ;
    }



    public static class Filter{

        public static final String DATE_CREATED = "created_at";
        public static final String HOLDER_NAME = "holder_name";
        public static final String SIX_FIST_DIGITS = "first_digits";
        public static final String FOUR_LAST_DIGITS = "last_digits";
        public static final String ID_CUSTOMER = "customer_id";
        public static final String VALID_CARD = "valid";
        public static final String COUNTRY = "country";
        public static final String EXPIRATION_DATE = "expiration_date";
        public static final String FINGER_PRINT = "fingerprint";

        public static class Brand{
            public static final String FILTER = "brand";
            public static final String MASTER_CARD = "mastercard";
            public static final String VISA = "visa";
            public static final String ELO = "elo";
            public static final String AMEX = "amex";
            public static final String DISCOVER = "discover";
            public static final String AURA = "aura";
            public static final String HIPERCARD = "hipercard";
        }
    }
}
