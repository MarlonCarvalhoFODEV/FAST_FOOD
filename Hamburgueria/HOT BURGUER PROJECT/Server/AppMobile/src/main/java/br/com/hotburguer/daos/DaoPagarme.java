package br.com.hotburguer.daos;

import apis.encrypt.Cryptograph;
import apis.pagarme.APIPagarme;
import apis.pagarme.accountbank.AccountBankManager;
import apis.pagarme.anticipation.AnticipationManager;
import apis.pagarme.anticipation.CAnticipation;
import apis.pagarme.antifraud.AntifraudManager;
import apis.pagarme.balance.BalanceManager;
import apis.pagarme.card.CardManager;
import apis.pagarme.customer.CustomerManager;
import apis.pagarme.divisionrule.DDivisionRule;
import apis.pagarme.divisionrule.DivisionRuleManager;
import apis.pagarme.plans.PPlan;
import apis.pagarme.plans.PlansManager;
import apis.pagarme.receivable.RReceivable;
import apis.pagarme.receivable.ReceivableManager;
import apis.pagarme.recipient.RRecipient;
import apis.pagarme.recipient.RecipientManager;
import apis.pagarme.refunds.Refund;
import apis.pagarme.refunds.RefundsManager;
import apis.pagarme.subscriptions.SFilter;
import apis.pagarme.subscriptions.SignatureManager;
import apis.pagarme.system.SystemConfigurations;
import apis.pagarme.transaction.TransactionsManager;
import apis.pagarme.transfer.TTransfer;
import apis.pagarme.transfer.TransferManager;
import apis.pagarme.userbuyer.BAccountBank;
import apis.pagarme.userbuyer.BCard;
import apis.pagarme.userbuyer.BDocument;
import apis.pagarme.userbuyer.UserBuyer;
import br.com.comprazencasa.database.ConnectionDB_APP;
import br.com.comprazencasa.database.ConnectionDB_Collaborators;
import com.google.gson.Gson;
import me.pagar.model.*;
import org.apache.commons.lang.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

public class DaoPagarme {

    private static final String API_KEY = "ak_live_ZtHfMPxyiUh4uFW1HIAcUPPt1c2ZM6";
    private static final String API_KEY_TESTE = "ak_test_6EZk6wzeQ80xgWagq218CTr5rvOYDB";
    private static final APIPagarme apiPagarme = new APIPagarme(API_KEY);

    /*
     * METHODS
     */

    public DaoPagarme(){}

    public Transactions getTransactions() {
        return new Transactions();
    }

    public Cards getCards() {
        return new Cards();
    }

    public Customer getCustomer() {
        return new Customer();
    }

    public AccountBank getBankAccount() {
        return new AccountBank();
    }

    public Anticipation getAnticipation() {
        return new Anticipation();
    }

    public Balance getBalance() {
        return new Balance();
    }

    public Antifraud getAntifraud() {
        return new Antifraud();
    }

    public DivisionRule getDivisionRule() {
        return new DivisionRule();
    }

    public Plans getPlans() {
        return new Plans() ;
    }

    public Receivable getReceivable() {
        return new Receivable();
    }

    public Recipient getRecipient() {
        return new Recipient();
    }

    public Refunds getRefunds() {
        return new Refunds();
    }

    public Transfers getTransfers() {
        return new Transfers();
    }

    public Signatures getSignatures() {
        return new Signatures();
    }

