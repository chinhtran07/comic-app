package com.main.comicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.main.comicapp.R;
import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;

import java.util.stream.Collectors;

public class TitleDetailActivity extends AppCompatActivity {

    private TextView txtTitleName;
    private TextView txtGenres;
    private TextView txtViews;
    private TextView txtCreatedDate;
    private TextView txtPublishStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_detail);

        txtTitleName = findViewById(R.id.title_detail_title_name);
        txtGenres = findViewById(R.id.title_detail_genres);
        txtViews = findViewById(R.id.title_detail_views);
        txtCreatedDate = findViewById(R.id.title_detail_created_date);
        txtPublishStatus = findViewById(R.id.title_detail_publishing_status);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = this.getIntent();
        Title title = (Title) intent.getSerializableExtra("title");
        if (title != null) {
            loadTitleData(title);
        }
    }

    // Get title data from other activities
    private void loadTitleData(Title title) {
        txtTitleName.setText(title.getTitle());
        txtGenres.setText(title.getGenres().stream().map(Genre::getName).collect(Collectors.joining(",")));
        txtViews.setText(String.valueOf(title.getViews()));
        txtCreatedDate.setText(title.getUploadedDate().toString());
        txtPublishStatus.setText(PubStatus.valueOf(title.getPubStatus()).toString());
    }
}