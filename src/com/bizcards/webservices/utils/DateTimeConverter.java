/*
 * Copyright 2015 Formilize Inc. formilize.com
 */

package com.bizcards.webservices.utils;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by gd on 13/5/15.
 */
public class DateTimeConverter {

    private static DateTimeConverter instance;

    public static DateTimeConverter getInstance() {
        if(instance == null) instance = new DateTimeConverter();
        return instance;
    }

    public int daysBetween(Date start, Date stop){
        return (int)( (stop.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
    }

    public Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date getUTCTimeNow() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Calendar currentCal=Calendar.getInstance(TimeZone.getDefault());
        return currentCal.getTime();
    }

    public String getUTCTimeStringNow() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        Calendar currentCal = Calendar.getInstance(TimeZone.getDefault());
        return DatatypeConverter.printDateTime(currentCal);
    }


    public String getUTCDateString(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return DatatypeConverter.printDateTime(cal);
        }
        return null;
    }

    public Date getUTCDate(String dateString) {
        if (dateString != null) {
            Date date = DatatypeConverter.parseDateTime(dateString).getTime();
            return date;
        }
        return null;
    }

    public String getUTCDateStringFromLong(long date) {
        if (date != 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(date));
            return DatatypeConverter.printDateTime(cal);
        }
        return null;
    }

    public long getUTCDateLong(String dateString) {
        if (dateString != null) {
            Date date = DatatypeConverter.parseDateTime(dateString).getTime();
            return date.getTime();
        }
        return 0;
    }

}
