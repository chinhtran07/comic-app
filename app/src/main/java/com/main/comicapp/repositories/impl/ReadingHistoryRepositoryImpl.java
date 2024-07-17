package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.repositories.ReadingHistoryRepository;
import com.main.comicapp.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class ReadingHistoryRepositoryImpl implements ReadingHistoryRepository {

    private final String collectionName = "reading_histories";

    private static ReadingHistoryRepositoryImpl instance;

    private ReadingHistoryRepositoryImpl() {

    }

    public static synchronized ReadingHistoryRepositoryImpl getInstance() {
        if (instance == null)
            instance = new ReadingHistoryRepositoryImpl();
        return instance;
    }

    // limit = -1 --> get all recent Reading Titles
    @Override
    public LiveData<List<ReadingHistory>> getRecentReadingTitles(String userId, int limit) {
        MutableLiveData<List<ReadingHistory>> readingHistoryLiveData = new MutableLiveData<>();
        Query query = getReadingHistoryReference()
                .whereEqualTo("userId", userId)
                .orderBy("lastTimeReading", Query.Direction.DESCENDING);
        if (limit > 0) {
            query.limit(limit);
        }
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ReadingHistory> readingHistoryList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                ReadingHistory readingHistory = document.toObject(ReadingHistory.class);
                                if (readingHistory != null) {
                                    readingHistoryList.add(readingHistory);
                                }
                            }
                            readingHistoryLiveData.setValue(readingHistoryList);
                        } else {
                            readingHistoryLiveData.setValue(null);
                        }
                    }
                });

        return readingHistoryLiveData;
    }

    @Override
    public void addReadingHistory(ReadingHistory readingHistory) {
        getReadingHistoryReference().add(readingHistory);
    }

    @Override
    public void updateReadingHistory(String id, ReadingHistory readingHistory) {
        getReadingHistoryReference().document(id).set(readingHistory);
    }

    @Override
    public void deleteReadingHistory(String id) {
        getReadingHistoryReference().document(id).delete();
    }

    private CollectionReference getReadingHistoryReference() {
        return FirebaseUtil.getFirestore().collection(collectionName);
    }
}
