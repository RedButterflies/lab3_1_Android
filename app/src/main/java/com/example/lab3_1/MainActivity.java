package com.example.lab3_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private WordViewModel mWordViewModel;
    private WordListAdapter mAdapter;
    private FloatingActionButton mMainFab;
    private SelectionTracker<Long> mSelectionTracker;
    private FloatingActionButton mDeleteFab;
    private boolean mIsMainFabAdd = true;
    private ActivityResultLauncher<Intent> mActivityResultLauncher;
    private WordItemKeyProvider mWordItemKeyProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);


        /*TextView textPreferenceTextView = findViewById(R.id.textPreferenceTextView);
        TextView listPreferenceTextView = findViewById(R.id.listPreferenceTextView);
        TextView switch1TextView = findViewById(R.id.switch1TextView);
        TextView switch2TextView = findViewById(R.id.switch2TextView);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);*/



        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SettingsActivity
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            }
        });


        /*
        String textPreference = sharedPreferences.getString("text_preference", "");
        if (TextUtils.isEmpty(textPreference)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("text_preference", "default from Java");
            editor.apply();
        }

        textPreferenceTextView.setText(sharedPreferences.getString("text_preference", "?"));
        listPreferenceTextView.setText(sharedPreferences.getString("list_preference", "?"));
        switch1TextView.setText(Boolean.toString(sharedPreferences.getBoolean("switch_1_preference", false)));
        switch2TextView.setText(Boolean.toString(sharedPreferences.getBoolean("switch_2_preference", false)));*/


        // Initialize ViewModel
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        // Observe word list from ViewModel
        mWordViewModel.getAllWords().observe(this, words -> {
            // Update RecyclerView adapter with new word list
            mAdapter.setWordList(words);
        });

        // Initialize WordItemKeyProvider
        mWordItemKeyProvider = new WordItemKeyProvider();
        // Initialize adapter with WordItemKeyProvider
        mAdapter = new WordListAdapter(this, mWordItemKeyProvider);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and register ActivityResultLauncher
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        String wordText = result.getData().getStringExtra(NewWordActivity.EXTRA_REPLY);
                        Word word = new Word(wordText);
                        mWordViewModel.insert(word);
                    }
                });

        // Initialize FloatingActionButton
        mMainFab = findViewById(R.id.fabMain);
        mMainFab.setOnClickListener(view -> mainFabClicked());

        // Now, initialize the SelectionTracker after setting up the adapter
        mSelectionTracker = new SelectionTracker.Builder<>(
                "word-selection",
                recyclerView,
                mWordItemKeyProvider,
                new WordItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()).build();
        // Set the SelectionTracker to the adapter
        mAdapter.setSelectionTracker(mSelectionTracker);

        // Setup FloatingActionButton for deletion
        mDeleteFab = findViewById(R.id.fabDelete);
        mDeleteFab.setOnClickListener(view -> deleteSelection());

        // Selection tracker will notify about selection changes
        mSelectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onSelectionChanged() {
                // Show/hide delete button
                updateFabs();
                super.onSelectionChanged();
            }

            @Override
            public void onSelectionRestored() {
                // Show/hide delete button
                updateFabs();
                super.onSelectionRestored();
            }

            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
            }
        });
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.clear_data) {
                Toast.makeText(this, "Clearing the data...", Toast.LENGTH_SHORT).show();
                mWordViewModel.deleteAll();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void mainFabClicked() {
            // Main fab can be "+" (add) or "x" (cancel)
            if (mIsMainFabAdd) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                mActivityResultLauncher.launch(intent);
            } else {
                mSelectionTracker.clearSelection();
            }
        }

        private void deleteSelection() {
            Selection<Long> selection = mSelectionTracker.getSelection();
            int wordPosition = -1;
            List<Word> wordList = mWordViewModel.getAllWords().getValue();
            // Iterate through selected item identifiers and delete items
            for (long wordId : selection) {
                wordPosition = mWordItemKeyProvider.getPosition(wordId);
                mWordViewModel.deleteWord(wordList.get(wordPosition));
            }
        }

// Depending on the situation, the main fab is "+" or "x"
// Additionally, show or hide the delete button
        private void updateFabs() {
            if (mSelectionTracker.hasSelection()) {
                mDeleteFab.setVisibility(View.VISIBLE);
                mMainFab.setImageDrawable(getDrawable(R.drawable.ic_baseline_cancel_24));
                mIsMainFabAdd = false;
            } else {
                mDeleteFab.setVisibility(View.GONE);
                mMainFab.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_24));
                mIsMainFabAdd = true;
            }
        }

        @Override
        protected void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            // Save selection state
            mSelectionTracker.onSaveInstanceState(outState);
        }

        @Override
        protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            // Restore selection state
            mSelectionTracker.onRestoreInstanceState(savedInstanceState);
        }
    }
