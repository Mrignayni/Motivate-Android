package com.jozapps.inspire.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {

    public static Date getDateAsGMTMidnight() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        return date.getTime();
    }

    public static Calendar createCalendarAtTimeToday(int second, int minute, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        return cal;
    }

    public static Calendar createCalendarNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal;
    }
}
