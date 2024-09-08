package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.main.comicapp.models.Page;

import java.util.List;
import java.util.Map;

public interface PageRepository {
    LiveData<List<Page>> getPages(Map<String, String> params, String chapterId);
    Task<Void> addPage(Page page);
    void deletePage(String pageId);
}
