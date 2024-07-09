package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.adapters.RecentTitlesAdapter;
import com.main.comicapp.models.Title;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.adapters.AllComicsAdapter;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvRecentComics;
    private RecentTitlesAdapter adapter;
    private List<Title> recentTitles;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        fetchComicsFromFirebase("titles", Title.class);

        findViewById(R.id.tv_all_comics).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AllRecentComicActivity.class);
            startActivity(intent);
        });
    }

    private void init() {
        rvRecentComics = findViewById(R.id.rv_recent_comics);
        rvRecentComics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recentTitles = new ArrayList<>();
        adapter = new RecentTitlesAdapter(this, recentTitles);
        rvRecentComics.setAdapter(adapter);
    }

    private <T> void fetchComicsFromFirebase(String collectionName, Class<T> type) {
        Query query = FirebaseUtils.getInstance().getDb().collection(collectionName);
        FirebaseUtils.getInstance().fetchData(collectionName, query, type, new FirebaseUtils.DataFetchListener<T>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataFetched(List<T> data) {
                recentTitles.clear();
                for (T item : data) {
                    recentTitles.add((Title) item);  // Casting to Title, ensure your collection contains Comics
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}