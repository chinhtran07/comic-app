package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.Page;

import java.util.List;
import java.util.Map;

public interface PageRepository {
    LiveData<List<Page>> getPages(Map<String, String> params, String chapterId);
}
