package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.Page;
import com.main.comicapp.repositories.PageRepository;
import com.main.comicapp.repositories.impl.PageRepositoryImpl;

import java.util.List;
import java.util.Map;

public class PageViewModel extends ViewModel {
    private final PageRepository pageRepository;

    public PageViewModel() {
        pageRepository = PageRepositoryImpl.getInstance();
    }

    public LiveData<List<Page>> getPages(Map<String, String> params, String chapterId) {
        return this.pageRepository.getPages(params, chapterId);
    }
}
