package apis.pagarme.anticipation;

import me.pagar.RecipientStatus;
import me.pagar.model.BulkAnticipation;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Recipient;
import org.joda.time.DateTime;

import java.util.Collection;

public class AnticipationManager {
    private final String API_KEY;
    private final String URL_ROOT = "https://api.pagar.me/1/recipients/recipient_id/bulk_anticipations";

    public AnticipationManager(String API_KEY){
        this.API_KEY = API_KEY ;
    }

    public BulkAnticipation createAnticipation(CAnticipation ant) throws PagarMeException {
        PagarMe.init(API_KEY);

        Recipient recipient = new Recipient().find(ant.getIdRecipient());
        DateTime paymentDate = new DateTime().plusDays(ant.getPlusDays());

        BulkAnticipation anticipation = new BulkAnticipation();
        anticipation.setRequiredParametersForCreation(
                paymentDate,
                ant.getTimeFrame()==CAnticipation.TimeFrameType.START? BulkAnticipation.Timeframe.START: BulkAnticipation.Timeframe.END,
                ant.getRequestAmount(),
                ant.isBuild()
        );
        anticipation = recipient.anticipate(anticipation);
        return anticipation;
    }

    public Recipient getLimitiAnticipation(CAnticipation ant) throws PagarMeException {
        PagarMe.init(API_KEY);

        Recipient recipient = new Recipient().find(ant.getIdRecipient());
        DateTime paymentDate = new DateTime().plusDays(ant.getPlusDays());
        recipient.getMaxAnticipationLimit(paymentDate, ant.getTimeFrame()==CAnticipation.TimeFrameType.START? BulkAnticipation.Timeframe.START: BulkAnticipation.Timeframe.END);
        recipient.getMinAnticipationLimit(paymentDate, ant.getTimeFrame()==CAnticipation.TimeFrameType.START? BulkAnticipation.Timeframe.START: BulkAnticipation.Timeframe.END);

        return recipient;
    }

    public BulkAnticipation confirmAnticipationBuild(CAnticipation ant) throws PagarMeException {
        PagarMe.init(API_KEY);

        Recipient recipient = new Recipient().find(ant.getIdRecipient());

        BulkAnticipation anticipiation = (BulkAnticipation) recipient.findAnticipations(ant.getCountPage(), ant.getPage()).toArray()[0];
        BulkAnticipation cancelledAnticipation = recipient.confirmBulkAnticipation(anticipiation);
        return cancelledAnticipation ;
    }

    public BulkAnticipation cancelAnticipation(CAnticipation ant) throws PagarMeException {
        PagarMe.init(API_KEY);
        Recipient recipient = new Recipient().find(ant.getIdRecipient());

        BulkAnticipation anticipiation = (BulkAnticipation) recipient.findAnticipations(ant.getCountPage(), ant.getPage()).toArray()[0];

        BulkAnticipation cancelledAnticipation = recipient.cancelAnticipation(anticipiation);

        return cancelledAnticipation;
    }

    public Recipient deleteAnticipationBuild(CAnticipation ant) throws PagarMeException {
        PagarMe.init(API_KEY);
        Recipient recipient = new Recipient().find(ant.getIdRecipient());

        BulkAnticipation anticipation = new BulkAnticipation();
        anticipation.setRequiredParametersForCreation(
                new DateTime().plusDays(ant.getPlusDays()),
                ant.getTimeFrame()==CAnticipation.TimeFrameType.START? BulkAnticipation.Timeframe.START: BulkAnticipation.Timeframe.END,
                ant.getRequestAmount(),
                ant.isBuild()
        );

        BulkAnticipation createdAnticipation = recipient.anticipate(anticipation);

        recipient.deleteAnticipation(createdAnticipation);
        return recipient;
    }

    public Collection<BulkAnticipation> getAnticipations(CAnticipation ant) throws PagarMeException {
        PagarMe.init(API_KEY);

        Recipient recipient = new Recipient().find(ant.getIdRecipient());
        return recipient.findAnticipations(ant.getCountPage(), ant.getPage());
    }
}
