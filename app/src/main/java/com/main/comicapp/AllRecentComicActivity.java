package com.main.comicapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.adapters.AllComicsAdapter;
import com.main.comicapp.models.Comic;

import java.util.ArrayList;
import java.util.List;

public class AllRecentComicActivity extends AppCompatActivity {

    private RecyclerView rvAllComics;
    private AllComicsAdapter adapter;
    private List<Comic> comics;
    private TextView tvClearHistory;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_comic);

        rvAllComics = findViewById(R.id.rv_all_comics);
        rvAllComics.setLayoutManager(new GridLayoutManager(this, 3));

        comics = new ArrayList<>();

        comics.add(new Comic("Comic Title 1", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        comics.add(new Comic("Comic Title 2", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        comics.add(new Comic("Comic Title 3", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        comics.add(new Comic("Comic Title 4", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        comics.add(new Comic("Comic Title 5", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));
        comics.add(new Comic("Comic Title 6", "https://res.cloudinary.com/dk4uoxtsx/image/upload/v1714275085/bisui8vnhfwwzgunpdlo.jpg"));

        adapter = new AllComicsAdapter(this, comics);
        rvAllComics.setAdapter(adapter);

        tvClearHistory = findViewById(R.id.clear_history);

        tvClearHistory.setOnClickListener(view -> {
            comics.clear();
        });

    }
}