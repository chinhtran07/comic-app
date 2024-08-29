package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.Title;
import com.main.comicapp.repositories.TitleRepository;
import com.main.comicapp.repositories.impl.TitleRepositoryImpl;

import java.util.List;
import java.util.Map;

public class TitleViewModel extends ViewModel {
    private final TitleRepository titleRepository;

    public TitleViewModel() {
        titleRepository = TitleRepositoryImpl.getInstance();
    }

    public LiveData<Title> getTitle(String id) {
        return titleRepository.getTitle(id);
    }

    public LiveData<List<Title>> getTitles(Map<String, String> params) {
        return titleRepository.getTitles(params);
    }

    public LiveData<Boolean> addTitle(Title title) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        titleRepository.addTitle(title)
                .addOnSuccessListener(documentReference -> success.setValue(true))
                .addOnFailureListener(e -> success.setValue(false));
        return success;
    }

    public void updateTitle(String id, Title title) {
        titleRepository.updateTitle(id, title);
    }

    public void deleteTitle(String id) {
        titleRepository.deleteTitle(id);
    }

    public LiveData<List<Title>> getRecentReadingTitles(String userId, int limit) {
        return titleRepository.getRecentTitles(userId, limit);
    }

    public LiveData<Integer> getTitleCount() {
        return titleRepository.getTitleCount();
    }

    public LiveData<Integer> getTitleCountByGenre(String genreId) {
        return titleRepository.getTitleCountByGenre(genreId);
    }

    public LiveData<List<Title>> getTitlesUpdatedThisMonth() {
        return titleRepository.getTitlesUpdatedThisMonth();
    }

    public LiveData<Integer> getTitleCountByGenreId(String genreId) {
        return titleRepository.getTitleCountByGenreId(genreId);
    }
}
