package com.main.comicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.main.comicapp.R;
import com.main.comicapp.models.Chapter;

public class NovelFragment extends Fragment {

    private TextView contentTextView;
    private Chapter currentChapter;

    public NovelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel, container, false);
        contentTextView = view.findViewById(R.id.tv_content);

        if (getArguments() != null) {
            currentChapter = (Chapter) getArguments().getSerializable("chapter");
            loadContent();
        }

        return view;    }

    private void loadContent() {
        if (currentChapter != null) {
//            contentTextView.setText(currentChapter.getContent());
        }
    }

}