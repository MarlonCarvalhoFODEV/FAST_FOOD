package apis.date;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class HoursManager {

    private int minuto;

    public HoursManager() {
        this.minuto = 0;
    }

    public HoursManager(String horas) {
        String[] hrs = horas.split(":");
        this.minuto = (Integer.parseInt(hrs[0]) * 60)
                + (Integer.parseInt(hrs[1]));
    }

    public static String addMinuteNowHour(int sumMinute){
        Calendar calendar = GregorianCalendar.getInstance(Locale.getDefault());
        calendar.setTime(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        calendar.add(Calendar.MINUTE,sumMinute);
        return sdf.format(calendar.getTime());
    }

    public static String getCurrentTime() {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Date hour = calendar.getTime();

        SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        return fm.format(hour);
    }

    public void setHours(String horas) {
        String[] hrs = horas.split(":");
        this.minuto = (Integer.parseInt(hrs[0]) * 60)
                + (Integer.parseInt(hrs[1]));
    }

    public void clear() {
        this.minuto = 0;
    }

    public synchronized void add(String horas) {
        String[] hrs = horas.split(":");
        minuto += (Integer.parseInt(hrs[0]) * 60) + (Integer.parseInt(hrs[1]));
    }

    public void subtract(String horas) {
        String[] hrs = horas.split(":");
        minuto -= (Integer.parseInt(hrs[0]) * 60) + (Integer.parseInt(hrs[1]));
    }

    public String getHours() {
        String horas;
        DecimalFormat fmt = new DecimalFormat("00");
        horas = Integer.toString(this.minuto / 60);
        horas += ":";
        horas += fmt.format(this.minuto % 60);
        return horas;
    }

    public static boolean isAfter(String timeActual, String openingOrClosedHour) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date present = parser.parse(timeActual);
            Date closed = parser.parse(openingOrClosedHour);
            if (present.after(closed)) {
                return true;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return false;
    }

    public static boolean isBefore(String timeActual, String openingOrClosedHour) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date present = parser.parse(timeActual);
            Date closed = parser.parse(openingOrClosedHour);
            if (present.before(closed)) {
                return true;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return false;
    }

    public static boolean equals(String timeActual, String openingOrClosedHour) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date present = parser.parse(timeActual);
            Date closed = parser.parse(openingOrClosedHour);
            if (present.equals(closed)) {
                return true;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return false;
    }

}
