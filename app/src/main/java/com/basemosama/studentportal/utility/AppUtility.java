package com.basemosama.studentportal.utility;

import android.content.Context;
import android.net.ConnectivityManager;

import com.basemosama.studentportal.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AppUtility {
    // Check Network Connection
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        } else {
            return false;
        }
    }

    public static <T> String getError(Context context, List<T> data, String defaultError) {
        String error = defaultError;
        if (!AppUtility.isConnectedToInternet(context)) {
            if (data != null && data.size() > 0) {
                error = "";
            } else {
                error = context.getString(R.string.main_no_internet_error);
            }
        }
        return error;
    }

    public static <T> String getError(Context context, T data, String defaultError) {
        String error = defaultError;
        if (!AppUtility.isConnectedToInternet(context)) {
            if (data != null) {
                error = "";
            } else {
                error = context.getString(R.string.main_no_internet_error);
            }
        }
        return error;
    }

    //Converting Date to FormattedString
    public static String getFormattedDate(Date date) {
        if (date == null)
            return "";
        DateFormat eventDateFormat = new SimpleDateFormat("E, d MMM, h:mm a", Locale.US);
        return eventDateFormat.format(date);
    }

    public static String getFormattedTime(String time) {
        String DEFAULT_PATTERN = "HH:mm:ss";
        DateFormat formatter = new SimpleDateFormat(DEFAULT_PATTERN, Locale.US);
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null)
            return "";
        DateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        return timeFormat.format(date);
    }

    //Converting Date to Past time String
    public static String getFormattedPastDate(Date date) {
        if (date == null)
            return "";

        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(date);
    }


}
