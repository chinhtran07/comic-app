package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.repositories.UserRepository;
import com.main.comicapp.repositories.impl.UserRepositoryImpl;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Integer> readerCountLiveData;

    public UserViewModel() {
        userRepository = new UserRepositoryImpl();
        readerCountLiveData = new MutableLiveData<>();
    }

    public LiveData<Integer> getReaderCountLiveData() {
        return readerCountLiveData;
    }

    public void fetchReaderCount() {
        userRepository.getReaderCount().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                int count = task.getResult().size();
                readerCountLiveData.setValue(count);
            } else {
                readerCountLiveData.setValue(0);
            }
        });
    }
}
