package com.eladnava.karmadefender.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.eladnava.karmadefender.config.Logging;
import com.eladnava.karmadefender.logic.ScanScheduler;

public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Make sure the right intent was provided
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            // Log phone startup
            Log.d(Logging.TAG, "Boot completed");

            // Schedule recurring vulnerability scan
            ScanScheduler.rescheduleRecurringScan(context);
        }
    }

}