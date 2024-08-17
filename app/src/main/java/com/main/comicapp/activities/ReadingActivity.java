package com.main.comicapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.main.comicapp.R;
import com.main.comicapp.enums.TitleFormat;
import com.main.comicapp.fragments.ComicFragment;
import com.main.comicapp.fragments.NovelFragment;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;
import com.main.comicapp.viewmodels.ReadingPositionViewModel;

import java.util.List;

public class ReadingActivity extends AppCompatActivity {

    private ImageButton btnPrevChapter, btnNextChapter, btnBack, btnOption;
    private TextView tvTitle;
    private ChapterViewModel chapterViewModel;
    private Chapter currentChapter;
    private int currentChapterIndex;
    private List<String> chapterDocumentIds;
    private TitleFormat titleFormat;
    String titleId;

    private ReadingPositionViewModel readingPositionViewModel;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        init();
        setupListeners();
        loadInitialData();
    }

    private void init() {
        btnBack = findViewById(R.id.btn_back);
        btnOption = findViewById(R.id.btn_more);
        btnNextChapter = findViewById(R.id.btn_next_chapter);
        btnPrevChapter = findViewById(R.id.btn_prev_chapter);
        tvTitle = findViewById(R.id.tv_title);

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
        readingPositionViewModel = new ViewModelProvider(this).get(ReadingPositionViewModel.class);

        sharedPreferences = getSharedPreferences("ReadingPositions", Context.MODE_PRIVATE);

    }

    private void setupListeners() {
        btnNextChapter.setOnClickListener(v -> loadNextChapter());
        btnPrevChapter.setOnClickListener(v -> loadPreviousChapter());
        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void loadInitialData() {
        Intent intent = getIntent();
        if (intent != null) {
            titleId = intent.getStringExtra("titleId");
            String format = intent.getStringExtra("titleFormat");
            if (format != null) {
                titleFormat = TitleFormat.valueOf(format);
            }

            if (titleId != null) {
                // loadReadingPosition(titleId);
                Chapter chapter = (Chapter) intent.getSerializableExtra("chapter");
                loadChapterById(chapter.getId(), chapter.getChapterNumber() - 1);
                if (chapterDocumentIds == null) {
                    loadChapterDocumentIds();
                }
            }
        }
    }

    private void loadReadingPosition(String titleId) {
        // Load from SharedPreferences
        String chapterId = sharedPreferences.getString(titleId + "_chapterId", null);
        String pageId = sharedPreferences.getString(titleId + "_pageId", null);

        if (chapterId != null) {
            // If data exists in SharedPreferences, load chapter and pages
            loadChapterById(chapterId, -1);
        } else {
            // Else, load from Firestore
            readingPositionViewModel.getReadingPosition(titleId, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String chapterId = documentSnapshot.getString("chapterId");
                        String pageId = documentSnapshot.getString("pageId");
                        if (chapterId != null) {
                            loadChapterById(chapterId, -1);
                        }
                    }
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    private void loadChapterData(Chapter chapter) {
        tvTitle.setText(String.format("Chapter %d", chapter.getChapterNumber()));
    }

    private void loadNextChapter() {
        if (chapterDocumentIds != null && !chapterDocumentIds.isEmpty()) {
            int nextIndex = currentChapterIndex + 1;
            if (nextIndex < chapterDocumentIds.size()) {
                String nextChapterId = chapterDocumentIds.get(nextIndex);
                loadChapterById(nextChapterId, nextIndex);
            } else {
                Toast.makeText(this, "No next chapter available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPreviousChapter() {
        if (chapterDocumentIds != null && !chapterDocumentIds.isEmpty()) {
            int prevIndex = currentChapterIndex - 1;
            if (prevIndex >= 0) {
                String prevChapterId = chapterDocumentIds.get(prevIndex);
                loadChapterById(prevChapterId, prevIndex);
            } else {
                Toast.makeText(this, "No previous chapter available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadChapterById(String chapterId, int chapterIndex) {
        chapterViewModel.getChapter(chapterId).observe(this, new Observer<Chapter>() {
            @Override
            public void onChanged(Chapter chapter) {
                if (chapter != null) {
                    currentChapter = chapter;
                    currentChapterIndex = chapterIndex;
                    loadChapterData(currentChapter);
                    loadFragment();
                    saveReadingPosition();
                }
            }
        });
    }

    private void loadChapterDocumentIds() {
        chapterViewModel.getChapterDocumentIds(titleId).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if (strings != null) {
                    if (currentChapter != null) {
                        chapterDocumentIds = strings;
                        currentChapterIndex = chapterDocumentIds.indexOf(currentChapter.getId());
                    }
                }
            }
        });
    }

    private void loadFragment() {
        Fragment fragment;
        if (titleFormat == TitleFormat.COMIC) {
            fragment = new ComicFragment();
        } else {
            fragment = new NovelFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString("chapterId", currentChapter.getId());
        bundle.putSerializable("chapter", currentChapter);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    private void saveReadingPosition() {
        if (currentChapter != null && titleId != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(titleId + "_chapterId", currentChapter.getId());
            editor.apply();

            readingPositionViewModel.saveReadingPosition(titleId, currentChapter.getId(), null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveReadingPosition();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveReadingPosition();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        saveReadingPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveReadingPosition();
        // Cleanup or release resources if necessary
    }
}
