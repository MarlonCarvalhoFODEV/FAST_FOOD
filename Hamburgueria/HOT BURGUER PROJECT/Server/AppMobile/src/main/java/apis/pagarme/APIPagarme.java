package apis.pagarme;

import apis.pagarme.accountbank.AccountBankManager;
import apis.pagarme.anticipation.AnticipationManager;
import apis.pagarme.antifraud.AntifraudManager;
import apis.pagarme.balance.BalanceManager;
import apis.pagarme.card.CardManager;
import apis.pagarme.customer.CustomerManager;
import apis.pagarme.divisionrule.DivisionRuleManager;
import apis.pagarme.plans.PlansManager;
import apis.pagarme.posback.PostbackManager;
import apis.pagarme.postalcode.PostalCodeManager;
import apis.pagarme.receivable.ReceivableManager;
import apis.pagarme.recipient.RecipientManager;
import apis.pagarme.refunds.RefundsManager;
import apis.pagarme.subscriptions.SignatureManager;
import apis.pagarme.transaction.TransactionsManager;
import apis.pagarme.transfer.TransferManager;

public class APIPagarme{
    private AccountBankManager accountBankManager ;
    private AnticipationManager anticipationManager ;
    private AntifraudManager antifraudManager ;
    private BalanceManager balanceManager ;
    private CardManager cardManager ;
    private CustomerManager customerManager ;
    private DivisionRuleManager divisionRuleManager ;
    private PlansManager plansManager ;
    private PostbackManager postbackManager ;
    private PostalCodeManager postalCodeManager ;
    private ReceivableManager receivableManager ;
    private RecipientManager recipientManager ;
    private RefundsManager refundsManager ;
    private SignatureManager signatureManager ;
    private TransactionsManager transactionsManager ;
    private TransferManager transferManager ;

    public APIPagarme(String API_KEY){
        accountBankManager = new AccountBankManager(API_KEY);
        anticipationManager = new AnticipationManager(API_KEY);
        antifraudManager = new AntifraudManager(API_KEY);
        balanceManager = new BalanceManager(API_KEY);
        cardManager = new CardManager(API_KEY);
        customerManager = new CustomerManager(API_KEY);
        divisionRuleManager = new DivisionRuleManager(API_KEY);
        plansManager = new PlansManager(API_KEY);
        postbackManager = new PostbackManager(API_KEY);
        postalCodeManager = new PostalCodeManager(API_KEY);
        receivableManager = new ReceivableManager(API_KEY);
        recipientManager = new RecipientManager(API_KEY);
        refundsManager = new RefundsManager(API_KEY);
        signatureManager = new SignatureManager(API_KEY);
        transactionsManager = new TransactionsManager(API_KEY);
        transferManager = new TransferManager(API_KEY);
    }

    public AccountBankManager getAccountBankManager() {
        return accountBankManager;
    }

    public void setAccountBankManager(AccountBankManager accountBankManager) {
        this.accountBankManager = accountBankManager;
    }

    public AnticipationManager getAnticipationManager() {
        return anticipationManager;
    }

    public void setAnticipationManager(AnticipationManager anticipationManager) {
        this.anticipationManager = anticipationManager;
    }

    public AntifraudManager getAntifraudManager() {
        return antifraudManager;
    }

    public void setAntifraudManager(AntifraudManager antifraudManager) {
        this.antifraudManager = antifraudManager;
    }

    public BalanceManager getBalanceManager() {
        return balanceManager;
    }

    public void setBalanceManager(BalanceManager balanceManager) {
        this.balanceManager = balanceManager;
    }

    public CardManager getCardManager() {
        return cardManager;
    }

    public void setCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public DivisionRuleManager getDivisionRuleManager() {
        return divisionRuleManager;
    }

    public void setDivisionRuleManager(DivisionRuleManager divisionRuleManager) {
        this.divisionRuleManager = divisionRuleManager;
    }

    public PlansManager getPlansManager() {
        return plansManager;
    }

    public void setPlansManager(PlansManager plansManager) {
        this.plansManager = plansManager;
    }

    public PostbackManager getPostbackManager() {
        return postbackManager;
    }

    public void setPostbackManager(PostbackManager postbackManager) {
        this.postbackManager = postbackManager;
    }

    public PostalCodeManager getPostalCodeManager() {
        return postalCodeManager;
    }

    public void setPostalCodeManager(PostalCodeManager postalCodeManager) {
        this.postalCodeManager = postalCodeManager;
    }

    public ReceivableManager getReceivableManager() {
        return receivableManager;
    }

    public void setReceivableManager(ReceivableManager receivableManager) {
        this.receivableManager = receivableManager;
    }

    public RecipientManager getRecipientManager() {
        return recipientManager;
    }

    public void setRecipientManager(RecipientManager recipientManager) {
        this.recipientManager = recipientManager;
    }

    public RefundsManager getRefundsManager() {
        return refundsManager;
    }

    public void setRefundsManager(RefundsManager refundsManager) {
        this.refundsManager = refundsManager;
    }

    public SignatureManager getSignatureManager() {
        return signatureManager;
    }

    public void setSignatureManager(SignatureManager signatureManager) {
        this.signatureManager = signatureManager;
    }

    public TransactionsManager getTransactionsManager() {
        return transactionsManager;
    }

    public void setTransactionsManager(TransactionsManager transactionsManager) {
        this.transactionsManager = transactionsManager;
    }

    public TransferManager getTransferManager() {
        return transferManager;
    }

    public void setTransferManager(TransferManager transferManager) {
        this.transferManager = transferManager;
    }
}