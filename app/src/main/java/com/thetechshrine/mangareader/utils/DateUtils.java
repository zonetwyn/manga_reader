package com.thetechshrine.mangareader.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.thetechshrine.mangareader.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    public static String formatUTCDate(Context context, String utcDate) {
        if (utcDate == null || context == null) return "";

        String date = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date currentDate = new Date();
        Date createdAt = null;
        long createdAtTime = 0;
        try {
            createdAt = format.parse(utcDate);
            format.setTimeZone(TimeZone.getDefault());
            String formatted = format.format(createdAt);
            createdAtTime = format.parse(formatted).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long elapsed = currentDate.getTime() - createdAtTime;
        long days = TimeUnit.DAYS.convert(elapsed, TimeUnit.MILLISECONDS);
        long hours = TimeUnit.HOURS.convert(elapsed, TimeUnit.MILLISECONDS);
        long minutes = TimeUnit.MINUTES.convert(elapsed, TimeUnit.MILLISECONDS);

        if (days >= 1 && days <= 30) {
            date += "" + days + context.getString(R.string.days_ago);
        } else if (days > 30) {
            date += "on " + utcDate.split("T")[0].replaceAll("-", "/");
        } else {
            if (hours >= 1 && hours <= 24) {
                date += "" + hours + context.getString(R.string.hours_ago);
            } else {
                date += "" + minutes + context.getString(R.string.minutes_ago);
            }
        }

        return date;
    }
}
