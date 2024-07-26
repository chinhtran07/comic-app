package com.main.comicapp.repositories.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.UserSession;
import com.main.comicapp.repositories.UserSessionRepository;

public class UserSessionRepositoryImpl implements UserSessionRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Task<DocumentSnapshot> getUserSession(String id) {
        return db.collection("user_sessions").document(id).get();
    }

    @Override
    public Task<Void> deleteUserSession(String id) {
        return db.collection("user_sessions").document(id).delete();
    }

    @Override
    public Task<Void> createUserSession(UserSession userSession) {
        return db.collection("user_sessions").document(userSession.getId()).set(UserSession.toMap(userSession));
    }
}
