package com.luke.skeleton.utils;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateUtils {

    private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_FORMAT = "MMM. d, yyyy hh:mm a";

    private static DateTimeFormatter shortDateTimeFormatter = DateTimeFormat.forPattern(SHORT_DATE_FORMAT);
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT)
            .withZone(DateTimeZone.getDefault());
    private static DateTimeFormatter isoDateTimeFormatter = ISODateTimeFormat.dateTime();

    public static DateTimeFormatter getShortDateTimeFormatter() {
        return shortDateTimeFormatter;
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public static DateTimeFormatter getIsoDateTimeFormatter() {
        return isoDateTimeFormatter;
    }

}
