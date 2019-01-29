package com.blog.utils;

import org.joda.time.DateTime;

import java.util.Date;

public class DateUtils {

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatFullDate(Date date) {
        return formatDate(date, FULL_DATE_FORMAT);
    }

    private static String formatDate(Date date, String format) {
        if (date == null)
            return null;

        return new DateTime(date).toString(format);
    }
}
