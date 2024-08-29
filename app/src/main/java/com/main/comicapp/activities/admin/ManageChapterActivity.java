package com.main.comicapp.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.ChapterAdapter;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.util.List;

public class ManageChapterActivity extends AppCompatActivity {

    private ChapterViewModel chapterViewModel;
    private RecyclerView recyclerView;
    private ChapterAdapter adapter;
    private String titleId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chapter);

        titleId = getIntent().getStringExtra("title_id");

        recyclerView = findViewById(R.id.recycler_view_chapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ChapterAdapter(null);
        recyclerView.setAdapter(adapter);

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);

        chapterViewModel.getChapterDocumentIds(titleId).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> chapterIds) {
                if (chapterIds != null && !chapterIds.isEmpty()) {
                    chapterViewModel.getChaptersByIds(chapterIds).observe(ManageChapterActivity.this, new Observer<List<Chapter>>() {
                        @Override
                        public void onChanged(List<Chapter> chapters) {
                            adapter.setChapters(chapters);
                        }
                    });
                } else {
                    adapter.clearChapters();
                }
            }
        });


        findViewById(R.id.button_add_chapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageChapterActivity.this, AddChapterActivity.class);
                intent.putExtra("title_id", titleId);
                startActivity(intent);
            }
        });

        adapter.setListener(new ChapterAdapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(Chapter chapter) {
            }

            @Override
            public void onUpdateClick(Chapter chapter) {
                Intent intent = new Intent(ManageChapterActivity.this, UpdateChapterActivity.class);
                intent.putExtra("chapter_id", chapter.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Chapter chapter) {
                chapterViewModel.deleteChapter(chapter.getId());
                Toast.makeText(ManageChapterActivity.this, "Chapter deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
