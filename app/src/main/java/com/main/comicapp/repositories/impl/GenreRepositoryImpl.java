package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Genre;
import com.main.comicapp.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenreRepositoryImpl implements GenreRepository {

    private static GenreRepositoryImpl instance;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private GenreRepositoryImpl() {

    }

    public static synchronized GenreRepositoryImpl getInstance() {
        if (instance == null)
            instance = new GenreRepositoryImpl();
        return instance;
    }

    @Override
    public LiveData<Genre> getGenre(String id) {
        MutableLiveData<Genre> genreLiveData = new MutableLiveData<>();
        getGenreReference().document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Genre genre = document.toObject(Genre.class);
                                genreLiveData.setValue(genre);
                            } else {
                                genreLiveData.setValue(null);
                            }
                        } else {
                            genreLiveData.setValue(null);
                        }
                    }
                });
        return genreLiveData;
    }

    @Override
    public LiveData<List<Genre>> getGenres(List<String> genreIds) {
        MutableLiveData<List<Genre>> genresLiveData = new MutableLiveData<>();
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String genreId : genreIds) {
            tasks.add(getGenreReference().document(genreId).get());
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                List<Genre> genres = new ArrayList<>();
                for (Object object : objects) {
                    DocumentSnapshot document = (DocumentSnapshot) object;
                    if (document.exists()) {
                        Genre genre = Genre.toObject(document.getData(), document.getId());
                        genres.add(genre);
                    }
                }
                genresLiveData.setValue(genres);
            }
        });
        return genresLiveData;
    }

    private CollectionReference getGenreReference() {
        return FirebaseFirestore.getInstance().collection("genres");
    }

    @Override
    public Task<QuerySnapshot> getAllGenres(){
        return db.collection("genres").get();
    }
}
