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

    public <T> void fetchData(String collectionName, Query query, final Class<T> type, final DataFetchListener<T> listener) {
        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("FirebaseDataManager", "Error fetching data from " + collectionName + ": " + error.getMessage(), error);
                return;
            }

            List<T> dataList = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot document : value) {
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
