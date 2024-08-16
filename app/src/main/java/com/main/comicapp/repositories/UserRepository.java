package com.main.comicapp.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public interface UserRepository {
    Task<QuerySnapshot> getAllUser();
    Task<QuerySnapshot> getAllAdmin();
    Task<DocumentSnapshot> getUserById(String userId);
    Task<QuerySnapshot> getUserByEmail(String userEmail);
    Task<QuerySnapshot> fetchUserByUsername(String username);
    Task<Void> save(Map<String, Object> userData, String userId);
    Task<Void> updateUserStatus(String userId);
    Task<Void> updateUserRole(String id);
}