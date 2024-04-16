package com.example.lab3_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private List<Word> mWordList;
    private LayoutInflater mLayoutInflater;
    private SelectionTracker<Long> mSelectionTracker;
    private WordItemKeyProvider mWordItemKeyProvider;

    public WordListAdapter(Context context, WordItemKeyProvider wordItemKeyProvider) {
        mLayoutInflater = LayoutInflater.from(context);
        mWordItemKeyProvider = wordItemKeyProvider;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        boolean isSelected = mSelectionTracker != null && mSelectionTracker.isSelected(mWordItemKeyProvider.getKey(position));
        holder.bindToWordViewHolder(position, isSelected);
    }

    @Override
    public int getItemCount() {
        return mWordList != null ? mWordList.size() : 0;
    }

    public class WordViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        WordItemDetails wordItemDetails;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            wordItemDetails = new WordItemDetails();
        }

        public void bindToWordViewHolder(int position, boolean isSelected) {
            wordTextView.setText(mWordList.get(position).getWord());
            wordItemDetails.id = mWordList.get(position).getId();
            wordItemDetails.position = position;
            itemView.setActivated(isSelected);
        }

        public WordItemDetails getWordItemDetails() {
            return wordItemDetails;
        }
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        mSelectionTracker = selectionTracker;
    }

    public void setWordList(List<Word> wordList) {
        if (mSelectionTracker != null) {
            mSelectionTracker.clearSelection();
        }
        mWordList = wordList;
        mWordItemKeyProvider.setWords(wordList);
        notifyDataSetChanged();
    }
}
