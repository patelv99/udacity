package com.vish.travelbook.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final DateTimeFormatter NUMERICAL_DATE_FORMAT       = DateTimeFormat.forPattern("MM/dd/yyyy");
    public static final DateTimeFormatter NUMERICAL_DATE_SHORT_FORMAT = DateTimeFormat.forPattern("MM/dd/yy");
    public static final DateTimeFormatter TEXT_DATE_FORMAT            = DateTimeFormat.forPattern("MMM dd, yyyy");
    public static final DateTimeFormatter TIME_FORMAT                 = DateTimeFormat.forPattern("h:mm aa");

    public static String getDatesDuration(DateTime startDate, DateTime endDate) {
        if (startDate.getDayOfYear() == endDate.getDayOfYear()) {
            return getTextDate(startDate);
        } else {
            return getTextDate(startDate) + " - " + getTextDate(endDate);
        }
    }

    public static String getNumericalDate(DateTime dateTime) {
        return NUMERICAL_DATE_FORMAT.print(dateTime);
    }

    public static String getNumericalShortDate(DateTime dateTime) {
        return NUMERICAL_DATE_SHORT_FORMAT.print(dateTime);
    }

    public static String getTextDate(DateTime dateTime) {
        return TEXT_DATE_FORMAT.print(dateTime);
    }

    public static String getTimeDuration(DateTime startTime, DateTime endTime) {

        return getTime(startTime) + " - " + getTime(endTime);
    }

    public static String getTime(DateTime dateTime) {
        return TIME_FORMAT.print(dateTime);
    }
}
