package apis.pagarme.postalcode;

import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Zipcode;

public class PostalCodeManager {
    private final String API_KEY ;
    private final String URL_ROOT = "https://api.pagar.me/1/zipcodes/zipcode";

    public PostalCodeManager(String apiKey){
        this.API_KEY = apiKey;
    }

    public Zipcode consultCEP(String zipCode) throws PagarMeException {
        PagarMe.init(API_KEY);
        Zipcode zipcode = new Zipcode();
        return zipcode.checkZipcode(zipCode);
    }
}
