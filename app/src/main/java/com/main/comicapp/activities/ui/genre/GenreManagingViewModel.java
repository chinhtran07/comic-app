package com.main.comicapp.activities.ui.genre;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GenreManagingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GenreManagingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}