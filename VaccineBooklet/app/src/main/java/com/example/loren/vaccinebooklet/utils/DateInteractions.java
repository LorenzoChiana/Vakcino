package com.example.loren.vaccinebooklet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateInteractions {

    public static String translateDate(String birthDate, int da) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(birthDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, da);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        return sdf1.format(c.getTime());
    }

    public static String changeDateFormat(String dateIn, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateIn);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean isLateThan(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        Date today = new Date();
        String currentDateString = sdf.format(today);
        try {
            strDate = sdf.parse(date);
            today = sdf.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today.after(strDate);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(date);
    }

    public static int getYear(String dateIn) {
        try {
            Date date = stringToDate(dateIn);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            return Integer.parseInt(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getMonth(String dateIn) {
        try {
            Date date = stringToDate(dateIn);
            SimpleDateFormat df = new SimpleDateFormat("MM");
            return Integer.parseInt(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String convertToDate(String dateString){
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date originDate;
        String returnDate = "";
        try {
            originDate = dateFormat1.parse(dateString);
            returnDate = dateFormat2.format(originDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static int getDay(String dateIn) {
        try {
            Date date = stringToDate(dateIn);
            SimpleDateFormat df = new SimpleDateFormat("dd");
            return Integer.parseInt(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
