package com.main.comicapp.activities;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chapter);

        recyclerView = findViewById(R.id.recycler_view_chapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ChapterAdapter(null);
        recyclerView.setAdapter(adapter);

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
        chapterViewModel.getAllChapters().observe(this, new Observer<List<Chapter>>() {
            @Override
            public void onChanged(List<Chapter> chapters) {
                adapter.setChapters(chapters);
            }
        });

        findViewById(R.id.button_add_chapter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở Activity để thêm chương mới
                Intent intent = new Intent(ManageChapterActivity.this, AddChapterActivity.class);
                startActivity(intent);
            }
        });

        adapter.setListener(new ChapterAdapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(Chapter chapter) {
                // Xử lý khi click vào chương (có thể mở chi tiết chương)
            }

            @Override
            public void onUpdateClick(Chapter chapter) {
                // Mở Activity để cập nhật chương
                Intent intent = new Intent(ManageChapterActivity.this, UpdateChapterActivity.class);
                intent.putExtra("chapter_id", chapter.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Chapter chapter) {
                // Xóa chương
                chapterViewModel.deleteChapter(chapter.getId());
                Toast.makeText(ManageChapterActivity.this, "Chapter deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
