package apis.pagarme.plans;

import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Plan;

import java.util.Collection;

public class PlansManager {

    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/plans/";

    public PlansManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public Plan createPlan(PPlan p) throws PagarMeException {
        PagarMe.init(API_KEY);

        Plan plan = new Plan();
        plan.setAmount(p.getAmount());
        plan.setName(p.getName());
        plan.setDays(p.getDays());
        return plan.save();
    }

    public Plan createPlanInvoiceReminder(PPlan p) throws PagarMeException {
        PagarMe.init(API_KEY);

        Plan plan = new Plan();
        plan.setAmount(p.getAmount());
        plan.setName(p.getName());
        plan.setDays(p.getDays());
        plan.setInvoiceReminder(p.getInvoiceReminder());
        return plan.save();
    }

    public Plan createPlanTrialDays(PPlan p) throws PagarMeException {
        PagarMe.init(API_KEY);

        Plan plan = new Plan();
        plan.setTrialDays(p.getTrialDays());
        plan.setAmount(p.getAmount());
        plan.setName(p.getName());
        plan.setDays(p.getDays());
        return plan.save();
    }

    public Plan createPlanTrialDaysInvociceReminder(PPlan p) throws PagarMeException {
        PagarMe.init(API_KEY);

        Plan plan = new Plan();
        plan.setTrialDays(p.getTrialDays());
        plan.setAmount(p.getAmount());
        plan.setName(p.getName());
        plan.setDays(p.getDays());
        plan.setInvoiceReminder(p.getInvoiceReminder());
        return plan.save();
    }

    public Collection<Plan> getPlans(PPlan p) throws PagarMeException {
        PagarMe.init(API_KEY);

        return new Plan().findCollection(p.getTotalPage(), p.getPage());
    }

    public Plan getPlan(PPlan p) throws PagarMeException {
        PagarMe.init(API_KEY);
        return new Plan().find(p.getIdPlan());
    }

    public Plan updatePlan(PPlan updatedPlan) throws PagarMeException {
        PagarMe.init(API_KEY);
        Plan planF = new Plan();
        planF.setName(updatedPlan.getName());
        planF.setTrialDays(updatedPlan.getTrialDays());
        planF.setId(updatedPlan.getIdPlan());
        return planF.save();
    }
}
