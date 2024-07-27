package com.main.comicapp.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.main.comicapp.models.User;
import com.main.comicapp.models.UserSession;
import com.main.comicapp.repositories.UserSessionRepository;
import com.main.comicapp.repositories.impl.UserSessionRepositoryImpl;

import java.util.Map;


public class UserSessionViewModel extends ViewModel {
    private static final String TAG = "com.main.comicapp.viewmodels.UserSessionViewModel";
    private final UserSessionRepository userSessionRepository;

    private final MutableLiveData<UserSession> currentUserSession = new MutableLiveData<>();

    public UserSessionViewModel() {
        userSessionRepository = new UserSessionRepositoryImpl();
    }

    public void fetchUserSession(String id) {
        userSessionRepository.getUserSession(id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        UserSession userSession = UserSession.toObject(data, document.getId());
                        currentUserSession.setValue(userSession);
                    }
                    else {
                        currentUserSession.setValue(null);
                    }
                }
            }
        });

    }

    public LiveData<UserSession> getCurrentUserSession() {
        return currentUserSession;
    }

    public void createUserSession(UserSession userSession) {
        userSessionRepository.createUserSession(userSession);
    }

    public void deleteUserSession(String id) {
        userSessionRepository.deleteUserSession(id);
    }
}
