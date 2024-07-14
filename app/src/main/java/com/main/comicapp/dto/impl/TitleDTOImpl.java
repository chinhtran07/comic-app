package com.main.comicapp.dto.impl;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.dto.TitleDTO;
import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.enums.TitleFormat;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;
import com.main.comicapp.utils.FirebaseUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TitleDTOImpl implements TitleDTO{

    private static final String TAG = "com.main.comicapp.dto.impl.TitleDTOImpl";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getAllTitleRealtime(FirebaseUtils.DataFetchListener<Title> listener)  {
        List<Title> titles = new ArrayList<>();

        db.collection("titles")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "onEvent: Listen failed", error);
                    return;
                }
                assert value != null;
                for (DocumentSnapshot document: value.getDocuments()) {
                    Title title = new Title();
                    title.setId(document.getId());
                    Map<String, Object> data = document.getData();
                    assert data != null;
                    if (validateTitle(data)) {
                        title.setName((String)data.get("name"));
                        title.setCover((String)data.get("cover"));
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            Date uploadedDate = formatter.parse((String)data.get("uploadedDate"));
                            title.setUploadedDate(uploadedDate);
                        } catch (ParseException e) {
                            Log.e("Error", "onEvent: Parse Exception", e);
                        }
                        title.setPubStatus(PubStatus.valueOf((String)data.get("pubStatus")));
                        title.setTitleFormat(TitleFormat.valueOf((String)data.get("titleFormat")));
                        List<Genre> genres = new ArrayList<>();
                        //noinspection unchecked
                        List<String> genreIds = (List<String>) data.get("genres");
                        assert genreIds != null;
                        for (String id : genreIds) {
                            Genre genre = new Genre();
                            genre.setId(id);
                            db.collection("genres").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    genre.setName((String)documentSnapshot.get("name"));
                                    genres.add(genre);
                                }
                            });
                        }
                        title.setGenres(genres);
                        titles.add(title);
                    }
                }

                listener.onDataFetched(titles);
            }
        });
    }

    @Override
    public Title getTitle(String id) {
        return null;
    }

    private boolean validateTitle(Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() == null) return false;
        }
        return true;
    }
}
