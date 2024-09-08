package com.main.comicapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.Page;
import com.main.comicapp.models.Title;
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

    public void addPage(Page page) {
        pageRepository.addPage(page)
                .addOnSuccessListener(documentReference -> {})
                .addOnFailureListener(e -> {});
    }

    public void deletePage(String pageId) {
        pageRepository.deletePage(pageId);
    }
}