    private String decrypFullJson(String code) {
        try {
            String crypto = new Gson().fromJson(code, String.class);
            return Cryptograph.decrypt(crypto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
     * CLASSES
     */

    public static class Cards {

        private CardManager cardManager = apiPagarme.getCardManager();

        public boolean createCard(String cardJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(cardJsonCrypted, String.class);

                BCard card = new Gson().fromJson(Cryptograph.decrypt(crypto), BCard.class);

                Card resultCard = cardManager.createCard(card.getIdCustomer(), card.getNumber(), card.getHolderName(), card.getExpirationDate(), card.getCVV());
                return (resultCard != null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;//return cardManager.createCard();
        }

        public boolean createCard(int idCustomer, String cardHash) {
            try {
                Card resultCard = cardManager.createCard(idCustomer, cardHash);
                return (resultCard != null);
            } catch (Exception e) {
                if(e != null)
                System.err.println("ERROR: "+e.getMessage()+"\n"+e.getLocalizedMessage());
            }
            return false;//return cardManager.createCard();
        }

        public BCard getCard(String idCardJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(idCardJsonCrypted, String.class);

                String json = Cryptograph.decrypt(crypto);
                String idCard = new Gson().fromJson(json, String.class);

                Card card = cardManager.getCard(String.valueOf(idCard));

                BCard c = new BCard();
                c.setId(card.getId());
                c.setIdCustomer(card.getCustomer().getId());
                c.setBrand(card.getBrand().name());
                c.setHolderName(card.getHolderName());
                c.setNumber(card.getLastDigits());
                return c;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public ArrayList<BCard> getAllCard(String idUserJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(idUserJsonCrypted, String.class);

                String json = Cryptograph.decrypt(crypto);
                int idUser = new Gson().fromJson(json, Integer.class);

                ArrayList<BCard> cards = cardManager.getAllCards(idUser);

                return cards;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public ArrayList<BCard> getAllCard(int idCustomer) {
            try {
                ArrayList<BCard> cards = cardManager.getAllCards(idCustomer);

                return cards;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private boolean saveCard(int idUser, Card card) {
            Connection con = ConnectionDB_APP.connect();
            PreparedStatement pst = null;

            if (con != null) {

                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO one_click_buy (id_user, card_id, card_number, card_brand) ");
                sql.append("VALUES (?,?,?,?); ");

                try {
                    pst = con.prepareStatement(sql.toString());
                    pst.setInt(1, idUser);
                    pst.setString(2, card.getId());
                    pst.setString(3, card.getNumber());
                    pst.setString(4, card.getBrand().name());
                    int lineAffected = pst.executeUpdate();

                    con.close();
                    pst.close();

                    return lineAffected > 0 ? true : false;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }

        public String getPublicKeyToEncrypt(){
            return cardManager.getPublicKeyToCardHash();
        }
    }

    public class Transactions {
        private TransactionsManager transactionsManager = apiPagarme.getTransactionsManager();

        public Transaction createTransaction(UserBuyer buyer) {

            try {
                Transaction tr = transactionsManager.createTransaction(buyer);
                return tr;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Transaction createSplitTransaction(UserBuyer buyer, SystemConfigurations configs) {

            try {
                Transaction tr = transactionsManager.createSplitTransaction(buyer, configs);
                return tr;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Transaction createTransactionCapture(UserBuyer buyer) {

            try {
               /* buyer.getCard().setCardHash(buyer.getCard().getCardHash().replaceAll("\\s","")
                        .replaceAll("\t","")
                        .replaceAll("\n",""));*/
                Transaction tr = transactionsManager.createTransactionCapture(buyer);
                return tr;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Transaction createTransactionCaptureSplit(int idTransaction, int totalValue, String idRecipient, int splitValue) {

            try {

                String idRecipientMaster = "re_cka0useo70iqwt56d6n9w48h8";
                Transaction tr = transactionsManager.captureTransactionSplit(idTransaction, totalValue,idRecipientMaster, idRecipient, splitValue);
                return tr;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Transaction.Status chargebackTotalTransaction(String idTransJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(idTransJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                int idTransaction = new Gson().fromJson(json, Integer.class);

                Transaction tr = transactionsManager.chargebackTotalTransaction(idTransaction);
                //getCards().saveCard(userB.getId(), tr.getCard());
                return tr.getStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public Transaction.Status chargebackTotalTransaction(int idTransaction) {
            try {

                Transaction tr = transactionsManager.chargebackTotalTransaction(idTransaction);
                //getCards().saveCard(userB.getId(), tr.getCard());
                return tr.getStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public Transaction captureTransaction(UserBuyer buyer) {

            try {
                Transaction tr = transactionsManager.createTransaction(buyer);
                return tr;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Customer {

        private CustomerManager customerManager = apiPagarme.getCustomerManager();

        public me.pagar.model.Customer createCustomer(String uberBuyerJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(uberBuyerJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                UserBuyer userB = new Gson().fromJson(json, UserBuyer.class);

                return customerManager.createCustomer(userB);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public me.pagar.model.Customer createCustomer(UserBuyer buyer) {
            return customerManager.createCustomer(buyer);
        }

        public me.pagar.model.Customer getCustomer(int idCustomer) {
            try {
                return customerManager.getCustomer(idCustomer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class AccountBank {

        private AccountBankManager abm = apiPagarme.getAccountBankManager();

        public BankAccount createBankAccount(BAccountBank bankAccount) {
            try {
                return abm.createAccountBank(bankAccount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public ArrayList<BAccountBank> getBanksAccount(String idCollJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(idCollJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                int idColl = new Gson().fromJson(json, Integer.class);

                ArrayList<BankAccount> bankAccounts = abm.getBankAccounts(getAllIDSBankAccounts(idColl));

                ArrayList<BAccountBank> responseAccount = new ArrayList<>();

                BAccountBank account;
                for (BankAccount b : bankAccounts) {
                    account = new BAccountBank();
                    account.setId(b.getId());
                    account.setAgency(b.getAgencia());
                    account.setAgencyDv(b.getAgenciaDv());
                    account.setBankCode(b.getBankCode());

                    BDocument doc = new BDocument();
                    if (b.getDocumentType() == BankAccount.DocumentType.CPF) {
                        doc.setType(BDocument.Type.CPF);
                        doc.setCPF(b.getDocumentNumber());
                    } else {
                        doc.setType(BDocument.Type.CNPJ);
                        doc.setCPF(b.getDocumentNumber());
                    }

                    account.setDocument(doc);
                    account.setLegalName(b.getLegalName());
                    responseAccount.add(account);
                }

                return responseAccount;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public ArrayList<Integer> getAllIDSBankAccounts(int idColl) {
            Connection con = ConnectionDB_Collaborators.connect();
            PreparedStatement pst = null;

            if (con != null) {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT id_bank_account FROM coll_bank_account WHERE id_coll=? ");

                ArrayList<Integer> idsBanksAccount = new ArrayList<>();
                try {
                    pst = con.prepareStatement(sql.toString());
                    pst.setInt(1, idColl);

                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        idsBanksAccount.add(rs.getInt("id_bank_account"));
                    }

                    con.close();
                    pst.close();

                    return idsBanksAccount;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        con.close();
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

    }

    public class Anticipation {

        private AnticipationManager am = apiPagarme.getAnticipationManager();

        public boolean createAnticipation(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                CAnticipation anticipation = new Gson().fromJson(json, CAnticipation.class);

                BulkAnticipation an = am.createAnticipation(anticipation);
                //an.
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public BulkAnticipation cancelAnticipation(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                CAnticipation anticipation = new Gson().fromJson(json, CAnticipation.class);

                BulkAnticipation rec = am.cancelAnticipation(anticipation);
                return rec;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public me.pagar.model.Recipient deleteAnticipation(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                CAnticipation anticipation = new Gson().fromJson(json, CAnticipation.class);

                me.pagar.model.Recipient rec = am.deleteAnticipationBuild(anticipation);
                return rec;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public BulkAnticipation confirmAnticipation(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                CAnticipation anticipation = new Gson().fromJson(json, CAnticipation.class);

                BulkAnticipation rec = am.confirmAnticipationBuild(anticipation);
                return rec;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public me.pagar.model.Recipient getLimitMinimumMaxAnticipation(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                CAnticipation anticipation = new Gson().fromJson(json, CAnticipation.class);

                me.pagar.model.Recipient rec = am.getLimitiAnticipation(anticipation);
                return rec;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public class Balance {
        private BalanceManager balm = apiPagarme.getBalanceManager();

        public me.pagar.model.Balance getBalanceAccount(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                String idRecipient = new Gson().fromJson(json, String.class);

                me.pagar.model.Balance bal = balm.getBalanceAccount(idRecipient);
                return bal;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void getHistoricOperation(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                String idRecipient = new Gson().fromJson(json, String.class);

                //Balance bal = balm.getHistoricOperation(idRecipient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Antifraud {
        private AntifraudManager anti = apiPagarme.getAntifraudManager();

        public Collection<AntifraudAnalysis> getBalanceAccount(String antJsonCrypted) {
            try {
                String crypto = new Gson().fromJson(antJsonCrypted, String.class);
                String json = Cryptograph.decrypt(crypto);

                int idTransaction = new Gson().fromJson(json, Integer.class);

                Collection<AntifraudAnalysis> antis = anti.returnAnaliseAntifraud(idTransaction);
                return antis;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class DivisionRule {
        private DivisionRuleManager div = apiPagarme.getDivisionRuleManager();

        public SplitRule getDivisionRule(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                DDivisionRule divRule = new Gson().fromJson(json, DDivisionRule.class);

                SplitRule split = null;
                split = div.getDivisionRule(divRule);

                return split;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Collection<SplitRule> getSplitRule(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                int idTransaction = new Gson().fromJson(json, Integer.class);

                Collection<SplitRule> splits = div.getSplitRules(idTransaction);

                return splits;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Plans {
        private PlansManager div = apiPagarme.getPlansManager();

        public Plan createPlan(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                PPlan plan = new Gson().fromJson(json, PPlan.class);

                SplitRule split = null;
                Plan p = div.createPlan(plan);

                return p;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Plan createPlanTrialDays(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                PPlan p = new Gson().fromJson(json, PPlan.class);

                Plan plan = div.createPlanTrialDays(p);

                return plan;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Plan updatePlan(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                PPlan p = new Gson().fromJson(json, PPlan.class);

                Plan plan = div.updatePlan(p);

                return plan;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Plan createPlanInvoiceReminder(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                PPlan p = new Gson().fromJson(json, PPlan.class);

                Plan plan = div.createPlanInvoiceReminder(p);

                return plan;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Plan createPlanTrialDaysInvoiceReminder(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                PPlan p = new Gson().fromJson(json, PPlan.class);

                Plan plan = div.createPlanTrialDaysInvociceReminder(p);

                return plan;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Plan getPlan(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                PPlan p = new Gson().fromJson(json, PPlan.class);

                Plan plan = div.getPlan(p);

                return plan;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Collection<Plan> getPlans(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                PPlan p = new Gson().fromJson(json, PPlan.class);

                Collection<Plan> plans = div.getPlans(p);

                return plans;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public class Receivable {
        private ReceivableManager rec = apiPagarme.getReceivableManager();

        public Payable getReceivable(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                int idPayable = new Gson().fromJson(json, Integer.class);

                Payable p = rec.getReceivable(idPayable);

                return p;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Collection<Payable> getReceivables(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                RReceivable r = new Gson().fromJson(json, RReceivable.class);

                Collection<Payable> ps = rec.getReceivables(r);

                return ps;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class Recipient {
        private RecipientManager rec = apiPagarme.getRecipientManager();


        public me.pagar.model.Recipient createRecipient(RRecipient newRecipient, BAccountBank bAccountBank) {
            try {

                BankAccount account = new BankAccount();
                account.setAgencia(bAccountBank.getAgency());
                account.setAgenciaDv(bAccountBank.getAgencyDv());
                account.setConta(bAccountBank.getAccount());
                account.setContaDv(bAccountBank.getAccountDv());
                account.setBankCode(bAccountBank.getBankCode());
                account.setDocumentNumber(bAccountBank.getDocument().get());
                account.setLegalName(bAccountBank.getLegalName());
                account.setType(AccountBankManager.decodeBankType(bAccountBank.getType()));

                me.pagar.model.Recipient dr = rec.createRecipient(newRecipient, account);

                return dr;
            } catch (PagarMeException e) {
                e.printStackTrace();
                return null ;
            }
        }



        public me.pagar.model.Recipient updateRecipient(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                RRecipient r = new Gson().fromJson(json, RRecipient.class);

                me.pagar.model.Recipient dr = rec.updateRecipient(r);

                return dr;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public me.pagar.model.Recipient getRecipient(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                RRecipient r = new Gson().fromJson(json, RRecipient.class);

                me.pagar.model.Recipient dr = rec.getRecipient(r.getIdRecipient());

                return dr;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Collection<me.pagar.model.Recipient> getRecipients(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                RRecipient r = new Gson().fromJson(json, RRecipient.class);

                Collection<me.pagar.model.Recipient> recps = rec.getRecipients(r.getCountPage(), r.getPage());

                return recps;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public me.pagar.model.Balance getBalanceRecipient(String idRecipient) {
            try {

                me.pagar.model.Balance bal = rec.getBalanceRecipient(idRecipient);

                return bal;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }
            return null;
        }

        public ObjectUtils.Null getOneMovimentation(String antJsonCrypted) {

            return null;
        }


    }

    public class Refunds {
        private RefundsManager ref = apiPagarme.getRefundsManager();

        public ArrayList<Refund> getRefunds(String antJsonCrypted) {
            String json = decrypFullJson(antJsonCrypted);

            Refund refund = new Gson().fromJson(json, Refund.class);

            ArrayList<Refund> dr = ref.getRefunds(Integer.valueOf(refund.getTransactionID()), refund.getDateCreated(), refund.getDateCreated());

            return dr;
        }
    }

    public class Transfers {

        private TransferManager trans = apiPagarme.getTransferManager();

        public Transfer createTransfer(String idRecipient, int amount) {
            try {

                Transfer tr = trans.createTransfer(
                        idRecipient,
                        amount);

                return tr ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

        public Transfer getTransfer(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                String idTransfer = new Gson().fromJson(json, String.class);

                Transfer tr = trans.getTransfer(idTransfer);

                return tr ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

        public Collection<Transfer> getTransfers(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                TTransfer t = new Gson().fromJson(json, TTransfer.class);

                Collection<Transfer> tr = trans.getAllTransfer(t.getCountPage(),t.getPage());

                return tr ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

        public Transfer cancelTransfer(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                TTransfer t = new Gson().fromJson(json, TTransfer.class);

                Transfer tr = trans.cancelTransfer(t.getIdTransfer());

                return tr ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }
    }

    public class Signatures {

        private SignatureManager signs = apiPagarme.getSignatureManager();

        public Subscription createSignature(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                UserBuyer userBuyer = new Gson().fromJson(json, UserBuyer.class);

                Subscription subs = signs.createSignature(userBuyer);

                return subs ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

        public Subscription cancelSignature(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                String idSubscription = new Gson().fromJson(json, String.class);

                Subscription subs = signs.cancelSignature(idSubscription);

                return subs ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

        public Subscription getSignature(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);

                String idSubscription = new Gson().fromJson(json, String.class);

                Subscription subs = signs.getSignature(idSubscription);

                return subs ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

        public Collection<Subscription> getSignatures(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);


                SFilter filter = new Gson().fromJson(json, SFilter.class);


                Collection<Subscription> subs = signs.getSubscriptions(filter);

                return subs ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

        public Collection<Transaction> getAllTransactionsFromSignature(String antJsonCrypted) {
            try {
                String json = decrypFullJson(antJsonCrypted);


                String idSignature = new Gson().fromJson(json, String.class);


                Collection<Transaction> subs = signs.getAllTransactionsFromSignature(idSignature);

                return subs ;
            } catch (PagarMeException e) {
                e.printStackTrace();
            }

            return null ;
        }

    }
}
