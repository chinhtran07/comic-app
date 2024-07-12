package com.main.comicapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Title;
import com.main.comicapp.utils.FirebaseUtils;

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