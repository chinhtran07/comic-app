package com.main.comicapp.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;


public class FirebaseUtils {

    private static FirebaseUtils instance;
    private FirebaseFirestore db;

    private FirebaseUtils() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirebaseUtils getInstance() {
        if (instance == null) {
            instance = new FirebaseUtils();
        }
        return instance;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public interface DataFetchListener<T> {
        void onDataFetched(List<T> data);
    }

    public <T> ListenerRegistration fetchData(String collectionName, Query query, final Class<T> type, final DataFetchListener<T> listener) {

        // Use of query path validation not needed in the context of using a single database.
        return query.addSnapshotListener((value, error) -> {
            if (error != null) {
                String errorMessage = "Error fetching data from " + collectionName + ": " + (error.getMessage() != null ? error.getMessage() : "Unknown error");
                Log.e("FirebaseDataManager", errorMessage);
                return;
            }

            List<T> dataList = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot document : value) {
                    Log.d("Firebase", "Document Data: " + document.getData());
                    try {
                        T item = document.toObject(type);
                        dataList.add(item);
                    } catch (RuntimeException e) {
                        Log.e("Firebase", "Error parsing document: " + e.getMessage(), e);
                    }
                }
            }
            listener.onDataFetched(dataList);
        });
    }
}
