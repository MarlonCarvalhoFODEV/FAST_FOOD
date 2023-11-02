package apis.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateManager {

    public static String format(String formatIn, String formatOut, String dateData) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat(formatIn);
        Date date = parser.parse(dateData);
        SimpleDateFormat formatter = new SimpleDateFormat(formatOut);
        return formatter.format(date);
    }

    public static synchronized String formatDate(String data, String formatIn, String formatOut) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat(formatIn);
        SimpleDateFormat formatoSaidaDiaMesAno = new SimpleDateFormat(formatOut);

        Date dataTypeDate = null;
        try {
            if(data != null)dataTypeDate = formatoEntrada.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dataTypeDate!=null?formatoSaidaDiaMesAno.format(dataTypeDate):null;
    }

    public static Date addTimeFromDate(Date date, int hours, int minutes, int seconds){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, hours);
        cal.add(Calendar.MINUTE, minutes);
        cal.add(Calendar.SECOND, seconds);// adds one hour
        return cal.getTime();
    }

    public static String getDateStringMoreDayADD(int day){
        Date dataDoUsuario = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(dataDoUsuario);
        c.add(Calendar.DATE, +day);

        dataDoUsuario = c.getTime();

        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataDoUsuario);
    }

    public static Date getDateMoreDayADD(int day){
        Date dataDoUsuario = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(dataDoUsuario);
        c.add(Calendar.DATE, +day);

        dataDoUsuario = c.getTime();
        return dataDoUsuario;
    }

    public static int getCountDownDate(String dateNow, String otherDate,String formatIn){
        if(otherDate != null && dateNow != null){
            try {
                DateFormat df = new SimpleDateFormat(formatIn);
                df.setLenient(false);
                Date d1 = null;
                d1 = df.parse (dateNow);
                Date d2 = df.parse (otherDate);
                long dt = (d2.getTime() - d1.getTime()) + 3600000; // 1 hora para compensar horário de verão
                return (int) (dt / 86400000L); // passaram-se 67111 dias;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static String getCurrentDate(String format) {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Date hour = calendar.getTime();

        SimpleDateFormat fm = new SimpleDateFormat(format);

        return fm.format(hour);
    }

    public static String getCurrentDate(String format, int addDay) {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + addDay);

        Date hour = calendar.getTime();
        SimpleDateFormat fm = new SimpleDateFormat(format);

        return fm.format(hour);
    }

    public static boolean beforeDateTime(Date date, Date otherDate){
        if (date.before(otherDate)) {
            return true;
        }
        return false;
    }

    public static boolean equalsDateTime(Date date, Date otherDate) {
        if (date.equals(otherDate)) {
            return true;
        }
        return false;
    }

    public static boolean afterDateTime(Date date, Date otherDate){
        if (date.after(otherDate)) {
            return true;
        }
        return false;
    }
}
