package com.example.lab3_1;

import static java.security.AccessController.getContext;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        EditTextPreference textPreference = findPreference("text_preference");
        textPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Toast.makeText(getContext(), "Preference with key: " + preference.getKey() + " is about to change to: " + newValue, Toast.LENGTH_SHORT).show();
            return true; // accept the change
        });
    }
}
