package com.main.comicapp.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.UserSession;

public interface UserSessionRepository {
    Task<QuerySnapshot> getUserSession(String id);
    Task<Void> deleteUserSession(String id);
    Task<Void> createUserSession(UserSession userSession);
}
