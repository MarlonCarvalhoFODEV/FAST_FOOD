package apis.pagarme.customer;

import apis.internet.WebJson;
import apis.pagarme.userbuyer.BDocument;
import apis.pagarme.userbuyer.UserBuyer;
import me.pagar.model.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerManager {
    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/customers/";

    public CustomerManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public Customer createCustomer(UserBuyer buyer){
        try {
            PagarMe.init(API_KEY);

            Customer.Type coporation = Customer.Type.CORPORATION;
            Customer.Type individual = Customer.Type.INDIVIDUAL;

            Document.Type docCPF = Document.Type.CPF;
            Document.Type docCNPJ = Document.Type.CNPJ;

            Customer customer = new Customer();
            //customer.setId(buyer.getId());
            customer.setType(buyer.getDocument().getType()== BDocument.Type.CPF?individual:coporation);
            customer.setExternalId(String.valueOf(buyer.getId()));
            customer.setName(buyer.getName());
            customer.setBirthday(buyer.getBirthday());
            customer.setCountry(buyer.getAddressDefault().getCountry().substring(0,2).toLowerCase());//BR
            customer.setEmail(buyer.getContact().getEmail());

            Collection<Document> documents = new ArrayList();
            Document document = new Document();
            document.setType(buyer.getDocument().getType() == BDocument.Type.CPF?docCPF:docCNPJ);
            document.setNumber(buyer.getDocument().get());
            documents.add(document);

            customer.setDocuments(documents);

            Phone phone = new Phone();
            phone.setDdi(String.valueOf(buyer.getContact().getPhone().getDdi()));
            phone.setDdd(String.valueOf(buyer.getContact().getPhone().getDDD()));
            phone.setNumber(String.valueOf(buyer.getContact().getPhone().getNumber()));

            Collection<String> phones = new ArrayList();
            phones.add(buyer.getContact().getPhone().getFullNumber());

            customer.setPhoneNumbers(phones);

            customer.save();

            return customer ;
        } catch (PagarMeException e) {
            e.printStackTrace();
        }

        return null ;
    }

    public Customer createFullCustomer(Customer.Type type, String externalID, String name, String birtDay ){
        /*Customer customer = new Customer();
        customer.setType(type);
        customer.setExternalId(externalID);
        customer.setName(name);
        customer.setBirthday(birtDay);

        Customer customer = new Customer();
        customer.setType(type);
        customer.setExternalId(externalID);
        customer.setName(name);
        customer.setBirthday(birtDay);
        customer.setEmail(email);
        customer.setCountry(country);

        Collection<Document> documents = new ArrayList();
        Document document = new Document();
        document.setType(docType);
        document.setNumber(docNumber);
        documents.add(document);
        customer.setDocuments(documents);

        Collection<String> phones = new ArrayList();
        phones.add(phoneNumber);
        customer.setPhoneNumbers(phones);

        // Obrigatório com o antifraude habilitado. Define os dados de cobrança, como nome e endereço
        Billing billing = new Billing();
        billing.setName(name);

        Address address = new Address();
        address.setCity(city);
        address.setCountry(country);
        address.setState(state);
        address.setNeighborhood(neighborhood);
        address.setStreet(street);
        address.setZipcode(zipCode);
        address.setStreetNumber(streetNumber);
        billing.setAddress(address);

        return customer ;*/
        return null;
    }

    public Customer getCustomer(int idcustomer) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Customer().find(idcustomer);
    }

    public Collection<Customer> getCustomers(int countPage, int page) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Customer().findCollection(countPage, page);
    }

    public boolean updateCustomer(int idCustomer){
        /*StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append(idCustomer);
        url.append("&")
        url.append("&api_key=");
        url.append(API_KEY);

        JSONObject obJson = new JSONObject();
        obJson.put("email",email);

        String json = new WebJson().jsonRequest("POST",url.toString(),obJson, null);
        return json ;*/
        return false ;
    }
}
