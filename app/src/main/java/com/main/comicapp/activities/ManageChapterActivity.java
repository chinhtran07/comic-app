package com.main.comicapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.ChaptersAdapter;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.util.Date;

public class ManageChapterActivity /*extends AppCompatActivity implements ChaptersAdapter.OnChapterClickListener*/ {
//    private ChapterViewModel chapterViewModel;
//    private ChaptersAdapter chaptersAdapter;
//    private EditText chapterNumberInput;
//    private EditText descriptionInput;
//    private EditText titleIdInput;
//    private Button addChapterButton;
//    private Button updateChapterButton;
//    private Button deleteChapterButton;
//    private String selectedChapterId = null;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_chapter);
//
//        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
//
//        chapterNumberInput = findViewById(R.id.chapterNumberInput);
//        descriptionInput = findViewById(R.id.descriptionInput);
//        titleIdInput = findViewById(R.id.titleIdInput);
//        addChapterButton = findViewById(R.id.addChapterButton);
//        updateChapterButton = findViewById(R.id.updateChapterButton);
//        deleteChapterButton = findViewById(R.id.deleteChapterButton);
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_view_chapters);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        chaptersAdapter = new ChaptersAdapter(null);
//        chaptersAdapter.setListener(this);
//        recyclerView.setAdapter(chaptersAdapter);
//
//        chapterViewModel.getChapters("").observe(this, chapters -> {
//            if (chapters != null) {
//                chaptersAdapter.setChapters(chapters);
//            }
//        });
//
//        addChapterButton.setOnClickListener(v -> addChapter());
//        updateChapterButton.setOnClickListener(v -> updateChapter());
//        deleteChapterButton.setOnClickListener(v -> deleteChapter());
//    }
//
//    private void addChapter() {
//        String chapterNumberStr = chapterNumberInput.getText().toString();
//        String description = descriptionInput.getText().toString();
//        String titleId = titleIdInput.getText().toString();
//
//        if (chapterNumberStr.isEmpty() || description.isEmpty() || titleId.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int chapterNumber;
//        try {
//            chapterNumber = Integer.parseInt(chapterNumberStr);
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Please enter a valid number for chapter", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Date uploadedDate = new Date();
//
//        Chapter chapter = new Chapter(chapterNumber, description, uploadedDate, titleId);
//        chapterViewModel.addChapter(chapter);
//        clearInputs();
//    }
//
//    private void updateChapter() {
//        if (selectedChapterId == null) {
//            Toast.makeText(this, "Please select a chapter to update", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String chapterNumberStr = chapterNumberInput.getText().toString();
//        String description = descriptionInput.getText().toString();
//        String titleId = titleIdInput.getText().toString();
//
//        if (chapterNumberStr.isEmpty() || description.isEmpty() || titleId.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int chapterNumber;
//        try {
//            chapterNumber = Integer.parseInt(chapterNumberStr);
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Please enter a valid number for chapter", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Date uploadedDate = new Date();
//
//        Chapter chapter = new Chapter(chapterNumber, description, uploadedDate, titleId);
//        chapter.setId(selectedChapterId);
//        chapterViewModel.updateChapter(selectedChapterId, chapter);
//        clearInputs();
//    }
//
//    private void deleteChapter() {
//        if (selectedChapterId == null) {
//            Toast.makeText(this, "Please select a chapter to delete", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        chapterViewModel.deleteChapter(selectedChapterId);
//        clearInputs();
//    }
//
//    private void clearInputs() {
//        chapterNumberInput.setText("");
//        descriptionInput.setText("");
//        titleIdInput.setText("");
//        selectedChapterId = null;
//    }
//
//    @Override
//    public void onChapterClick(Chapter chapter) {
//        chapterNumberInput.setText(String.valueOf(chapter.getChapterNumber()));
//        descriptionInput.setText(chapter.getDescription());
//        titleIdInput.setText(chapter.getTitleId());
//        selectedChapterId = chapter.getId();
//    }
}
