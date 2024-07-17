package com.main.comicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.main.comicapp.R;
import com.main.comicapp.models.Title;

public class TitleDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = this.getIntent();
        Title title = (Title) intent.getSerializableExtra("title");
        if (title != null) {
//            loadTitleData(title);
        }



    }

    // Get title data from other activities
//    private void loadTitleData(Title title) {
//        FirebaseFirestore db = FirebaseUtils.getInstance().getDb();
//        title.getGenres().forEach(genre -> {
//            db.collection("genres")
//                    .whereEqualTo("id", genre.getId())
//                    .get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//
//                    })
//        });
//    }
}