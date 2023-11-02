package apis.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateAPI {
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

    public static String getDateMoreDayADD(int day){
        Date dataDoUsuario = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(dataDoUsuario);
        c.add(Calendar.DATE, +day);

        dataDoUsuario = c.getTime();

        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataDoUsuario);
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

    public class CzcDate{
        private String dateNow, dateFinal ;
        private int day, month, year, hours ;

        public String getDateNow() {
            return dateNow;
        }

        public void setDateNow(String dateNow) {
            this.dateNow = dateNow;
        }

        public String getDateFinal() {
            return dateFinal;
        }

        public void setDateFinal(String dateFinal) {
            this.dateFinal = dateFinal;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }
    }
}
