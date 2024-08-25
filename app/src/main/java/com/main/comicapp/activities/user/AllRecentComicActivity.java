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
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.TitleViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllRecentComicActivity extends BaseActivity {

    private RecyclerView rvAllComics;
    private TitleAdapter adapter;
    private TitleViewModel titleViewModel;
    private TextView tvClearHistory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_comic);

        init();

        fetchData();

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
    }

    private void fetchData() {
        Map<String, String> params = new HashMap<>();
        titleViewModel.getTitles(params).observe(this, new Observer<List<Title>>() {
            @Override
            public void onChanged(List<Title> titles) {
                if (titles != null) {
                    adapter.setTitles(titles);
                } else {
                    Toast.makeText(AllRecentComicActivity.this, "No comics found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearHistory() {
        tvClearHistory.setOnClickListener(view -> {
            adapter.clearTitles();
        });
    }

    private void openTitleDetail(Title title) {
        Intent intent = new Intent(getApplicationContext(), TitleDetailActivity.class);
        intent.putExtra("titleId", title.getId());
        startActivity(intent);
    }
}
