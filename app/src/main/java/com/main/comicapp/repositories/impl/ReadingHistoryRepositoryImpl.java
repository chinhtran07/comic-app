package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.repositories.ReadingHistoryRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReadingHistoryRepositoryImpl implements ReadingHistoryRepository {

    private static ReadingHistoryRepositoryImpl instance;

    private ReadingHistoryRepositoryImpl() {

    }

    public static synchronized ReadingHistoryRepositoryImpl getInstance() {
        if (instance == null)
            instance = new ReadingHistoryRepositoryImpl();
        return instance;
    }

    @Override
    public LiveData<List<ReadingHistory>> getRecentReadingTitles(String userId, int limit) {
        MutableLiveData<List<ReadingHistory>> readingHistoryLiveData = new MutableLiveData<>();
        Query query = getReadingHistoryReference()
                .whereEqualTo("userId", userId)
                .orderBy("lastTimeReading", Query.Direction.DESCENDING);
        if (limit > 0) {
            query.limit(limit);
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        String id = UUID.randomUUID().toString();
        readingHistory.setId(id);
        getReadingHistoryReference().document(id).set(readingHistory);
    }

    @Override
    public void addHistory(String userId, String titleId) {
        String id = UUID.randomUUID().toString();
        Date currentDate = new Date();
        ReadingHistory readingHistory = new ReadingHistory(userId, titleId, currentDate);
        readingHistory.setId(id);
        getReadingHistoryReference().document(id).set(readingHistory);
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
        return FirebaseFirestore.getInstance().collection("reading_histories");
    }

    @Override
    public LiveData<List<ReadingHistory>> getAllReadingHistoriesByUserId(String userId) {
        MutableLiveData<List<ReadingHistory>> readingHistoryLiveData = new MutableLiveData<>();
        getReadingHistoryReference()
                .whereEqualTo("userId", userId)
                .get()
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
    public void deleteAllHistoriesByUserId(String userId) {
        getReadingHistoryReference().whereEqualTo("userId", userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().delete(); // Xóa từng tài liệu
                    }
                });
    }
}
