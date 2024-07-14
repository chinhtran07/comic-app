package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.main.comicapp.adapters.RecentTitlesAdapter;
import com.main.comicapp.dto.TitleDTO;
import com.main.comicapp.dto.impl.TitleDTOImpl;
import com.main.comicapp.models.Title;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.adapters.AllTitlesAdapter;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvRecentComics;
    private RecentTitlesAdapter adapter;
    private List<Title> recentTitles;
    private TitleDTO titleDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        findViewById(R.id.tv_all_comics).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AllRecentComicActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        titleDTO.getAllTitleRealtime(new FirebaseUtils.DataFetchListener<Title>() {
            @Override
            public void onDataFetched(List<Title> data) {
                recentTitles.clear();
                for (int i = 0;i < data.size(); i++) {
                    recentTitles.add(data.get(i));
                    adapter.notifyItemChanged(i);
                }
            }
        });
    }

    private void init() {
        rvRecentComics = findViewById(R.id.rv_recent_comics);
        rvRecentComics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recentTitles = new ArrayList<>();
        adapter = new RecentTitlesAdapter(this, recentTitles);
        rvRecentComics.setAdapter(adapter);

        titleDTO = new TitleDTOImpl();
    }


    private <T> void fetchComicsFromFirebase(String collectionName, Class<T> type) {
        FirebaseUtils.getInstance().fetchData(collectionName, type, new FirebaseUtils.DataFetchListener<T>() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}