package com.main.comicapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.main.comicapp.R;
import com.main.comicapp.models.Title;

import java.text.SimpleDateFormat;

public class TitleDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtTitleName;
    private TextView txtGenres;
    private TextView txtViews;
    private TextView txtCreatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_detail);

        txtTitleName = findViewById(R.id.title_detail_title_name);
        txtGenres = findViewById(R.id.title_detail_genres);
        txtViews = findViewById(R.id.title_detail_views);
        txtCreatedDate = findViewById(R.id.title_detail_created_date);

    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = this.getIntent();
        Title title = (Title) intent.getSerializableExtra("title");
        if (title != null) {
            txtTitleName.setText(title.getTitle());
            txtViews.setText(String.format("%d", title.getViews()));
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            txtCreatedDate.setText(format.format(title.getUploadedDate()));
            StringBuilder genres = new StringBuilder();
            title.getGenres().forEach(g -> {
                genres.append(String.format("%s ,", g.getName()));
            });
            txtGenres.setText(genres);
        }

    }
}