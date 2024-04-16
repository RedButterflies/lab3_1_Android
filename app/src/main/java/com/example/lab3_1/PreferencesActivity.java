package com.example.lab3_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class PreferencesActivity extends AppCompatActivity {

    TextView textPreferenceTextView;
    TextView listPreferenceTextView;
    TextView switch1TextView;
    TextView switch2TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_view);

        // Initialize views
        textPreferenceTextView = findViewById(R.id.textPreferenceTextView);
        listPreferenceTextView = findViewById(R.id.listPreferenceTextView);
        switch1TextView = findViewById(R.id.switch1TextView);
        switch2TextView = findViewById(R.id.switch2TextView);
        Button settingsButton = findViewById(R.id.settingsButton);

        // Set onClickListener for settings button
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SettingsActivity
                startActivity(new Intent(PreferencesActivity.this, SettingsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload preferences every time the activity is resumed
        loadPreferences();
    }

    private void loadPreferences() {
        // Access shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set default value for text_preference if it's empty
        if (TextUtils.isEmpty(sharedPreferences.getString("text_preference", ""))) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("text_preference", "default from Java");
            editor.commit(); // Use commit() for immediate effect
        }

        // Set text values for TextViews from shared preferences
        textPreferenceTextView.setText(sharedPreferences.getString("text_preference", "?"));
        listPreferenceTextView.setText(sharedPreferences.getString("list_preference", "?"));
        switch1TextView.setText(Boolean.toString(sharedPreferences.getBoolean("switch_1_preference", false)));
        switch2TextView.setText(Boolean.toString(sharedPreferences.getBoolean("switch_2_preference", false)));
    }
}


