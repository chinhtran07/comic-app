package com.main.comicapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.ChaptersAdapter;
import com.main.comicapp.adapters.PagesAdapter;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.models.Page;
import com.main.comicapp.viewmodels.ChapterViewModel;
import com.main.comicapp.viewmodels.PageViewModel;

import java.util.HashMap;
import java.util.List;

public class ReadingActivity extends AppCompatActivity {

    private ImageButton btnPrevChapter, btnNextChapter, btnBack, btnOption;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private PagesAdapter pagesAdapter;
    private PageViewModel pageViewModel;
    private ChapterViewModel chapterViewModel;
    private ChaptersAdapter chaptersAdapter;
    private Chapter currentChapter;
    private int currentChapterIndex;
    private List<String> chapterDocumentIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        init();
        setupListeners();
    }

    private void init() {
        btnBack = findViewById(R.id.btn_back);
        btnOption = findViewById(R.id.btn_more);
        btnNextChapter = findViewById(R.id.btn_next_chapter);
        btnPrevChapter = findViewById(R.id.btn_prev_chapter);
        tvTitle = findViewById(R.id.tv_title);
        recyclerView = findViewById(R.id.recycler_img);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pagesAdapter = new PagesAdapter(this, null);
        recyclerView.setAdapter(pagesAdapter);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);


        Intent intent = getIntent();
        if (intent != null) {
            currentChapter = (Chapter) intent.getSerializableExtra("chapter");
            if (currentChapter != null) {
                loadChapterData(currentChapter);
                loadPages();
            }
            String titleId = (String) intent.getSerializableExtra("titleId");
            if (chapterDocumentIds == null) {
                loadChapterDocumentIds(titleId);
            }
        }

    }

    private void setupListeners() {
        btnNextChapter.setOnClickListener(v -> loadNextChapter());
        btnPrevChapter.setOnClickListener(v -> loadPreviousChapter());
        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    @SuppressLint("DefaultLocale")
    private void loadChapterData(Chapter chapter) {
        tvTitle.setText(String.format("Chapter %d", chapter.getChapterNumber()));
    }

    private void loadPages() {
        if (currentChapter != null) {
            pageViewModel.getPages(new HashMap<>(), currentChapter.getId()).observe(this, new Observer<List<Page>>() {
                @Override
                public void onChanged(@Nullable List<Page> pages) {
                    if (pages != null) {
                        pagesAdapter.setPages(pages);
                    }
                }
            });
        }
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
        // Giả sử bạn có một phương thức để lấy Chapter từ Firestore theo ID
        chapterViewModel.getChapter(chapterId).observe(this, new Observer<Chapter>() {
            @Override
            public void onChanged(Chapter chapter) {
                if (chapter != null) {
                    currentChapter = chapter;
                    currentChapterIndex = chapterIndex;
                    loadChapterData(currentChapter);
                    loadPages();
                }
            }
        });
    }

    private void loadChapterDocumentIds(String titleId) {
        chapterViewModel.getChapterDocumentIds(titleId)
                .thenAccept(documentIds ->{
                    chapterDocumentIds = documentIds;
                    currentChapterIndex = chapterDocumentIds.indexOf(currentChapter.getId());
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    Toast.makeText(this, "Error loading chapter list", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup or release resources if necessary
    }
}
