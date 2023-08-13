package com.sm.qms.controllers.visitor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFormatter {
    public static String format(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

}