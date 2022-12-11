package edu.northeastern.numad22fateam26.finalProject.utils;

import android.os.Build;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import java.util.Date;

public class Common {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calculateTime(Date date) {
        long millis = date.toInstant().toEpochMilli();
        return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000, DateUtils.FORMAT_ABBREV_TIME).toString();
    }
}
