package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;
import com.main.comicapp.adapters.TitlesAdapter;
import com.main.comicapp.models.Title;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class AllRecentComicActivity extends AppCompatActivity {

    private RecyclerView rvAllComics;
    private TitlesAdapter adapter;
    private List<Title> titles;
    private TextView tvClearHistory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_comic);

        initViews();
        setupRecyclerView();
        setupClearHistory();
        fetchTitlesFromFirebase("titles", Title.class);
    }

    private void initViews() {
        rvAllComics = findViewById(R.id.rv_all_comics);
        tvClearHistory = findViewById(R.id.clear_history);
    }

    private void setupRecyclerView() {
        rvAllComics.setLayoutManager(new GridLayoutManager(this, 3));
        titles = new ArrayList<>();
        adapter = new TitlesAdapter(this, titles);
        adapter.setListener(new TitlesAdapter.OnTitleClickListener() {
            @Override
            public void onTitleClick(Title title) {
                openTitleDetail(title);
            }
        });
        rvAllComics.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupClearHistory() {
        tvClearHistory.setOnClickListener(view -> {
            titles.clear();
            adapter.notifyDataSetChanged(); // Notify the adapter to update the view
        });
    }

    private <T> void fetchTitlesFromFirebase(String collectionName, Class<T> type) {
        Query query = FirebaseUtils.getInstance().getDb().collection(collectionName);
        FirebaseUtils.getInstance().fetchData(collectionName, query, type, new FirebaseUtils.DataFetchListener<T>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataFetched(List<T> data) {
                titles.clear();
                for (T item : data) {
                    titles.add((Title) item);  // Casting to Title, ensure your collection contains Titles
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void openTitleDetail(Title title) {
        Intent intent = new Intent(getApplicationContext(), TitleDetailActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
