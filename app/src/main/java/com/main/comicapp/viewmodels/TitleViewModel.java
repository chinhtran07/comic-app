package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
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

    public void addTitle(Title title) {
        titleRepository.addTitle(title);
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
}
