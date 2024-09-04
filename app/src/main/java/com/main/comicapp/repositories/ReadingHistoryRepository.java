package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.ReadingHistory;

import java.util.Date;
import java.util.List;

public interface ReadingHistoryRepository {
    LiveData<List<ReadingHistory>> getRecentReadingTitles(String userId, int limit);
    void addReadingHistory(ReadingHistory readingHistory);
    void updateReadingHistory(String id, ReadingHistory readingHistory);
    void deleteReadingHistory(String id);
    void addHistory(String userId, String titleId);
    LiveData<List<ReadingHistory>> getAllReadingHistoriesByUserId(String userId);
    void deleteAllHistoriesByUserId(String userId);
}
