package com.main.comicapp.repositories.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Task<QuerySnapshot> getAllUser() {
        return db.collection("users")
                .whereEqualTo("userRole", "USER")
                .get();
    }

    @Override
    public Task<QuerySnapshot> getAllAdmin() {
        return db.collection("users")
                .whereEqualTo("userRole", "ADMIN")
                .get();
    }

    @Override
    public Task<DocumentSnapshot> getUserById(String userId) {
        return db.collection("users").document(userId).get();
    }

    @Override
    public Task<QuerySnapshot> getUserByEmail(String userEmail) {
        return db.collection("users")
                .whereEqualTo("email", userEmail)
                .get();
    }

    @Override
    public Task<QuerySnapshot> fetchUserByUsername(String username) {
        return db.collection("users")
                .whereEqualTo("username", username)
                .get();
    }

    @Override
    public Task<Void> save(Map<String, Object> userData, String userId) {
        return db.collection("users").document(userId).set(userData);
    }

    @Override
    public Task<Void> updateUserStatus(String userId) {
        DocumentReference userDocRef = db.collection("users").document(userId);

        // Fetch the current 'isActive' status
        return userDocRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Boolean isActive = document.getBoolean("isActive");
                    if (isActive != null) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("isActive", !isActive);
                        return userDocRef.update(updates);
                    } else {
                        throw new RuntimeException("Field 'isActive' is missing or null.");
                    }
                } else {
                    throw new RuntimeException("Document does not exist.");
                }
            } else {
                throw new RuntimeException("Failed to fetch the document.", task.getException());
            }
        });
    }
}