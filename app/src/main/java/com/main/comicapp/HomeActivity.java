package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.adapters.AllComicsAdapter;
import com.main.comicapp.adapters.RecentComicsAdapter;
import com.main.comicapp.models.Comic;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvRecentComics;
    private RecentComicsAdapter adapter;
    private List<Comic> recentComics;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        fetchComicsFromFirebase("titles", Comic.class);

        findViewById(R.id.tv_all_comics).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AllRecentComicActivity.class);
            startActivity(intent);
        });
    }

    private void init() {
        rvRecentComics = findViewById(R.id.rv_recent_comics);
        rvRecentComics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recentComics = new ArrayList<>();
        adapter = new RecentComicsAdapter(this, recentComics);
        rvRecentComics.setAdapter(adapter);
    }

    private <T> void fetchComicsFromFirebase(String collectionName, Class<T> type) {
        FirebaseUtils.getInstance().fetchData(collectionName, type, new FirebaseUtils.DataFetchListener<T>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataFetched(List<T> data) {
                recentComics.clear();
                for (T item : data) {
                    recentComics.add((Comic) item);  // Casting to Comic, ensure your collection contains Comics
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}