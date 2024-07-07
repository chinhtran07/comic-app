package com.main.comicapp.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    public interface DataFetchListener<T> {
        void onDataFetched(List<T> data);
    }

    public <T> void fetchData(String collectionName, final Class<T> type, final DataFetchListener<T> listener) {
        CollectionReference collectionRef = db.collection(collectionName);
        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FirebaseDataManager", "Error fetching data: ", error);
                    return;
                }

                List<T> dataList = new ArrayList<>();
                assert value != null;

                for (DocumentSnapshot document : value.getDocuments()) {
                    Log.d("Firebase", "Document Data: " + document.getData());
                    T item = document.toObject(type);
                    if (item != null) {
                        dataList.add(item);
                    } else {
                        Log.e("Firebase", "Error parsing document to " + type.getSimpleName());
                    }
                }
                listener.onDataFetched(dataList);
            }
        });
    }
}
