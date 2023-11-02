package apis.pagarme.receivable;

import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Payable;
import me.pagar.model.filter.PayableQueriableFields;
import org.joda.time.DateTime;

import java.util.Collection;

public class ReceivableManager {
    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/payables/";

    public ReceivableManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public Collection<Payable> getReceivables(RReceivable r) throws PagarMeException {
        PagarMe.init(API_KEY);

        PayableQueriableFields payableFilter = new PayableQueriableFields();
        payableFilter.createdBefore(new DateTime());
         return new Payable().findCollection(r.getTotalPage(), r.getPage(), payableFilter);
    }

    public Payable getReceivable(int idPayable) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new Payable().find(idPayable);
    }
}
