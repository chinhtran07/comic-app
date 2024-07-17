package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.main.comicapp.models.Genre;
import com.main.comicapp.repositories.GenreRepository;
import com.main.comicapp.utils.FirebaseUtil;

public class GenreRepositoryImpl implements GenreRepository {

    private static GenreRepositoryImpl instance;

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

    private CollectionReference getGenreReference() {
        return FirebaseUtil.getFirestore().collection("genres");
    }
}
