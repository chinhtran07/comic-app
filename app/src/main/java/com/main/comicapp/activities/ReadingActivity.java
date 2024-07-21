package com.main.comicapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

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
            String titleId = (String) intent.getSerializableExtra("titleId");
            if (currentChapter != null) {
                loadChapterData(currentChapter);
                loadPages();
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
        // Logic to load the next chapter
        // Update `currentChapter` and call `loadChapterData(currentChapter)` and `loadPages()`
    }

    private void loadPreviousChapter() {
        // Logic to load the previous chapter
        // Update `currentChapter` and call `loadChapterData(currentChapter)` and `loadPages()`
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup or release resources if necessary
    }
}
