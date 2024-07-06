package com.main.comicapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.adapters.RecentComicsAdapter;
import com.main.comicapp.models.Comic;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvRecentComics;
    private RecentComicsAdapter adapter;
    private List<Comic> recentComics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvRecentComics = findViewById(R.id.rv_recent_comics);
        rvRecentComics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recentComics = new ArrayList<>();

        recentComics.add(new Comic("Comic Title 1", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        recentComics.add(new Comic("Comic Title 2", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        recentComics.add(new Comic("Comic Title 3", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        recentComics.add(new Comic("Comic Title 4", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        recentComics.add(new Comic("Comic Title 5", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        recentComics.add(new Comic("Comic Title 6", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));

        adapter = new RecentComicsAdapter(this, recentComics);
        rvRecentComics.setAdapter(adapter);

        findViewById(R.id.tv_all_comics).setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AllRecentComicActivity.class);
            startActivity(intent);
        });
    }

}