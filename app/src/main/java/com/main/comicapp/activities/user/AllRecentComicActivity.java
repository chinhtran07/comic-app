package com.main.comicapp.activities.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.activities.BaseActivity;
import com.main.comicapp.adapters.TitleAdapter;
import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.ReadingHistoryViewModel;
import com.main.comicapp.viewmodels.TitleViewModel;

import java.util.List;

public class AllRecentComicActivity extends BaseActivity {

    private RecyclerView rvAllComics;
    private TitleAdapter adapter;
    private TitleViewModel titleViewModel;
    private ReadingHistoryViewModel readingHistoryViewModel;
    private TextView tvClearHistory;
    private String userId; // Lưu trữ userId nhận được từ Intent

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_comic);

        // Nhận userId từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("USER_ID");
        }

        init();

        if (userId != null) {
            fetchUserReadingHistory(userId); // Lấy lịch sử đọc dựa trên userId
        } else {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show();
        }

        clearHistory();
    }

    private void init() {
        tvClearHistory = findViewById(R.id.clear_history);
        rvAllComics = findViewById(R.id.rv_all_comics);
        rvAllComics.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new TitleAdapter(this, null);
        adapter.setListener(new TitleAdapter.OnTitleClickListener() {
            @Override
            public void onTitleClick(Title title) {
                openTitleDetail(title);
            }
        });
        rvAllComics.setAdapter(adapter);

        titleViewModel = new ViewModelProvider(this).get(TitleViewModel.class);
        readingHistoryViewModel = new ViewModelProvider(this).get(ReadingHistoryViewModel.class);
    }

    private void fetchUserReadingHistory(String userId) {
        // Gọi ViewModel để lấy lịch sử đọc của người dùng
        readingHistoryViewModel.getAllReadingHistoriesByUserId(userId).observe(this, new Observer<List<ReadingHistory>>() {
            @Override
            public void onChanged(List<ReadingHistory> readingHistories) {
                if (readingHistories != null && !readingHistories.isEmpty()) {
                    // Lấy danh sách title từ lịch sử đọc
                    for (ReadingHistory history : readingHistories) {
                        titleViewModel.getTitleById(history.getTitleId()).observe(AllRecentComicActivity.this, new Observer<Title>() {
                            @Override
                            public void onChanged(Title title) {
                                if (title != null) {
                                    adapter.addTitle(title); // Thêm truyện vào adapter
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(AllRecentComicActivity.this, "No reading history found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearHistory() {
        tvClearHistory.setOnClickListener(view -> {
            if (userId != null) {
                readingHistoryViewModel.deleteAllHistoriesByUserId(userId);
                adapter.clearTitles(); // Xóa truyện hiển thị trong adapter
                Toast.makeText(AllRecentComicActivity.this, "All history cleared.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openTitleDetail(Title title) {
        Intent intent = new Intent(getApplicationContext(), TitleDetailActivity.class);
        intent.putExtra("titleId", title.getId());
        startActivity(intent);
    }
}
