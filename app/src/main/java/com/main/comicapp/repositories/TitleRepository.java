package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;

import java.util.List;
import java.util.Map;

public interface TitleRepository {
    LiveData<Title> getTitle(String id);
    Task<DocumentReference> addTitle(Title title);
    void updateTitle(String id, Title title);
    void deleteTitle(String id);
    LiveData<List<Title>> getTitles(Map<String, String> params);
    LiveData<List<Title>> getRecentTitles(String userId, int limit);

    LiveData<Integer> getTitleCount();
    LiveData<Integer> getTitleCountByGenre(String genreId);
    LiveData<List<Title>> getTitlesUpdatedThisMonth();
    LiveData<Integer> getTitleCountByGenreId(String genreId);

}
