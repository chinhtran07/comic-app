package com.main.comicapp.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.R;
import com.main.comicapp.adapters.ChapterAdapter;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.util.Date;

public class ManageChapterActivity extends AppCompatActivity {

    private ChapterViewModel chapterViewModel;
    private RecyclerView recyclerView;
    private ChapterAdapter adapter;
    private String titleId;
    private TextView tvNoChapters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chapter);

        titleId = getIntent().getStringExtra("title_id");

        recyclerView = findViewById(R.id.recycler_view_chapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        tvNoChapters = findViewById(R.id.tv_no_chapters);

        adapter = new ChapterAdapter(null);
        recyclerView.setAdapter(adapter);

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);

        loadChapters();

        findViewById(R.id.button_add_chapter).setOnClickListener(v -> showAddChapterDialog());

        adapter.setListener(new ChapterAdapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(Chapter chapter) {
                Intent intent = new Intent(ManageChapterActivity.this, UpdateChapterActivity.class);
                intent.putExtra("chapter_id", chapter.getId());
                startActivityForResult(intent, 1001);
            }

            @Override
            public void onUpdateClick(Chapter chapter) {
                Intent intent = new Intent(ManageChapterActivity.this, UpdateChapterActivity.class);
                intent.putExtra("chapter_id", chapter.getId());
                startActivityForResult(intent, 1001);
            }

            @Override
            public void onDeleteClick(Chapter chapter) {
                chapterViewModel.deleteChapter(chapter.getId());
                Toast.makeText(ManageChapterActivity.this, "Chapter đã được xóa", Toast.LENGTH_SHORT).show();
                loadChapters();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            loadChapters();
        }
    }

    private void loadChapters() {
        chapterViewModel.getChapterDocumentIds(titleId).observe(this, chapterIds -> {
            if (chapterIds != null && !chapterIds.isEmpty()) {
                chapterViewModel.getChaptersByIds(chapterIds).observe(ManageChapterActivity.this, chapters -> {
                    if (chapters != null && !chapters.isEmpty()) {
                        adapter.setChapters(chapters);
                        tvNoChapters.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        adapter.clearChapters();
                        tvNoChapters.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
            } else {
                adapter.clearChapters();
                tvNoChapters.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void showAddChapterDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_dialog_add_chapter, null);

        EditText etChapterNumber = dialogView.findViewById(R.id.et_chapter_number);
        EditText etChapterDescription = dialogView.findViewById(R.id.et_chapter_description);

        builder.setView(dialogView)
                .setTitle("Thêm Chapter")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String chapterNumberStr = etChapterNumber.getText().toString();
                    String chapterDescription = etChapterDescription.getText().toString();

                    if (!chapterNumberStr.isEmpty() && !chapterDescription.isEmpty()) {
                        int chapterNumber = Integer.parseInt(chapterNumberStr);
                        addChapter(chapterNumber, chapterDescription);
                    } else {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addChapter(int chapterNumber, String description) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("chapters").document().getId();
        Chapter newChapter = new Chapter();
        newChapter.setId(id);
        newChapter.setChapterNumber(chapterNumber);
        newChapter.setDescription(description);
        newChapter.setTitleId(titleId);
        newChapter.setUploadedDate(new Date());

        chapterViewModel.addChapter(newChapter).observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Chapter đã được thêm", Toast.LENGTH_SHORT).show();
                loadChapters();
            } else {
                Toast.makeText(this, "Lỗi khi thêm Chapter", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
