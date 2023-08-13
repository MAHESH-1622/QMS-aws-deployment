package com.sm.qms.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String TIMEZONE = "UTC";
    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final String DAY_PATTERN = "EEEE";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE));
    }

    public static long getTimestamp() {
        return getCalendar().getTimeInMillis();
    }

    public static String formatDuration(Duration duration) {
        long s = duration.getSeconds();
        return String.format("%dh%02dm", s / 3600, (s % 3600) / 60);
    }

    public static Date parse(String date) throws java.text.ParseException {
        return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(date);
    }

    public static String getDayOf(Date date) {
        return new SimpleDateFormat(DAY_PATTERN, Locale.getDefault()).format(date);
    }

    public static boolean isValidForAppointment(LocalDate parsedDate) {
        return parsedDate.compareTo(LocalDate.now()) >= 0;
    }

}
