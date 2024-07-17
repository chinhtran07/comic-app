package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.Genre;

public interface GenreRepository {
    LiveData<Genre> getGenre(String id);
}
