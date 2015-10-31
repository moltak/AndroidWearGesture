package com.horde.samantha.samantha.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by engeng on 9/30/15.
 */
public class ISO8601 {
    public static Calendar toCalendar(final String iso8601string, TimeZone timeZone)
            throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(timeZone);
        Date date = simpleDateFormat.parse(iso8601string);
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        return calendar;
    }
}
