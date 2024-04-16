package com.example.lab3_1;
import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public WordRepository(Application application) {
        WordRoomDatabase wordRoomDatabase = WordRoomDatabase.getDatabase(application);
        mWordDao = wordRoomDatabase.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.insert(word));
    }

    public void deleteAll() {
        WordRoomDatabase.databaseWriteExecutor.execute(mWordDao::deleteAll);
    }

    public void deleteWord(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.deleteWord(word));
    }
}
