package com.main.comicapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.adapters.TitlesAdapter;
import com.main.comicapp.models.ReadingHistory;
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
}

