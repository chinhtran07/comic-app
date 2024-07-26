package com.main.comicapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.main.comicapp.models.User;
import com.main.comicapp.models.UserSession;
import com.main.comicapp.repositories.UserSessionRepository;
import com.main.comicapp.repositories.impl.UserSessionRepositoryImpl;


public class UserSessionViewModel extends ViewModel {
    private static final String TAG = "com.main.comicapp.viewmodels.UserSessionViewModel";
    private final UserSessionRepository userSessionRepository;
    MutableLiveData<UserSession> currentUserSession = new MutableLiveData<>();

    public UserSessionViewModel() {
        userSessionRepository = new UserSessionRepositoryImpl();
    }

    public void fetchUserSession(String id) {
        userSessionRepository.getUserSession(id).addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                UserSession userSession = querySnapshot.getDocuments().get(0).toObject(UserSession.class);
                currentUserSession.setValue(userSession);
                } else {
                currentUserSession.setValue(null);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "getUserSession: Failed", e.getCause());
        });
    }

    public LiveData<UserSession> getCurrentUserSession() {
        return currentUserSession;
    }

    public void createUserSession(UserSession userSession) {
        userSessionRepository.createUserSession(userSession).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: Created Successfulle");
            }
        });
    }

    public void deleteUserSession(String id) {
        userSessionRepository.deleteUserSession(id);
    }
}
