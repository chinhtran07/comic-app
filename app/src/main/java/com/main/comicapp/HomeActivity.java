package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.adapters.TitlesAdapter;
import com.main.comicapp.models.Title;
import com.google.firebase.firestore.Query;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvRecentComics;
    private TitlesAdapter adapter;
    private List<Title> recentTitles;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView();
        setupAllComicsClickListener();
        fetchTitlesFromFirebase("titles", Title.class);
    }

    private void initViews() {
        rvRecentComics = findViewById(R.id.rv_recent_comics);
    }

    private void setupRecyclerView() {
        rvRecentComics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recentTitles = new ArrayList<>();
        adapter = new TitlesAdapter(this, recentTitles);
        adapter.setListener(new TitlesAdapter.OnTitleClickListener() {
            @Override
            public void onTitleClick(Title title) {
                openTitleDetailActivity(title);
            }
        });
        rvRecentComics.setAdapter(adapter);
    }

    private void setupAllComicsClickListener() {
        findViewById(R.id.tv_all_comics).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AllRecentComicActivity.class);
            startActivity(intent);
        });
    }

    private <T> void fetchTitlesFromFirebase(String collectionName, Class<T> type) {
        Query query = FirebaseUtils.getInstance().getDb().collection(collectionName);
        FirebaseUtils.getInstance().fetchData(collectionName, query, type, new FirebaseUtils.DataFetchListener<T>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataFetched(List<T> data) {
                recentTitles.clear();
                for (T item : data) {
                    recentTitles.add((Title) item);  // Casting to Title, ensure your collection contains Titles
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void openTitleDetailActivity(Title title) {
        Intent intent = new Intent(getApplicationContext(), TitleDetailActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
