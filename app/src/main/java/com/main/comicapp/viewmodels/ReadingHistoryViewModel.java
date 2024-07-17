package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.repositories.ReadingHistoryRepository;
import com.main.comicapp.repositories.impl.ReadingHistoryRepositoryImpl;

import java.util.List;

public class ReadingHistoryViewModel extends ViewModel {

    private final ReadingHistoryRepository readingHistoryRepository;

    public ReadingHistoryViewModel() {
        readingHistoryRepository = ReadingHistoryRepositoryImpl.getInstance();
    }

    public LiveData<List<ReadingHistory>> getRecentReadingTitles(String userId, int limit) {
        return readingHistoryRepository.getRecentReadingTitles(userId, limit);
    }
    public void addReadingHistory(ReadingHistory readingHistory) {
        readingHistoryRepository.addReadingHistory(readingHistory);
    }
    public void updateReadingHistory(String id, ReadingHistory readingHistory) {
        readingHistoryRepository.updateReadingHistory(id, readingHistory);
    }
    public void deleteReadingHistory(String id) {
        readingHistoryRepository.deleteReadingHistory(id);
    }
}
