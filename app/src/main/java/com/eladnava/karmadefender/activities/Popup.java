package com.eladnava.karmadefender.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eladnava.karmadefender.R;
import com.eladnava.karmadefender.config.Logging;
import com.eladnava.karmadefender.logic.Notifier;
import com.eladnava.karmadefender.logic.Scanner;
import com.eladnava.karmadefender.utils.ui.LayoutUtil;

public class Popup extends AppCompatActivity
{
    Button mOkay;
    Button mIgnore;
    TextView mNetworks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set up activity UI
        initializeUI();

        // Read popup data from intent extras
        initializePopup();

        // Log popup display
        Log.d(Logging.TAG, "Displayed popup");
    }

    private void initializeUI()
    {
        // Inflate popup activity layout
        setContentView(R.layout.activity_popup);

        // Cache views
        mOkay = (Button) findViewById(R.id.okay);
        mIgnore = (Button) findViewById(R.id.ignore);
        mNetworks = (TextView) findViewById(R.id.networks);

        // Setup on-click listeners
        initializeListeners();
    }

    private void initializePopup()
    {
        // Get vulnerable SSIDs from intent extras
        String vulnerableSSIDs = getIntent().getStringExtra(getString(R.string.ssids_extra));

        // Display them (to let the user know which ones to delete)
        mNetworks.setText(vulnerableSSIDs);
    }

    private void initializeListeners()
    {
        // "Okay" button click
        mOkay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Open the Wi-Fi settings screen
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });

        // "Ignore" button click
        mIgnore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Close popup (with fade out animation)
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // User resumed popup activity after deleting the vulnerable networks?
        if (Scanner.getVulnerableNetworks(this).size() == 0)
        {
            // Hide the notification (if not already hidden)
            Notifier.hideScanResultNotification(this);

            // Show a toast with a "You're safe now" message
            Toast.makeText(this, getString(R.string.manual_delete_success), Toast.LENGTH_SHORT).show();

            // Close popup (with fade out animation)
            onBackPressed();
        }
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        // Center the popup in the middle of the screen (horizontally + veritcally)
        LayoutUtil.centerPopupInParentWindow(getWindow());
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        // Fade out window gradually when activity is closed via back button
        overridePendingTransition(0, R.anim.fade_out);
    }
}
