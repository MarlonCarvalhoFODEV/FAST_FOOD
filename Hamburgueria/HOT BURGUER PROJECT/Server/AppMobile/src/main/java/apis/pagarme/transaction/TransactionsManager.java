package apis.pagarme.transaction;

import apis.APIFinance;
import apis.date.DateManager;
import apis.internet.WebJson;
import apis.pagarme.system.SystemConfigurations;
import apis.pagarme.userbuyer.BProduct;
import apis.pagarme.userbuyer.UserBuyer;
import br.com.comprazencasa.database.DaoPagarme;
import me.pagar.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;

public class TransactionsManager {

    private final String API_KEY ;
    private final String URL_ROOT = "https://api.pagar.me/1/transactions/";

    public TransactionsManager(String apiKey){
        this.API_KEY = apiKey;
    }

    public Transaction createTransaction(UserBuyer buyer) throws ParseException {
        PagarMe.init(API_KEY);

        Transaction transaction = new Transaction();

        Customer customer = new Customer();
        customer.setType(buyer.getDocument().getType()==0?Customer.Type.INDIVIDUAL:Customer.Type.CORPORATION);
        customer.setId(buyer.getIdCustomer());
        customer.setExternalId(String.valueOf(buyer.getId()));
        customer.setName(buyer.getName());
        customer.setBirthday(DateManager.format("dd-MM-yyyy","yyyy-MM-dd",buyer.getBirthday()));
        customer.setEmail(buyer.getContact().getEmail());
        customer.setCountry(buyer.getAddressDefault().getCountry());

        Collection<Document> documents = new ArrayList();
        Document document = new Document();
        document.setType(buyer.getDocument().getType()==0? Document.Type.CPF: Document.Type.CNPJ);
        document.setNumber(buyer.getDocument().get());
        documents.add(document);
        customer.setDocuments(documents);

        Collection<String> phones = new ArrayList();
        phones.add(buyer.getContact().getPhone().getFullNumber());
        customer.setPhoneNumbers(phones);

        // Obrigatório com o antifraude habilitado. Define os dados de cobrança, como nome e endereço
        Billing billing = new Billing();
        billing.setName(buyer.getName());

        Address address = new Address();
        address.setCity(buyer.getAddressDefault().getCity());
        address.setCountry(buyer.getAddressDefault().getCountry());
        address.setState(buyer.getAddressDefault().getState());
        address.setNeighborhood(buyer.getAddressDefault().getNeighborhood());
        address.setStreet(buyer.getAddressDefault().getStreet());
        address.setZipcode(buyer.getAddressDefault().getZipCode().replace("-",""));
        address.setStreetNumber(buyer.getAddressDefault().getStreetNumber());
        billing.setAddress(address);

        /*
        * Define os dados de envio da compra, como nome do recebedor, valor do envio e endereço de recebimento. Deve ser preenchido no caso da venda de bem físico (ver objeto items)*/
        Shipping shipping = new Shipping();
        shipping.setAddress(address);
        shipping.setName(buyer.getName());
        shipping.setFee(buyer.getAddressDefault().getFeeDelivery());
        shipping.setExpedited(buyer.getAddressDefault().isExpedited());

        Collection<Item> items = new ArrayList();

        boolean isDeliveryProductTangible = false ;
        for(BProduct product : buyer.getProducts()){
            Item item = new Item();
            item.setId(String.valueOf(product.getId()));
            item.setQuantity(product.getQuantity());
            item.setTangible(product.isTangible()?Boolean.TRUE:Boolean.FALSE);
            item.setTitle(product.getName());
            item.setUnitPrice((int)product.getPrice());

            if(item.getTangible()) {
                isDeliveryProductTangible = true;
            }

            items.add(item);
        }

        if(isDeliveryProductTangible){
            Item item = new Item();
            item.setId("DELIVERY");
            item.setQuantity(1);
            item.setTangible(true);
            item.setTitle("SERVIÇO DE ENTREGA");
            item.setUnitPrice(buyer.getAddressDefault().getFeeDelivery());
            items.add(item);

        }

        if(buyer.getCard().getId() != null && !buyer.getCard().getId().isEmpty()){
            transaction.setCardId(buyer.getCard().getId());
        }
        else if(buyer.getCard().getCardHash() != null && !buyer.getCard().getCardHash().isEmpty())
            transaction.setCardHash(buyer.getCard().getCardHash());
        else {
            transaction.setCardHolderName(buyer.getCard().getHolderName());
            transaction.setCardNumber(buyer.getCard().getNumber());
            transaction.setCardCvv(buyer.getCard().getCVV());
            transaction.setCardExpirationDate(buyer.getCard().getExpirationDate());
        }

        transaction.setInstallments(buyer.getInstallments());
        transaction.setCardId(buyer.getCard().getId());
        transaction.setShipping(shipping);
        transaction.setBilling(billing);
        transaction.setItems(items);
        transaction.setPaymentMethod(decodePaymentMethod(buyer.getCard().getPaymentMethod()));
        transaction.setAmount(buyer.getAmount(isDeliveryProductTangible));
        transaction.setCustomer(customer);

        if(buyer.getCard().getId() != null && !buyer.getCard().getId().isEmpty())
            transaction.setCardId(buyer.getCard().getId());

        try {
            transaction.save();
            return transaction ;
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction createTransactionCapture(UserBuyer buyer) throws ParseException {
        PagarMe.init(API_KEY);

        Transaction transaction = new Transaction();

        Customer customer = new Customer();
        customer.setType(buyer.getDocument().getType()==0?Customer.Type.INDIVIDUAL:Customer.Type.CORPORATION);
        customer.setId(buyer.getIdCustomer());
        customer.setExternalId(String.valueOf(buyer.getId()));
        customer.setName(buyer.getName());
        customer.setBirthday(DateManager.format("dd-MM-yyyy","yyyy-MM-dd",buyer.getBirthday()));
        customer.setEmail(buyer.getContact().getEmail());
        customer.setCountry(buyer.getAddressDefault().getCountry());

        Collection<Document> documents = new ArrayList();
        Document document = new Document();
        document.setType(buyer.getDocument().getType()==0? Document.Type.CPF: Document.Type.CNPJ);
        document.setNumber(buyer.getDocument().get());
        documents.add(document);
        customer.setDocuments(documents);

        Collection<String> phones = new ArrayList();
        phones.add(buyer.getContact().getPhone().getFullNumber());
        customer.setPhoneNumbers(phones);

        // Obrigatório com o antifraude habilitado. Define os dados de cobrança, como nome e endereço
        Billing billing = new Billing();
        billing.setName(buyer.getName());

        Address address = new Address();
        address.setCity(buyer.getAddressDefault().getCity());
        address.setCountry(buyer.getAddressDefault().getCountry());
        address.setState(buyer.getAddressDefault().getState());
        address.setNeighborhood(buyer.getAddressDefault().getNeighborhood());
        address.setStreet(buyer.getAddressDefault().getStreet());
        address.setZipcode(buyer.getAddressDefault().getZipCode().replace("-",""));
        address.setStreetNumber(buyer.getAddressDefault().getStreetNumber());
        billing.setAddress(address);

        /*
         * Define os dados de envio da compra, como nome do recebedor, valor do envio e endereço de recebimento. Deve ser preenchido no caso da venda de bem físico (ver objeto items)*/
        Shipping shipping = new Shipping();
        shipping.setAddress(address);
        shipping.setName(buyer.getName());
        shipping.setFee(buyer.getAddressDefault().getFeeDelivery());
        shipping.setExpedited(buyer.getAddressDefault().isExpedited());

        Collection<Item> items = new ArrayList();

        boolean isDeliveryProductTangible = false ;
        for(BProduct product : buyer.getProducts()){
            Item item = new Item();
            item.setId(String.valueOf(product.getId()));
            item.setQuantity(product.getQuantity());
            item.setTangible(product.isTangible()?Boolean.TRUE:Boolean.FALSE);
            item.setTitle(product.getName());
            item.setUnitPrice((int)product.getPrice());

            if(item.getTangible() && !isDeliveryProductTangible) {
                isDeliveryProductTangible = true;
                Item delivery = new Item();
                delivery.setId("DELIVERY");
                delivery.setQuantity(1);
                delivery.setTangible(true);
                delivery.setTitle("SERVIÇO DE ENTREGA");
                delivery.setUnitPrice(buyer.getAddressDefault().getFeeDelivery());
                items.add(delivery);
            }

            items.add(item);
        }

        if(buyer.getCard().getId() != null && !buyer.getCard().getId().isEmpty()){
            transaction.setCardId(buyer.getCard().getId());
        }
        else if(buyer.getCard().getCardHash() != null && !buyer.getCard().getCardHash().isEmpty())
            transaction.setCardHash(buyer.getCard().getCardHash());
        else {
            transaction.setCardHolderName(buyer.getCard().getHolderName());
            transaction.setCardNumber(buyer.getCard().getNumber());
            transaction.setCardCvv(buyer.getCard().getCVV());
            transaction.setCardExpirationDate(buyer.getCard().getExpirationDate());
        }


        transaction.setInstallments(buyer.getInstallments());
        transaction.setCardId(buyer.getCard().getId());
        transaction.setShipping(shipping);
        transaction.setBilling(billing);
        transaction.setItems(items);
        transaction.setPaymentMethod(decodePaymentMethod(buyer.getCard().getPaymentMethod()));
        transaction.setAmount(buyer.getAmount(isDeliveryProductTangible));
        transaction.setCustomer(customer);
        transaction.setCapture(false);

        if(buyer.getCard().getId() != null && !buyer.getCard().getId().isEmpty())
            transaction.setCardId(buyer.getCard().getId());

        try {
            transaction.save();
            return transaction ;
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction captureTransaction(int idTransaction, int value) throws ParseException {
        PagarMe.init(API_KEY);

        Transaction tx = null;
        try {
            tx = new Transaction().find(idTransaction);
            tx.capture(value);
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction captureTransactionSplit(int idTransaction, int totalValue,String idRecipientMaster, String idRecipient, int splitValue) throws ParseException {
        PagarMe.init(API_KEY);

        Transaction tx = null;
        try {
            SplitRule splitMaster = new SplitRule();
            splitMaster.setRecipientId(idRecipientMaster);
            splitMaster.setAmount(totalValue-splitValue);

            SplitRule split = new SplitRule();
            split.setRecipientId(idRecipient);
            split.setAmount(splitValue);
            split.setChargeProcessingFee(false);

            tx = new Transaction().find(idTransaction);
            tx.setSplitRules(Arrays.asList(splitMaster, split));
            tx.capture(totalValue);

            return tx ;
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction createSplitTransaction(UserBuyer buyer, SystemConfigurations configs) throws ParseException {
        PagarMe.init(API_KEY);

        Transaction transaction = new Transaction();

        Customer customer = new Customer();
        customer.setType(buyer.getDocument().getType()==0?Customer.Type.INDIVIDUAL:Customer.Type.CORPORATION);
        customer.setExternalId(String.valueOf(buyer.getId()));
        customer.setName(buyer.getName());
        customer.setBirthday(DateManager.format("dd-MM-yyyy","yyyy-MM-dd",buyer.getBirthday()));
        customer.setEmail(buyer.getContact().getEmail());
        customer.setCountry(buyer.getAddressDefault().getCountry());

        Collection<Document> documents = new ArrayList();
        Document document = new Document();
        document.setType(buyer.getDocument().getType()==0? Document.Type.CPF: Document.Type.CNPJ);
        document.setNumber(buyer.getDocument().get());
        documents.add(document);
        customer.setDocuments(documents);

        Collection<String> phones = new ArrayList();
        phones.add(buyer.getContact().getPhone().getFullNumber());
        customer.setPhoneNumbers(phones);

        // Obrigatório com o antifraude habilitado. Define os dados de cobrança, como nome e endereço
        Billing billing = new Billing();
        billing.setName(buyer.getName());

        Address address = new Address();
        address.setCity(buyer.getAddressDefault().getCity());
        address.setCountry(buyer.getAddressDefault().getCountry());
        address.setState(buyer.getAddressDefault().getState());
        address.setNeighborhood(buyer.getAddressDefault().getNeighborhood());
        address.setStreet(buyer.getAddressDefault().getStreet());
        address.setZipcode(buyer.getAddressDefault().getZipCode().replace("-",""));
        address.setStreetNumber(buyer.getAddressDefault().getStreetNumber());
        billing.setAddress(address);

        /*
         * Define os dados de envio da compra, como nome do recebedor, valor do envio e endereço de recebimento. Deve ser preenchido no caso da venda de bem físico (ver objeto items)*/
        Shipping shipping = new Shipping();
        shipping.setAddress(address);
        shipping.setName(buyer.getName());
        shipping.setFee(buyer.getAddressDefault().getFeeDelivery());
        shipping.setExpedited(buyer.getAddressDefault().isExpedited());

        Collection<Item> items = new ArrayList();

        boolean isDeliveryProductTangible = false ;
        for(BProduct product : buyer.getProducts()){
            Item item = new Item();
            item.setId(String.valueOf(product.getId()));
            item.setQuantity(product.getQuantity());
            item.setTangible(product.isTangible()?Boolean.TRUE:Boolean.FALSE);
            item.setTitle(product.getName());
            item.setUnitPrice((int)product.getPrice());

            if(item.getTangible()) {
                isDeliveryProductTangible = true;
            }

            items.add(item);
        }

        if(isDeliveryProductTangible){
            Item item = new Item();
            item.setId("DELIVERY");
            item.setQuantity(1);
            item.setTangible(false);
            item.setTitle("SERVIÇO DE ENTREGA");
            item.setUnitPrice(buyer.getAddressDefault().getFeeDelivery());
            items.add(item);
        }

        int ammount = buyer.getAmount(isDeliveryProductTangible);
        ammount += ammount*(buyer.getFeeInstallment()/100);

        transaction.setInstallments(buyer.getInstallments());
        transaction.setCardId(buyer.getCard().getId());
        transaction.setShipping(shipping);
        transaction.setBilling(billing);
        transaction.setItems(items);
        transaction.setPaymentMethod(decodePaymentMethod(buyer.getCard().getPaymentMethod()));
        transaction.setAmount(ammount);
        transaction.setCardHolderName(buyer.getCard().getHolderName());
        transaction.setCardNumber(buyer.getCard().getNumber());
        transaction.setCardCvv(buyer.getCard().getCVV());
        transaction.setCardExpirationDate(buyer.getCard().getExpirationDate());
        transaction.setCustomer(customer);

        SplitRule rule = new SplitRule();
        rule.setRecipientId(configs.getIdStoreRecipient());
        rule.setPercentage((100-configs.getPaymentSys().getCommission()));

        SplitRule ruleRoot = new SplitRule();
        if(!API_KEY.contains("test"))
            ruleRoot.setRecipientId(configs.getPaymentSys().getSystemId());
        else
            ruleRoot.setRecipientId("re_cka0useo70iqwt56d6n9w48h8");
        ruleRoot.setPercentage((configs.getPaymentSys().getCommission()));

        List<SplitRule> rules = new ArrayList<>();
        rules.add(rule);
        rules.add(ruleRoot);

        transaction.setSplitRules(rules);

        if(buyer.getCard().getId() != null && !buyer.getCard().getId().isEmpty())
            transaction.setCardId(buyer.getCard().getId());
        try {
            transaction.save();
            return transaction ;
        } catch (PagarMeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Shipping createShipping(Address address, String buyerName, int feeDelivery, String deliveryDate, boolean expedited){
        Shipping shipping = new Shipping();
        shipping.setAddress(address);
        shipping.setName(buyerName);
        shipping.setFee(feeDelivery);
        shipping.setExpedited(expedited);
        shipping.setDeliveryDate(deliveryDate);
        return shipping ;
    }

    public Transaction captureTransactionAfterApproved(int idTransactionOrToken, int value) throws PagarMeException {
        PagarMe.init(API_KEY);

        Transaction tx = new Transaction().find(idTransactionOrToken);
        tx.capture(value);
        return tx ;
    }

    public Transaction chargebackTotalTransaction(int idTransaction) throws PagarMeException {
        PagarMe.init(API_KEY);

        Transaction tx = new Transaction().find(idTransaction);
        return tx.refund(tx.getAmount());
    }

    public Transaction.Status chargebackPartialTransaction(int idTransaction, int valuePartial) throws PagarMeException {
        PagarMe.init(API_KEY);

        Transaction tx = new Transaction().find(idTransaction);

        if(valuePartial <= tx.getAmount())
            tx.refund(valuePartial);

        return tx.getStatus();
    }

    public boolean chargebackPartialWithSplitTransaction(int idTransaction, int valuePartial) throws PagarMeException {
       return false ;
    }

    public Collection<Transaction> getTransactions(int totalPerPage, int page) throws PagarMeException {
        PagarMe.init(API_KEY);

        Collection<Transaction> txs = new Transaction().findCollection(totalPerPage, page);
        return txs ;
    }

    public Transaction getTransaction(int idTransaction) throws PagarMeException {
        PagarMe.init(API_KEY);

        Transaction tx = new Transaction().find(idTransaction);
        return tx ;
    }

    public Collection<Payable> getReceivablesTransaction(int idTransaction) throws PagarMeException {
        PagarMe.init(API_KEY);
        Collection<Payable> payables = new Transaction().find(idTransaction).payables();
        return payables;
    }

    public Payable getReceivableTransaction(int idTransaction, int idReceivable) throws PagarMeException {
        PagarMe.init(API_KEY);
        Payable p = new Transaction().find(idTransaction).payables(idReceivable);
        return p;
    }

    // Revisão
    public ArrayList<HistoricRegister> getHistoricTransaction(int idTransaction) throws JSONException {
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append(idTransaction);
        url.append("/operations?api_key=");
        url.append(API_KEY);

        String json = new WebJson().getJson(url.toString(), null);
        JSONArray array = new JSONArray(json);

        ArrayList<HistoricRegister> historic = new ArrayList<>();

        HistoricRegister reg ;
        for(int i=0; i < array.length(); i++){
            reg = new HistoricRegister((JSONObject) array.get(i));
            historic.add(reg);
        }

        return historic ;
    }

    // Revisão
    public String notifyClientBilletToBePaid(int idTransaction, String email) throws JSONException {
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append(idTransaction);
        url.append("/collect_payment?api_key=");
        url.append(API_KEY);

        JSONObject obJson = new JSONObject();
        obJson.put("email",email);

        String json = new WebJson().jsonRequest("POST",url.toString(),obJson, null);
        return json ;
    }

    public String getAllEventsTransaction(int idTransaction){
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append(idTransaction);
        url.append("/events?api_key=");
        url.append(API_KEY);

        String json = new WebJson().getJson(url.toString(), null);
        return json ;
    }

    public ArrayList<Parcel> calculatePaymentsParcel(int ammount, int freeInstalments, int interestRate, int maxInstalments) throws JSONException {
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append("/calculate_installments_amount?amount=");
        url.append(ammount);
        url.append("&api_key=");
        url.append(API_KEY);
        url.append("&free_installments=");
        url.append(freeInstalments);
        url.append("&interest_rate=");
        url.append(interestRate);
        url.append("&max_installments=");
        url.append(maxInstalments);

        String json = new WebJson().getJson(url.toString(), null);
        JSONObject root = new JSONObject(json);

        JSONObject installments = root.getJSONObject("installments");

        ArrayList<Parcel> parcels = new ArrayList<>();

        int parcel = 1;
        while(parcel <= maxInstalments){
            JSONObject parcelOb = installments.getJSONObject(String.valueOf(parcel));

            int par = parcelOb.getInt("installment");
            int amo = parcelOb.getInt("amount");
            int instAmo = parcelOb.getInt("installment_amount");
            parcels.add(new Parcel(par,amo,instAmo));

            parcel++;
        }

        return parcels ;
    }

    public Billet testingPaymentBillets(int idTransaction) throws JSONException {
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append(idTransaction);

        String json = new WebJson().jsonRequest("PUT",url.toString(), null);

        return new Billet(new JSONObject(json));
    }

    public String generateKeyEncryptCardHash(String cardNumber, String cardHolderName, String cardExpirationDate, String CVV){
        StringBuilder url = new StringBuilder();
        url.append(URL_ROOT);
        url.append("/card_hash_key?api_key=");
        url.append(API_KEY);

        String json = new WebJson().getJson(url.toString(), null);

        StringBuilder queryString = new StringBuilder();
        queryString.append("card_number=");
        queryString.append(cardNumber);
        queryString.append("&card_holder_name=");
        queryString.append(cardHolderName);
        queryString.append("&card_expiration_date=");
        queryString.append(cardExpirationDate);
        queryString.append("&card_cvv=");
        queryString.append(CVV);


        // Encryptação RSA

        // PKCS1Padding

        // Base64

        return json ;
    }

    private Transaction.PaymentMethod decodePaymentMethod(int paymentMethod){
        switch (paymentMethod){
            case 0: return Transaction.PaymentMethod.CREDIT_CARD;
            case 1: return Transaction.PaymentMethod.DEBIT_CARD;
            case 2: return Transaction.PaymentMethod.BOLETO;
            default:return null ;
        }
    }


    public static class Status{
        public static final String PROCESSING = "processing";
        public static final String POSTBACK_URL = "postback_url";
        public static final String AUTHORIZED = "authorized";
        public static final String PAID = "paid";
        public static final String REFUSED = "refused";
        public static final String REFUNDED = "refunded";
        public static final String CHARGEDBACK = "chargedback";
        public static final String ANALYZING = "analyzing";
        public static final String PENDING_REVIEW = "pending_review";

        private static Map<String, String> messageStatus = new HashMap<>();
        private static Map<String, Integer> idStatus = new HashMap<>();

        public static String getMessageFrom(String status){
            messageStatus.clear();
            messageStatus.put(PROCESSING,"Transação está em processo de autorização.");
            messageStatus.put(POSTBACK_URL,"");
            messageStatus.put(AUTHORIZED,"Transação foi autorizada. Você possui saldo na conta e este valor foi reservado para futura captura.");
            messageStatus.put(PAID,"Pagamento realizado com sucesso!");
            messageStatus.put(REFUNDED,"Transação estornada completamente.");
            messageStatus.put(REFUSED,"Transação recusada, não autorizada.");
            messageStatus.put(CHARGEDBACK,"Transação sofreu chargeback.");
            messageStatus.put(ANALYZING,"Transação encaminhada para a análise manual feita por um especialista em prevenção a fraude.");
            messageStatus.put(PENDING_REVIEW,"Transação pendente de revisão manual por parte do lojista. Uma transação ficará com esse status por até 48 horas corridas.");
            return messageStatus.get(status.toLowerCase());
        }

        public static int getStatusFrom(String status){
            idStatus.clear();
            idStatus.put(PROCESSING,Payment.PROCESSING);
            idStatus.put(POSTBACK_URL,Payment.POSTBACK_URL);
            idStatus.put(AUTHORIZED,Payment.AUTHORIZED);
            idStatus.put(PAID,Payment.PAID);
            idStatus.put(REFUNDED, Payment.REFUNDED);
            idStatus.put(REFUSED, Payment.REFUSED);
            idStatus.put(CHARGEDBACK, Payment.CHARGEBACK);
            return idStatus.get(status.toLowerCase());
        }

        public static String getMsgAlert(String status){
            messageStatus.clear();
            messageStatus.put(PROCESSING,"Em Processamento");
            messageStatus.put(AUTHORIZED,"Autorizado");
            messageStatus.put(PAID,"Obrigado");
            messageStatus.put(REFUNDED, "Devolvido");
            messageStatus.put(REFUSED, "Recusado");
            messageStatus.put(CHARGEDBACK, "Estornado");
            return messageStatus.get(status.toLowerCase());
        }

        public static class Billet{

            private static Map<String, String> messageStatus = new HashMap<>();
            public static final String PAID = "paid";
            public static final String PEDING_REFUND = "pending_refund";
            public static final String WAIT_PAYMENT = "waiting_payment";

            public String getMessageFromStatus(String status){
                messageStatus.clear();
                messageStatus.put(PAID,"Transação paga. Foi autorizada e capturada com sucesso.");
                messageStatus.put(PEDING_REFUND,"Transação do tipo Boleto e que está aguardando confirmação do estorno solicitado");
                messageStatus.put(WAIT_PAYMENT,"Transação aguardando pagamento (status válido para Boleto bancário).");
                return messageStatus.get(status.toLowerCase());
            }
        }
    }
}
