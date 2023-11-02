package apis.pagarme.accountbank;

import apis.pagarme.userbuyer.BAccountBank;
import ch.qos.logback.core.util.COWArrayList;
import me.pagar.BankAccountType;
import me.pagar.model.BankAccount;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class AccountBankManager {
    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/bank_accounts";

    public AccountBankManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public BankAccount createAccountBank(BAccountBank ac) throws PagarMeException {
        PagarMe.init(API_KEY);

        BankAccount account = new BankAccount();
        account.setAgencia(ac.getAgency());
        account.setAgenciaDv(ac.getAgencyDv());
        account.setConta(ac.getAccount());
        account.setContaDv(ac.getAccountDv());
        account.setBankCode(ac.getBankCode());
        account.setDocumentNumber(ac.getDocument().get());
        account.setLegalName(ac.getLegalName());
        account.setType(decodeBankType(ac.getType()));
        return account.save() ;
    }

    public synchronized BankAccount getBankAccount(int idBankAccount) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new BankAccount().find(idBankAccount);
    }

    public Collection<BankAccount> getBankAccounts(int countPage, int page) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new BankAccount().findCollection(countPage, page);
    }

    public ArrayList<BankAccount> getBankAccounts(ArrayList<Integer> idsBankAccount) throws PagarMeException {
        PagarMe.init(API_KEY);

        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        for(Integer id : idsBankAccount){
            BankAccount account = getBankAccount(id);

            bankAccounts.add(account);
        }

        return bankAccounts ;
    }

    public static final BankAccountType decodeBankType(int type){
        switch (type){
            case BAccountBank.Type.CURRENT: return BankAccountType.CORRENTE ;
            case BAccountBank.Type.CURRENT_JOINT: return BankAccountType.CORRENTE_CONJUNTA ;
            case BAccountBank.Type.SAVINGS: return BankAccountType.POUPANCA ;
            case BAccountBank.Type.SAVINGS_JOINT: return BankAccountType.POUPANCA_CONJUNTA ;
        }
        return null ;
    }
}
