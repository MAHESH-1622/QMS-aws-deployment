package com.sm.qms.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils2 {

    private static final String DATE_FORMAT = "ddMMyyyy";

    public static String getCurrentDateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(new Date());
    }

    public static String getTimestampForDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date parsedDate = dateFormat.parse(date);
            return String.valueOf(parsedDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getTimestamp() {
        return System.currentTimeMillis();
    }
}