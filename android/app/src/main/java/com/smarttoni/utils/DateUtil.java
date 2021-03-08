package com.smarttoni.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {


    public static String SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String STANDARD_DATE_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";

    public static String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";


    public static String ORDER_DATE_FORMAT = "dd-MM-yyyy HH:mm";


    public static String formatDate(long date,String format) {
        return formatDate(new Date((date)), format);
    }

    public static String formatDate(long date) {
        return formatDate(date,SERVER_DATE_FORMAT);
    }

    public static String formatStandardDate(Date date) {
        return formatDate(date, STANDARD_DATE_FORMAT_2);
    }

    public static String formatDate(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static long parse(String date) {
        DateFormat df = new SimpleDateFormat(STANDARD_DATE_FORMAT);
        try {
            if (date == null || date.equals("")) {
                throw new ParseException("Null Value", 0);
            }
            return df.parse(date).getTime();
        } catch (ParseException e) {
            return new Date().getTime();
        }
    }
}
