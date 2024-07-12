package com.main.comicapp.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Requires API Level 26
@RequiresApi(api = Build.VERSION_CODES.O)
public class TimeUtils {
    public static ZonedDateTime convertTimeString(String timeString) {
        ZonedDateTime resultTime = null;
        try {
                resultTime = ZonedDateTime.parse(timeString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } catch (DateTimeParseException e) {
            if (e.getMessage() != null) {
                Log.e("com.main.comicapp.utils.TimeUtils","Invalid pattern:" + e.getMessage());
            }
        }
        return resultTime;
    }

    /* Localized format quick ref:
    'at': Any string you want to appear in the format
    EEEE: Full day name (Friday)
    MMMM: Full month name (July)
    d: Day of month (12)
    yyyy: Year (2024)
    h: Hour (1-12)
    mm: Minute (45)
    a: AM/PM marker (PM)
    z: Time zone abbreviation (ICT)
     */
    public static String timeToHumanReadable(ZonedDateTime time, String format) {
        DateTimeFormatter formatPattern = DateTimeFormatter.ofPattern(format);
        String formattedString = "";
        try {
            formattedString = time.format(formatPattern);
        } catch (DateTimeParseException e) {
            Log.e("com.main.comicapp.utils.TimeUtils", "Invalid pattern: " + e.getMessage());
        }
        return formattedString;
    }

    public static String timeToISOTimeString(ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

}
