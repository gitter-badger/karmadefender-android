package com.eladnava.karmadefender.logic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.support.v4.app.NotificationCompat;

import com.eladnava.karmadefender.R;
import com.eladnava.karmadefender.activities.Main;
import com.eladnava.karmadefender.activities.Popup;
import com.eladnava.karmadefender.config.Logging;
import com.eladnava.karmadefender.model.KarmaScanResult;
import com.eladnava.karmadefender.utils.SystemServices;

public class Notifier
{
    // Notification ID for all app notifications (overwrite previous ones)
    private static final int NOTIFICATION_ID = 1000;

    public static void notifyVulnerableNetworksDeleted(KarmaScanResult result, Context context)
    {
        // Get notification manager
        NotificationManager mNotificationManager = SystemServices.getNotificationManager(context);

        // Prepare a "vulnerable networks deleted" message
        String scanResult = context.getResources().getQuantityString(R.plurals.vulnerable_networks_deleted, result.vulnerableNetworks.size(), result.vulnerableNetworks.size());

        // Build the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(Logging.TAG)
                .setAutoCancel(true)
                .setContentText(scanResult);

        // Set content intent
        mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, getMainActivityIntent(context), 0));

        // Display the notification
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void notifyVulnerableNetworksFound(KarmaScanResult result, Context context)
    {
        // Get notification manager
        NotificationManager mNotificationManager = SystemServices.getNotificationManager(context);

        // Prepare a "vulnerable networks found" message
        String scanResult = context.getResources().getQuantityString(R.plurals.vulnerable_networks_found, result.vulnerableNetworks.size(), result.vulnerableNetworks.size());

        // Build the notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(Logging.TAG)
                .setAutoCancel(true)
                .setContentText(scanResult);

        // Set content intent
        mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, getScanResultPopupIntent(result, context), 0));

        // Display the notification
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void hideScanResultNotification(Context context)
    {
        // Get notification manager
        NotificationManager mNotificationManager = SystemServices.getNotificationManager(context);

        // Cancel active notifications with the specified ID
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public static Intent getScanResultPopupIntent(KarmaScanResult result, Context context)
    {
        // Create new popup intent
        Intent popupIntent = new Intent(context, Popup.class);

        // Set flags for popup-like behavior
        popupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        popupIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        // Prepare a string containing all vulnerable SSIDs
        String vulnerableSSIDs = "";

        // Traverse vulnerable networks
        for (WifiConfiguration network : result.vulnerableNetworks)
        {
            // Add SSID (remove double-quotes) and separate with line break
            vulnerableSSIDs += network.SSID.replace("\"", "") + "\n";
        }

        // Set boilerplate action (otherwise popup activity receives null extras)
        // http://stackoverflow.com/a/24047080/1123355
        popupIntent.setAction(vulnerableSSIDs);

        // Pass on the SSIDs string for display in the popup activity
        popupIntent.putExtra(context.getString(R.string.ssids_extra), vulnerableSSIDs);

        // Finally, return the intent
        return popupIntent;
    }

    public static Intent getMainActivityIntent(Context context)
    {
        // Create new intent linking to main activity
        return new Intent(context, Main.class);
    }
}