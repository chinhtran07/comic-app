package com.main.comicapp.repositories.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.repositories.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Task<QuerySnapshot> getReaderCount() {
        return db.collection("users")
                .whereEqualTo("userRole", "USER")
                .get();
    }

    @Override
    public Task<QuerySnapshot> getAdminCount() {
        return db.collection("users")
                .whereEqualTo("userRole", "ADMIN")
                .get();
    }

    @Override
    public Task<DocumentSnapshot> getUserById(String userId) {
        return db.collection("users").document(userId).get();
    }
}
