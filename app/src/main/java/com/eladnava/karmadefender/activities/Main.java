package com.eladnava.karmadefender.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eladnava.karmadefender.R;
import com.eladnava.karmadefender.logic.Notifier;
import com.eladnava.karmadefender.logic.ScanScheduler;
import com.eladnava.karmadefender.logic.Scanner;
import com.eladnava.karmadefender.model.KarmaScanResult;

public class Main extends AppCompatActivity
{
    ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set up activity UI
        initializeUI();

        // Run vulnerability scanner automatically on activity launch
        runVulnerabilityScanner();
    }

    private void initializeUI()
    {
        // Inflate main activity layout
        setContentView(R.layout.activity_main);

        // Get logo image
        mLogo = (ImageView) findViewById(R.id.logo);

        // Setup on-click listeners
        initializeListeners();
    }

    private void initializeListeners()
    {
        // Logo click
        mLogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Run vulnerability scanner on demand
                runVulnerabilityScanner();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Re-schedule a scan every X minutes (in case scan interval setting changed)
        ScanScheduler.rescheduleRecurringScan(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu - this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void viewSettings()
    {
        // Start the settings activity
        startActivity(new Intent(Main.this, Settings.class));
    }

    private void viewWifiSettings()
    {
        // Start the Wi-Fi settings screen
        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
    }

    private void runVulnerabilityScanner()
    {
        // Run vulnerability scanner logic
        KarmaScanResult result = Scanner.scan(Main.this);

        // No vulnerable networks?
        if (result.vulnerableNetworks.size() == 0)
        {
            // Show a toast with "no vulnerable networks found"
            Toast.makeText(this, getString(R.string.no_vulnerable_networks), Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Delete success?
            if (result.networksDeletedSuccessfully)
            {
                // Show a toast with "X vulnerable networks deleted!"
                Toast.makeText(this, getResources().getQuantityString(R.plurals.vulnerable_networks_deleted, result.vulnerableNetworks.size(), result.vulnerableNetworks.size()), Toast.LENGTH_SHORT).show();
            }
            else
            {
                // Show popup asking the user to manually delete the vulnerable networks (since we failed)
                startActivity(Notifier.getScanResultPopupIntent(result, this));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item ID cases
        switch (item.getItemId())
        {
            // Scan
            case R.id.action_scan:
                runVulnerabilityScanner();
                return true;
            // Settings
            case R.id.action_settings:
                viewSettings();
                return true;
            // Wi-Fi Settings
            case R.id.action_wifi_settings:
                viewWifiSettings();
                return true;
        }

        // Don't consume the event
        return super.onOptionsItemSelected(item);
    }
}
