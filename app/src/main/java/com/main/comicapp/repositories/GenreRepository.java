package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Genre;

import java.util.List;

public interface GenreRepository {
    LiveData<Genre> getGenre(String id);
    LiveData<List<Genre>> getGenres(List<String> genreIds);
    Task<QuerySnapshot> getAllGenres();
}
