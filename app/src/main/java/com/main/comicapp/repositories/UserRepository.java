package com.main.comicapp.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface UserRepository {
    Task<QuerySnapshot> getReaderCount();
    Task<QuerySnapshot> getAdminCount();
}
