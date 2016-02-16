package com.eladnava.karmadefender.logic;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.eladnava.karmadefender.config.Logging;
import com.eladnava.karmadefender.model.KarmaScanResult;
import com.eladnava.karmadefender.utils.SystemServices;

import java.util.ArrayList;
import java.util.List;

public class Scanner
{
    public static KarmaScanResult scan(Context context)
    {
        // Log scan
        Log.d(Logging.TAG, "Scan started");

        // Prepare scan result
        KarmaScanResult result = new KarmaScanResult();

        // Get vulnerable networks (those which have no security configured)
        result.vulnerableNetworks = getVulnerableNetworks(context);

        // Log vulnerable network count
        Log.d(Logging.TAG, result.vulnerableNetworks.size() + " vulnerable network(s) found");

        // Attempt to delete vulnerable networks automatically
        result.networksDeletedSuccessfully = deleteVulnerableNetworks(result.vulnerableNetworks, context);

        // Return scan result and let caller handle alerting the user
        return result;
    }

    private static boolean deleteVulnerableNetworks(List<WifiConfiguration> vulnerableNetworks, Context context)
    {
        // Nothing to delete?
        if (vulnerableNetworks.size() == 0)
        {
            return false;
        }

        // Acquire an instance of the Android Wi-Fi manager
        WifiManager wifiManager = SystemServices.getWifiManager(context);

        // Delete vulnerable networks
        // Note - Android 6.0 prevents deletion of Wi-Fi networks created by the user (or other apps, for that matter)
        // Keep track of whether the delete operation succeeds -- ask user to delete the networks manually if it fails
        boolean deleteFailed = false;

        // Traverse vulnerable networks
        for (WifiConfiguration network : vulnerableNetworks)
        {
            // Attempt to remove network from device's saved networks
            if (!wifiManager.removeNetwork(network.networkId))
            {
                // Delete failed, ask user to delete manually
                deleteFailed = true;
            }
        }

        // Save our delete operations
        wifiManager.saveConfiguration();

        // Return flag indicating whether deletion was successful (only if all networks were deleted successfully)
        return !deleteFailed;
    }

    public static List<WifiConfiguration> getVulnerableNetworks(Context context)
    {
        // Acquire an instance of the Wi-Fi manager
        WifiManager wifiManager = SystemServices.getWifiManager(context);

        // Prepare list of vulnerable networks
        List<WifiConfiguration> vulnerableNetworks = new ArrayList<>();

        // Grab all saved networks
        List<WifiConfiguration> allNetworks = wifiManager.getConfiguredNetworks();

        // This call may fail if Wi-Fi is disabled
        if (allNetworks == null)
        {
            // Return empty list
            return vulnerableNetworks;
        }

        // Traverse all saved networks
        for (WifiConfiguration network : allNetworks)
        {
            // Is it an open (no-auth) type of network (therefore vulnerable)?
            if (isNetworkVulnerableToKarma(network))
            {
                // Are we not connected to it right now (to avoid disconnecting a legitimate network)?
                if (network.status != WifiConfiguration.Status.CURRENT)
                {
                    // Add to vulnerable networks list
                    vulnerableNetworks.add(network);
                }
            }
        }

        // Return vulnerable networks
        return vulnerableNetworks;
    }

    private static boolean isNetworkVulnerableToKarma(WifiConfiguration config)
    {
        // WPA PSK key?
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK))
        {
            return false;
        }

        // EAP / IEEE key?
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X))
        {
            return false;
        }

        // WEP key?
        if (config.wepKeys[0] != null)
        {
            return false;
        }

        // Open network, vulnerable!
        return true;
    }
}