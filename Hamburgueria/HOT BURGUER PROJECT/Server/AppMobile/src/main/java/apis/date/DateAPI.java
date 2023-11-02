package apis.date;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateAPI {

    private static final DateManager dateManager = new DateManager();
    private static final HoursManager hoursManager = new HoursManager() ;

    public static DateManager getDateManager() {
        return dateManager;
    }

    public static HoursManager getHoursManager() {
        return hoursManager;
    }

}
