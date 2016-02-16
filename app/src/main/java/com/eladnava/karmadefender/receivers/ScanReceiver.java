package com.eladnava.karmadefender.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eladnava.karmadefender.logic.Notifier;
import com.eladnava.karmadefender.logic.Scanner;
import com.eladnava.karmadefender.model.KarmaScanResult;
import com.eladnava.karmadefender.utils.AppPreferences;

public class ScanReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Verify that app is enabled before scanning
        if (!AppPreferences.isAppEnabled(context))
        {
            return;
        }

        // Run vulnerability scanner
        KarmaScanResult result = Scanner.scan(context);

        // Found anything interesting?
        if (result.vulnerableNetworks.size() > 0)
        {
            // Found & deleted network(s)?
            if (result.networksDeletedSuccessfully)
            {
                // Notify user of deletion success
                Notifier.notifyVulnerableNetworksDeleted(result, context);
            }
            else
            {
                // Notify user and ask to manually delete the vulnerable networks
                Notifier.notifyVulnerableNetworksFound(result, context);
            }
        }
    }
}
