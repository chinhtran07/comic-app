package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.Genre;
import com.main.comicapp.repositories.GenreRepository;
import com.main.comicapp.repositories.impl.GenreRepositoryImpl;

import java.util.List;

public class GenreViewModel extends ViewModel {
    private final GenreRepository genreRepository;

    public GenreViewModel() {
        genreRepository = GenreRepositoryImpl.getInstance();
    }

    public LiveData<Genre> getGenre(String id) {
        return genreRepository.getGenre(id);
    }

    public LiveData<List<Genre>> getGenres(List<String> genreIds) {
        return genreRepository.getGenres(genreIds);
    }

    public LiveData<List<Genre>> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public void addGenre(Genre genre) {
        genreRepository.addGenre(genre);
    }

    public void deleteGenre(String genreId) {
        genreRepository.deleteGenre(genreId);
    }

    public void updateGenre(Genre genre) {
        genreRepository.updateGenre(genre);
    }
}
