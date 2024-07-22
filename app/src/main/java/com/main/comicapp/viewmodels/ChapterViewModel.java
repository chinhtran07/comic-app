package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.Chapter;
import com.main.comicapp.repositories.ChapterRepository;
import com.main.comicapp.repositories.impl.ChapterRepositoryImpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChapterViewModel extends ViewModel {
    private final ChapterRepository chapterRepository;

    public ChapterViewModel() {
        this.chapterRepository = ChapterRepositoryImpl.getInstance();
    }


    public LiveData<Chapter> getChapter(String id) {
        return chapterRepository.getChapter(id);
    }

    public void addChapter(Chapter chapter) {
        chapterRepository.addChapter(chapter);
    }

    public void updateChapter(String id, Chapter chapter) {
        chapterRepository.updateChapter(id, chapter);
    }

    public void deleteChapter(String id) {
        chapterRepository.deleteChapter(id);
    }

    public LiveData<List<Chapter>> getChapters(String titleId) {
        return chapterRepository.getChapters(titleId);
    }

    public CompletableFuture<List<String>> getChapterDocumentIds(String titleId) {
        return chapterRepository.getChapterDocumentIds(titleId);
    }
}
