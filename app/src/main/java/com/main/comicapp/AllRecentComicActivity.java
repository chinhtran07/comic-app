package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.adapters.AllComicsAdapter;
import com.main.comicapp.adapters.RecentComicsAdapter;
import com.main.comicapp.models.Title;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class AllRecentComicActivity extends AppCompatActivity implements RecentComicsAdapter.OnComicClickListener{

    private RecyclerView rvAllComics;
    private AllComicsAdapter adapter;
    private List<Title> titles;
    private TextView tvClearHistory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_comic);

        rvAllComics = findViewById(R.id.rv_all_comics);
        rvAllComics.setLayoutManager(new GridLayoutManager(this, 3));

        titles = new ArrayList<>();

        adapter = new AllComicsAdapter(this, titles);
        rvAllComics.setAdapter(adapter);

        tvClearHistory = findViewById(R.id.clear_history);

        tvClearHistory.setOnClickListener(view -> {
            titles.clear();
        });

        fetchComicsFromFirebase("titles", Title.class);

    }

    private <T> void fetchComicsFromFirebase(String collectionName, Class<T> type) {
        FirebaseUtils.getInstance().fetchData(collectionName, type, new FirebaseUtils.DataFetchListener<T>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataFetched(List<T> data) {
                titles.clear();
                for (T item : data) {
                    titles.add((Title) item);  // Casting to Comic, ensure your collection contains Comics
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onComicClick(Title title) {
        Intent intent = new Intent(this, ReadingActivity.class);
        startActivity(intent);
    }
}