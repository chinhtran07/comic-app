package com.main.comicapp.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.main.comicapp.R;
import com.main.comicapp.models.Chapter;

import java.nio.charset.StandardCharsets;

public class NovelFragment extends Fragment {

    private TextView contentTextView;
    private Chapter currentChapter;
    private FirebaseStorage storage;

    public NovelFragment() {
        // Required empty public constructor
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel, container, false);
        contentTextView = view.findViewById(R.id.tv_content);
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentChapter = (Chapter) getArguments().getSerializable("chapter");
            loadContent();
        }

        return view;    }

    private void loadContent() {
        if (currentChapter != null) {
//            contentTextView.setText(currentChapter.getContent());
            StorageReference chapterFileRef = storage.getReference()
                    .child(currentChapter.getTitleId())
                    .child("ch" + currentChapter.getChapterNumber() + ".txt");
            new DownloadFileData().execute(chapterFileRef);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadFileData extends AsyncTask<StorageReference, Void, String> {

        @Override
        protected String doInBackground(StorageReference... storageReferences) {
            StorageReference fileRef = storageReferences[0];
            final long ONE_MEGABYTE = 1024 * 1024;

            try {
                byte[] data = Tasks.await(fileRef.getBytes(ONE_MEGABYTE));
                return new String(data, StandardCharsets.UTF_8);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String fileContent) {
            if (fileContent != null) {
                contentTextView.setText(fileContent);
            }
        }
    }

}