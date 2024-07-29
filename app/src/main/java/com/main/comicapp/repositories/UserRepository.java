package com.main.comicapp.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface UserRepository {
    Task<QuerySnapshot> getReaderCount();
    Task<QuerySnapshot> getAdminCount();
    Task<DocumentSnapshot> getUserById(String userId);
    Task<QuerySnapshot> getUserByEmail(String userEmail);
    Task<QuerySnapshot> fetchUserByUsername(String username); // Thêm phương thức này
}
