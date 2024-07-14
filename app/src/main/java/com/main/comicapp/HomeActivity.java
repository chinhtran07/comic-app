package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.main.comicapp.adapters.TitlesAdapter;
import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.models.Title;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.adapters.AllTitlesAdapter;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvRecentComics;
    private TitlesAdapter adapter;
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
        initViews();
        setupRecyclerView();
        setupAllComicsClickListener();
        fetchTitlesFromFirebase("titles", Title.class);
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

    private void init() {
        rvRecentComics = findViewById(R.id.rv_recent_comics);
        rvRecentComics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recentTitles = new ArrayList<>();
        adapter = new RecentTitlesAdapter(this, recentTitles);
        rvRecentComics.setAdapter(adapter);

        titleDTO = new TitleDTOImpl();
    }

    private void setupAllComicsClickListener() {
        findViewById(R.id.tv_all_comics).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AllRecentComicActivity.class);
            startActivity(intent);
        });
    }

    private void fetchTitlesFromFirebase(String collectionName, Class<Title> type) {
        // Assuming you have a way to get the current user's ID
        String currentUserId = "76932bc4-4f87-4239-b027-b58410b25982"; // Implement this method as per your authentication logic

        // Query reading history for the current user
        Query query = FirebaseUtils.getInstance().getDb()
                .collection("reading_histories")
                .whereEqualTo("userId", currentUserId);

        FirebaseUtils.getInstance().fetchData("ReadingHistory", query, ReadingHistory.class, new FirebaseUtils.DataFetchListener<ReadingHistory>() {
            @Override
            public void onDataFetched(List<ReadingHistory> data) {
                // Extract title IDs from reading history
                List<String> titleIds = new ArrayList<>();
                for (ReadingHistory history : data) {
                    titleIds.add(history.getTitleId());
                }

                // Fetch titles based on title IDs
                List<Title> titlesData = new ArrayList<>();
                for (String documentId : titleIds) {
                    FirebaseUtils.getInstance().getDb()
                            .collection("titles")
                            .document(documentId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Title title = documentSnapshot.toObject(Title.class);
                                    if (title != null) {
                                        titlesData.add(title);
                                        if (titlesData.size() == titleIds.size()) {
                                            // All titles fetched, update UI
                                            recentTitles.clear();
                                            recentTitles.addAll(titlesData);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to fetch document
                                Toast.makeText(HomeActivity.this, "Failed to fetch title: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }

            }
        });
    }

    private void openTitleDetailActivity(Title title) {
        Intent intent = new Intent(getApplicationContext(), TitleDetailActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

