package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.User;

public interface UserRepository {
    Task<QuerySnapshot> getReaderCount();
    Task<QuerySnapshot> getAdminCount();
    Task<DocumentSnapshot> getUserById(String userId);
    Task<QuerySnapshot> getUserByEmail(String userEmail);
}
