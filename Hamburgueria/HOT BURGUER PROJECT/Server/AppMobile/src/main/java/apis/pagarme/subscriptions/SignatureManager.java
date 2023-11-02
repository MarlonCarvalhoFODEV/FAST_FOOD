package apis.pagarme.subscriptions;

import apis.pagarme.userbuyer.UserBuyer;
import me.pagar.SubscriptionStatus;
import me.pagar.model.*;

import java.util.Collection;

public class SignatureManager {

    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/subscriptions/";

    public SignatureManager(String apiKey) {
        this.API_KEY = apiKey;
    }

    public Subscription createSignature(UserBuyer ub) throws PagarMeException {
        PagarMe.init(API_KEY);

        Phone phone = new Phone();
        phone.setDdd(ub.getContact().getPhone().getDDDString());
        phone.setDdi(ub.getContact().getPhone().getDdiString());
        phone.setNumber(ub.getContact().getPhone().getNumber());

        String street = ub.getAddressDefault().getStreet();
        String streetNumber = ub.getAddressDefault().getStreetNumber();
        String neighborhood = ub.getAddressDefault().getNeighborhood();
        String zipcode = ub.getAddressDefault().getZipCode();
        Address address = new Address(street, streetNumber, neighborhood, zipcode);

        String name = ub.getName();
        String email = ub.getContact().getEmail();
        String documentNumber = ub.getDocument().get();
        Customer customer = new Customer(name, email);
        customer.setAddress(address);
        customer.setPhone(phone);
        customer.setDocumentNumber(documentNumber);

        Card card = new Card();
        card.setNumber(ub.getCard().getNumber());
        card.setHolderName(card.getHolderName());
        card.setExpiresAt(card.getExpiresAt());
        card.setCvv(card.getCvv());
        card.save();

        Subscription subscription = new Subscription();
        subscription.setCreditCardSubscriptionWithCardId(String.valueOf(ub.getProducts().get(0).getId()), card.getId(), customer);
        return subscription.save() ;
    }

    // Assinatura com Split

    public Subscription getSignature(String idSubscription) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Subscription().find(idSubscription);
    }

    public Collection<Subscription> getSubscriptions(SFilter filter) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Subscription().findCollection(filter.getCountPage(), filter.getPage());
    }

    // atualizar assinatura

    public Subscription cancelSignature(String idSubscription) throws PagarMeException {
        PagarMe.init(API_KEY);

        Subscription subscription = new Subscription().find(idSubscription);
        return subscription.cancel();
    }

    public Collection<Transaction> getAllTransactionsFromSignature(String idSubscription) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Subscription().find(idSubscription).transactions();
    }

    // Pula combranças de assinaturas se o status for unpaid ou pending_payment
    public void skipCharge(String idSubscription, int countSkipCharges) throws PagarMeException {
        PagarMe.init(API_KEY);
        Subscription subs = new Subscription().find(idSubscription);
        subs.settleCharges(countSkipCharges);
    }


}
