package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.Genre;

import java.util.List;

public interface GenreRepository {
    LiveData<Genre> getGenre(String id);
    LiveData<List<Genre>> getGenres(List<String> genreIds);
}
