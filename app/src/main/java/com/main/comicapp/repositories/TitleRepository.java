package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;

import java.util.List;
import java.util.Map;

public interface TitleRepository {
    LiveData<Title> getTitle(String id);
    void addTitle(Title title);
    void updateTitle(String id, Title title);
    void deleteTitle(String id);
    LiveData<List<Title>> getTitles(Map<String, String> params);
    LiveData<List<Title>> getRecentTitles(String userId, int limit);

    LiveData<Integer> getTitleCount();
    LiveData<Integer> getTitleCountByGenre(String genreId);
    LiveData<List<Title>> getTitlesUpdatedThisMonth();
    LiveData<Integer> getTitleCountByGenreId(String genreId);

}
