package com.privatepe.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateFormatter {

    private static String inputdateFormat = "yyyy/MM/dd";
    private static String outputdateFormat = "dd MMM yyyy";


    private static final DateFormatter ourInstance = new DateFormatter();

    public static DateFormatter getInstance() {
        return ourInstance;
    }

    public DateFormatter() {
    }


    public String format(String date) {
        DateFormat sdfIn = new SimpleDateFormat(inputdateFormat, Locale.US);
        DateFormat sdfOut = new SimpleDateFormat(outputdateFormat, Locale.US);

        Date dateIn = null;
        try {
            dateIn = sdfIn.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfOut.format(dateIn);
    }

    public String formatDDMMM(String date) {
        DateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat sdfOut = new SimpleDateFormat("dd-MMM", Locale.US);

        Date dateIn = null;
        try {
            dateIn = sdfIn.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfOut.format(dateIn);
    }

    public String formatDateTime(String date) {
        DateFormat sdfOut = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US);
        String inFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateFormat sdfIn = new SimpleDateFormat(inFormat, Locale.US);
        sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date dateIn = null;
        try {
            dateIn = sdfIn.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfOut.format(dateIn);
    }

    public String formatDate(String date) {
        DateFormat sdfOut = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String inFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateFormat sdfIn = new SimpleDateFormat(inFormat, Locale.US);
        sdfIn.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date dateIn = null;
        try {
            dateIn = sdfIn.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdfOut.format(dateIn);
    }

    public String getDayFromDate(String date) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat inDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm a",Locale.US);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE, HH:mm a"); //SimpleDateFormat sdf3 = new SimpleDateFormat("EEE, MMM d, ''yy");

        Date dateIn = null;
        try {
            dateIn = inDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outFormat.format(dateIn);

    }
    public String calculateTime(long seconds) {
        String timeInSecond = "";
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);

        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                (TimeUnit.SECONDS.toHours(seconds)* 60);

        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                (TimeUnit.SECONDS.toMinutes(seconds) *60);

        System.out.println("Day " + day + " Hour " + hours + " Minute " + minute + " Seconds " + second);
        if(second > 0 && minute ==0 && hours ==0){
            timeInSecond =  second+"s"  ;
        }else if(second > 0 && minute >0 && hours ==0){
            timeInSecond =  minute + "m " +  second+"s" ;
        }else if(second > 0 && minute >0 && hours >0){
            timeInSecond =  hours+"h "+minute + "m " +  second+"s" ;
        } else{
            timeInSecond =  hours+"h "+minute + "m " +  second+"s" ;
        }
        
        return timeInSecond;
    }
}
