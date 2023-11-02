package apis.card;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.hotburguer.R;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.connection.WebController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import apis.encrypt.RSA;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import apis.userbuyer.BCard;

public class CardManager {

    public static final String HIPERCARD = "HIPERCARD";
    public static final String MASTERCARD = "MASTERCARD";
    public static final String VISA = "VISA";
    public static final String ELO = "ELO";
    public static final String DINERS_CLUB = "DINERS_CLUB";
    public static final String AMEX = "AMERICAN EXPRESS";
    public static final String DISCOVER = "DISCOVER";
    public static final String AURA = "AURA";
    public static final String JBC = "JBC";
    public static final String CARD = "CARD";

    public static BrandCard detectBrandCard(String cardNumber) {
        try {
            cardNumber = cardNumber.replace(" ", "");

            boolean visa = cardNumber.length() >= 1 && cardNumber.substring(0, 1).equals("4");
            if (visa) return new BrandCard(R.drawable.ic_card_visa, "VISA");

            boolean diners = cardNumber.length() >= 3 && (cardNumber.substring(0, 3).equals("301") || cardNumber.substring(0, 3).equals("305")
                    || cardNumber.substring(0, 2).equals("36") || cardNumber.substring(0, 2).equals("38"));
            if (diners) return new BrandCard(R.drawable.ic_card_diners_club, "DINERS CLUB");

            boolean elo = cardNumber.length() >= 6 && (cardNumber.substring(0, 6).equals("636368") || cardNumber.substring(0, 6).equals("438935")
                    || cardNumber.substring(0, 6).equals("504175") || cardNumber.substring(0, 6).equals("451416")
                    || cardNumber.substring(0, 6).equals("509048") || cardNumber.substring(0, 6).equals("509067")
                    || cardNumber.substring(0, 6).equals("509049") || cardNumber.substring(0, 6).equals("509069")
                    || cardNumber.substring(0, 6).equals("509050") || cardNumber.substring(0, 6).equals("509074")
                    || cardNumber.substring(0, 6).equals("509068") || cardNumber.substring(0, 6).equals("509040")
                    || cardNumber.substring(0, 6).equals("509045") || cardNumber.substring(0, 6).equals("509051")
                    || cardNumber.substring(0, 6).equals("509046") || cardNumber.substring(0, 6).equals("509066")
                    || cardNumber.substring(0, 6).equals("509042") || cardNumber.substring(0, 6).equals("509047")
                    || cardNumber.substring(0, 6).equals("509052") || cardNumber.substring(0, 6).equals("509043")
                    || cardNumber.substring(0, 6).equals("509064") || cardNumber.substring(0, 6).equals("509040"))
                    || cardNumber.length() >= 5 && cardNumber.substring(0, 5).equals("36297") || cardNumber.length() >= 4 && (cardNumber.substring(0, 4).equals("5067")
                    || cardNumber.substring(0, 4).equals("4576") || cardNumber.substring(0, 4).equals("4011"));
            if (elo) return new BrandCard(R.drawable.ic_card_elo, "ELO");

            boolean mastercard = cardNumber.length() >= 1 && cardNumber.substring(0, 1).equals("5");
            if (mastercard && !elo)
                return new BrandCard(R.drawable.ic_card_master_card, "MASTERCARD");

            boolean amex = cardNumber.length() >= 2 && (cardNumber.substring(0, 2).equals("34") || cardNumber.substring(0, 2).equals("37"));
            if (amex) return new BrandCard(R.drawable.ic_card_american_express, "AMERICAN EXPRESS");

            boolean discover = cardNumber.length() >= 4 && cardNumber.substring(0, 4).equals("6011") || cardNumber.length() >= 3 && cardNumber.substring(0, 3).equals("622")
                    || cardNumber.substring(0, 2).equals("64") || cardNumber.substring(0, 2).equals("65");
            if (discover) return new BrandCard(R.drawable.ic_card_discover, "DISCOVER");

            boolean aura = cardNumber.length() >= 2 && cardNumber.substring(0, 2).equals("50");
            if (aura) return new BrandCard(R.drawable.ic_card, "AURA");

            boolean jcb = cardNumber.length() >= 2 && cardNumber.substring(0, 2).equals("35");
            if (jcb) return new BrandCard(R.drawable.ic_card_jbc, "JBC");

            boolean hipercard = cardNumber.length() >= 2 && cardNumber.substring(0, 2).equals("38") || cardNumber.substring(0, 2).equals("60");
            if (hipercard) return new BrandCard(R.drawable.ic_card_hiper_card, "HIPERCARD");

        } catch (StringIndexOutOfBoundsException e) {

        }

        return new BrandCard(R.drawable.ic_card, "CARD");
    }

