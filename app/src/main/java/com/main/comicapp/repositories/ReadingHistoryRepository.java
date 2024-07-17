package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.ReadingHistory;

import java.util.List;

public interface ReadingHistoryRepository {
    LiveData<List<ReadingHistory>> getRecentReadingTitles(String userId, int limit);
    void addReadingHistory(ReadingHistory readingHistory);
    void updateReadingHistory(String id, ReadingHistory readingHistory);
    void deleteReadingHistory(String id);
}
