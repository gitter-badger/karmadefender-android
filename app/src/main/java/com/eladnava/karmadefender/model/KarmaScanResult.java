package com.eladnava.karmadefender.model;

import android.net.wifi.WifiConfiguration;

import java.util.ArrayList;
import java.util.List;

public class KarmaScanResult
{
    public boolean networksDeletedSuccessfully;
    public List<WifiConfiguration> vulnerableNetworks;

    public KarmaScanResult()
    {
        // Initialize with default values
        networksDeletedSuccessfully = false;
        vulnerableNetworks = new ArrayList<>();
    }
}
