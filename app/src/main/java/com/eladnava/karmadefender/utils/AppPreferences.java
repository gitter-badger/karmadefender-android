package com.eladnava.karmadefender.utils;

import android.content.Context;

import com.eladnava.karmadefender.R;

public class AppPreferences
{
    public static boolean isAppEnabled(Context context)
    {
        // Return enabled/disabled flag from SharedPreferences
        return SystemServices.getSharedPreferences(context).getBoolean(context.getString(R.string.enable_pref), context.getString(R.string.enable_default) == "true");
    }

    public static int getScanIntervalMinutes(Context context)
    {
        // Return scan interval in minutes from SharedPreferences
        return Integer.parseInt(SystemServices.getSharedPreferences(context).getString(context.getString(R.string.scan_interval_pref), context.getString(R.string.scan_interval_default)));
    }

}
