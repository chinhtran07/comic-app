package com.main.comicapp.repositories.impl;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.models.Genre;
import com.main.comicapp.repositories.GenreRepository;
import java.util.ArrayList;
import java.util.List;

public class GenreRepositoryImpl implements GenreRepository {

    private static GenreRepositoryImpl instance;

    private GenreRepositoryImpl() {}

    public static synchronized GenreRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new GenreRepositoryImpl();
        }
        return instance;
    }

    @Override
    public LiveData<Genre> getGenre(String id) {
        MutableLiveData<Genre> genreLiveData = new MutableLiveData<>();
        getGenreReference().document(id).get().addOnCompleteListener(task -> {
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
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            List<Genre> genres = new ArrayList<>();
            for (Object object : objects) {
                DocumentSnapshot document = (DocumentSnapshot) object;
                if (document.exists()) {
                    Genre genre = Genre.toObject(document.getData(), document.getId());
                    genres.add(genre);
                }
            }
            genresLiveData.setValue(genres);
        });
        return genresLiveData;
    }

    @Override
    public LiveData<List<Genre>> getAllGenres() {
        MutableLiveData<List<Genre>> genresLiveData = new MutableLiveData<>();
        getGenreReference().get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Genre> genres = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Genre genre = Genre.toObject(document.getData(), document.getId());
                    genres.add(genre);
                }
                genresLiveData.setValue(genres);
            } else {
                genresLiveData.setValue(new ArrayList<>());
            }
        });
        return genresLiveData;
    }

    @Override
    public void addGenre(Genre genre) {
        getGenreReference().add(genre).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("GenreRepository", "Genre added with ID: " + task.getResult().getId());
            } else {
                Log.e("GenreRepository", "Error adding genre", task.getException());
            }
        });
    }

    @Override
    public void updateGenre(Genre genre) {
        getGenreReference().document(genre.getId()).set(genre).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("GenreRepository", "Genre updated with ID: " + genre.getId());
            } else {
                Log.e("GenreRepository", "Error updating genre", task.getException());
            }
        });
    }

    @Override
    public void deleteGenre(String genreId) {
        getGenreReference().document(genreId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("GenreRepository", "Genre deleted with ID: " + genreId);
            } else {
                Log.e("GenreRepository", "Error deleting genre", task.getException());
            }
        });
    }

    private CollectionReference getGenreReference() {
        return FirebaseFirestore.getInstance().collection("genres");
    }
}
