package org.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date convertStringToDate(String dateStr, String format) throws ParseException {
        Date date = null;
        if (dateStr != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
            date = dateFormatter.parse(dateStr);
        }
        return date;
    }
}
