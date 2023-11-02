package br.com.comprazencasa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataPadraoUtil {
    public static synchronized String formataData(String data, String formatIn, String formatOut) {
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

    public static String getCurrentDate(String format){
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Date hour = calendar.getTime();

        SimpleDateFormat fm = new SimpleDateFormat(format);

        return fm.format(hour);
    }

    public static String getDateMoreDayADD(int day){
        Date dataDoUsuario = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(dataDoUsuario);
        c.add(Calendar.DATE, +7);

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

    public static int getDayFromDate(String dataMarcada, String s) {
        String[] data = dataMarcada.split(s);
        return Integer.valueOf(data[0]);
    }

    public static int getYearFromDate(String dataMarcada, String s) {
        String[] data = dataMarcada.split(s);
        return Integer.valueOf(data[2]);
    }

    public static int getMonthFromDate(String dataMarcada, String s) {
        String[] data = dataMarcada.split(s);
        return Integer.valueOf(data[1]);
    }

    public static OffsetDateTime getDateTime(String format, String date){
        DateTimeFormatter openingDate = DateTimeFormatter.ofPattern(format);
        return  LocalDateTime.parse(date, openingDate)
                .atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime getDateTimeDefault(){

        Calendar calendar = GregorianCalendar.getInstance();

        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        fmt.setCalendar(calendar);

        DateTimeFormatter openingDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return  LocalDateTime.parse(fmt.format(calendar.getTime()), openingDate)
                .atOffset(ZoneOffset.UTC);
    }

    public static int getDayOfWeek(){
        Calendar calendar = GregorianCalendar.getInstance();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setCalendar(calendar);

        DateTimeFormatter openingDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        OffsetDateTime dateTime = LocalDateTime.parse(fmt.format(calendar.getTime()), openingDate)
                .atOffset(ZoneOffset.UTC);

        return dateTime.getDayOfWeek().getValue();
    }

    public static boolean dateCompare(String format, String dateIn1, String dateIn2) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat(format);

        Date date1 = formater.parse(dateIn1);
        Date date2 = formater.parse(dateIn2);

        int result = date1.compareTo(date2);
        return result==0?true:false;
    }

    public static boolean dateCompare(String formatIn, Date date1, Date date2){
        int result = date1.compareTo(date2);
        return result==0?true:false;
    }

    public static int convertToDayNameToNumber(String nameDayOfWeek){
        switch (nameDayOfWeek.toLowerCase()){
            case "sunday": return 1;
            case "monday ": return 2;
            case "tuesday ": return 3;
            case "wednesday": return 4;
            case "thursday ": return 5;
            case "friday ": return 6;
            case "saturday ": return 7;
            default:return 0 ;
        }
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