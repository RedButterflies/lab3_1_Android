package com.example.lab3_1;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

    @Entity(tableName = "word_table")
    public class Word {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        private long mId;
        @NonNull
        @ColumnInfo(name = "word")
        private String mWord;

        public Word(@NonNull String word) {
            mWord = word;
        }

        public long getId() {
            return mId;
        }

        @NonNull
        public String getWord() {
            return mWord;
        }

        public void setId(long mId) {
            this.mId = mId;
        }
    }