    public static BrandCard detectIcon(String brand) {

        boolean visa = brand.equalsIgnoreCase(VISA);
        if (visa) return new BrandCard(R.drawable.ic_card_visa, VISA);

        boolean diners = brand.equalsIgnoreCase(DINERS_CLUB);
        if (diners) return new BrandCard(R.drawable.ic_card_diners_club, DINERS_CLUB);

        boolean elo = brand.equalsIgnoreCase(ELO);
        if (elo) return new BrandCard(R.drawable.ic_card_elo, ELO);

        boolean mastercard = brand.equalsIgnoreCase(MASTERCARD);
        if (mastercard && !elo) return new BrandCard(R.drawable.ic_card_master_card, MASTERCARD);

        boolean amex = brand.equalsIgnoreCase(AMEX);
        if (amex) return new BrandCard(R.drawable.ic_card_american_express, AMEX);

        boolean discover = brand.equalsIgnoreCase(DISCOVER);
        if (discover) return new BrandCard(R.drawable.ic_card_discover, DISCOVER);

        boolean aura = brand.equalsIgnoreCase(AURA);
        if (aura) return new BrandCard(R.drawable.ic_card, AURA);

        boolean jcb = brand.equalsIgnoreCase(JBC);
        if (jcb) return new BrandCard(R.drawable.ic_card_jbc, JBC);

        boolean hipercard = brand.equalsIgnoreCase(HIPERCARD);
        if (hipercard) return new BrandCard(R.drawable.ic_card_hiper_card, HIPERCARD);

        return new BrandCard(R.drawable.ic_card, CARD);
    }

    public static class HashManager {

        private Context context;
        private BCard card ;

        public HashManager(Context context, BCard card) {
            this.context = context;
            this.card = card ;
        }

        private PublicKey generatePublicKey(String hashKeyPublicRSA){
                try {
                    byte[] objectBytes = Base64.decode(hashKeyPublicRSA, Base64.DEFAULT);
                    KeyFactory factory = KeyFactory.getInstance("RSA");
                    return factory.generatePublic(new X509EncodedKeySpec(objectBytes));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                return null ;
        }

        public String getCardHash() {
            InternetRequisitionAPI requestServer = CustomApplication.getApiRequisition();
            requestServer.setIdRequisition("getCardHash()");
            requestServer.setTypeRequest(InternetRequisition.TypeRequest.GET);
            requestServer.useCryptographToSend(InternetRequisition.TypeCryptograph.AES);
            requestServer.useCryptographToReceiver(InternetRequisition.TypeCryptograph.AES);
            requestServer.setDomain(WebController.getDomain(context));
            requestServer.setController(WebController.Checkout.CONTROLLER);
            requestServer.setFunction(WebController.Checkout.Functions.GET_PUBLIC_KEY);
            String response = requestServer.request(((Activity) context).getClass().getSimpleName());

            if(response != null){
                try {
                    String json = new Gson().fromJson(response, String.class);
                    JSONObject obj = new JSONObject(json);

                    int id = obj.getInt("id");
                    String hashKey = obj.getString("public_key");
                    hashKey = hashKey.replace("-----BEGIN PUBLIC KEY-----\n","")
                            .replace("\n-----END PUBLIC KEY-----","");

                    StringBuilder queryString = new StringBuilder();
                    queryString.append("card_number=");
                    queryString.append(card.getNumber());
                    queryString.append("&");
                    queryString.append("card_holder_name=");
                    queryString.append(card.getHolderName().replace(" ","%20"));
                    queryString.append("&");
                    queryString.append("card_expiration_date=");
                    queryString.append(card.getExpirationDate());
                    queryString.append("&");
                    queryString.append("card_cvv=");
                    queryString.append(card.getCVV());

                    PublicKey publicKey = generatePublicKey(hashKey);
                    RSA rsa = new RSA(publicKey, null);
                    byte[] queryStringEncrypted = rsa.encrypt(queryString.toString());

                    String queryStringBase64 = Base64.encodeToString(queryStringEncrypted, Base64.DEFAULT);

                    StringBuilder cardHash = new StringBuilder();
                    cardHash.append(id);
                    cardHash.append("_");
                    cardHash.append(queryStringBase64);

                    return cardHash.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            return null ;
        }

    }

    public static class BrandCard {
        private int idIcon;
        private String brand;

        public BrandCard(int idIcon, String brand) {
            this.idIcon = idIcon;
            this.brand = brand;
        }

        public int getIdIcon() {
            return idIcon;
        }

        public void setIdIcon(int idIcon) {
            this.idIcon = idIcon;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }
}
