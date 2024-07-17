package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.Genre;
import com.main.comicapp.repositories.GenreRepository;
import com.main.comicapp.repositories.impl.GenreRepositoryImpl;

public class GenreViewModel {
    private final GenreRepository genreRepository;

    public GenreViewModel() {
        genreRepository = GenreRepositoryImpl.getInstance();
    }

    public LiveData<Genre> getGenre(String id) {
        return genreRepository.getGenre(id);
    }
}
