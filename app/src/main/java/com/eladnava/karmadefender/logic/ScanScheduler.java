package com.eladnava.karmadefender.logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eladnava.karmadefender.config.Logging;
import com.eladnava.karmadefender.receivers.ScanReceiver;
import com.eladnava.karmadefender.utils.AppPreferences;
import com.eladnava.karmadefender.utils.SystemServices;

public class ScanScheduler
{
    public static void rescheduleRecurringScan(Context context)
    {
        // Get scan interval from app settings and convert to milliseconds
        int scanIntervalMillis = 1000 * 60 * AppPreferences.getScanIntervalMinutes(context);

        // Get alarm manager instance
        AlarmManager alarmManager = SystemServices.getAlarmManager(context);

        // Get ScanReceiver intent
        PendingIntent scanIntent = getScanIntent(context);

        // Cancel previously scheduled scans
        alarmManager.cancel(scanIntent);

        // Make sure app is enabled before re-scheduling a recurring scan
        if (!AppPreferences.isAppEnabled(context))
        {
            return;
        }

        // Set repeating RTC alarm every X ms
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + scanIntervalMillis, scanIntervalMillis, scanIntent);

        // Output to log
        Log.d(Logging.TAG, "Scheduled scan every " + AppPreferences.getScanIntervalMinutes(context) + " minute(s)");
    }

    public static PendingIntent getScanIntent(Context context)
    {
        // Create intent for ScanReceiver class
        Intent intent = new Intent(context, ScanReceiver.class);

        // Convert to pending intent (for alarm manager)
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
