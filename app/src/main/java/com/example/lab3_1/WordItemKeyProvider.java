package com.example.lab3_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class provides mapping between Long keys and positions in the list
// It determines the key based on position and position based on the key
public class WordItemKeyProvider extends ItemKeyProvider<Long> {
    private Map<Long, Integer> mKeyToPosition;
    private List<Word> mWordList;

    public WordItemKeyProvider() {
        super(SCOPE_MAPPED);
        mWordList = null;
    }

    public void setWords(List<Word> wordList) {
        this.mWordList = wordList;
        mKeyToPosition = new HashMap<>(mWordList.size());
        for (int i = 0; i < mWordList.size(); ++i)
            mKeyToPosition.put(mWordList.get(i).getId(), i);
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        if (mWordList != null && position >= 0 && position < mWordList.size()) {
            return mWordList.get(position).getId();
        }
        return null;
    }

    @Override
    public int getPosition(@NonNull Long key) {
        if (mKeyToPosition.containsKey(key)) {
            return mKeyToPosition.get(key);
        }
        return RecyclerView.NO_POSITION;
    }
}
