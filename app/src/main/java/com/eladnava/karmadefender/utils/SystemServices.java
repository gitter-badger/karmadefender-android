package com.eladnava.karmadefender.utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

public class SystemServices
{
    private static WifiManager mWifiManager;
    private static AlarmManager mAlarmManager;
    private static SharedPreferences mSharedPreferences;
    private static NotificationManager mNotificationManager;

    public static SharedPreferences getSharedPreferences(Context context)
    {
        // First time?
        if (mSharedPreferences == null)
        {
            // Acquire system service
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        // Return cached instance
        return mSharedPreferences;
    }

    public static WifiManager getWifiManager(Context context)
    {
        // First time?
        if (mWifiManager == null)
        {
            // Acquire system service
            mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }

        // Return cached instance
        return mWifiManager;
    }

    public static AlarmManager getAlarmManager(Context context)
    {
        // First time?
        if (mAlarmManager == null)
        {
            // Acquire system service
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        // Return cached instance
        return mAlarmManager;
    }

    public static NotificationManager getNotificationManager(Context context)
    {
        // First time?
        if (mNotificationManager == null)
        {
            // Acquire system service
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // Return cached instance
        return mNotificationManager;
    }
}
