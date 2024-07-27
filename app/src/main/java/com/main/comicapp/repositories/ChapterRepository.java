package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;
import com.main.comicapp.models.Chapter;
import java.util.List;

public interface ChapterRepository {
    LiveData<Chapter> getChapter(String id);
    void addChapter(Chapter chapter);
    void updateChapter(String id, Chapter chapter);
    void deleteChapter(String id);
    LiveData<List<Chapter>> getChapters(String titleId);
    LiveData<List<String>> getChapterDocumentIds(String titleId);
    LiveData<List<Chapter>> getAllChapters();
}
