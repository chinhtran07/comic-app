package com.main.comicapp.activities.ui.usermanaging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserManagingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UserManagingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}