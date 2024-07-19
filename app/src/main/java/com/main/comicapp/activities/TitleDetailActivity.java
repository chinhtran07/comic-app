package com.main.comicapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.ChaptersAdapter;
import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.util.List;
import java.util.stream.Collectors;

import java.text.SimpleDateFormat;

public class TitleDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtTitleName;
    private TextView txtGenres;
    private TextView txtViews;
    private TextView txtCreatedDate;
    private TextView txtPublishStatus;
    private RecyclerView rvChapters;
    private ChaptersAdapter chaptersAdapter;
    private ChapterViewModel chapterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_detail);

        // Initialize views
        txtTitleName = findViewById(R.id.title_detail_title_name);
        txtGenres = findViewById(R.id.title_detail_genres);
        txtViews = findViewById(R.id.title_detail_views);
        txtCreatedDate = findViewById(R.id.title_detail_created_date);
        txtPublishStatus = findViewById(R.id.title_detail_publishing_status);
        rvChapters = findViewById(R.id.title_detail_chapter_list);

        // Initialize RecyclerView
        initRv();

    }

    private void initRv() {
        rvChapters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chaptersAdapter = new ChaptersAdapter(this, null);
        chaptersAdapter.setListener(new ChaptersAdapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(Chapter chapter) {
                intentToReading(chapter);
            }
        });
        rvChapters.setAdapter(chaptersAdapter);
        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = this.getIntent();
        Title title = (Title) intent.getSerializableExtra("title");
        if (title != null) {
            loadTitleData(title);
            chapterViewModel.getChapters(title.getId()).observeForever(new Observer<List<Chapter>>() {
                @Override
                public void onChanged(List<Chapter> chapters) {
                    if (chapters != null) {
                        chaptersAdapter.setChapters(chapters);
                    }
                }
            });
        }
    }

    // Get title data from other activities
    private void loadTitleData(Title title) {
        txtTitleName.setText(title.getTitle());
        txtGenres.setText(title.getGenres().stream().map(Genre::getName).collect(Collectors.joining(",")));
        txtViews.setText(String.valueOf(title.getViews()));
        txtCreatedDate.setText(title.getUploadedDate().toString());
        txtPublishStatus.setText(PubStatus.valueOf(title.getPubStatus()).toString());
    }

    private void intentToReading(Chapter chapter) {
        Intent intent = new Intent(getApplicationContext(), ReadingActivity.class);
        intent.putExtra("chapter", chapter);
        startActivity(intent);
    }
}