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
import com.google.firebase.firestore.SetOptions;
import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.models.Title;
import com.main.comicapp.repositories.TitleRepository;
import com.main.comicapp.viewmodels.ReadingHistoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TitleRepositoryImpl implements TitleRepository {

    private final String collectionName = "titles";

    private static TitleRepositoryImpl instance;

    private TitleRepositoryImpl() {

    }

    public static synchronized TitleRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new TitleRepositoryImpl();
        }

        return instance;
    }

    @Override
    public LiveData<Title> getTitle(String id) {
        MutableLiveData<Title> titleLiveData = new MutableLiveData<>();
        getTitleReference().document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Title title = documentSnapshot.toObject(Title.class);
                                titleLiveData.setValue(title);
                            } else {
                                titleLiveData.setValue(null);
                            }
                        } else {
                            titleLiveData.setValue(null);
                        }
                    }
                });
        return titleLiveData;
    }

    @Override
    public void addTitle(Title title) {
        getTitleReference().add(title);

    }

    @Override
    public void updateTitle(String id, Title title) {
        getTitleReference().document(id).set(title, SetOptions.merge());
    }

    @Override
    public void deleteTitle(String id) {
        getTitleReference().document(id).delete();
    }

    @Override
    public LiveData<List<Title>> getTitles(Map<String, String> params) {
        MutableLiveData<List<Title>> titlesLiveData = new MutableLiveData<>();
        Query query = getTitleReference();

        //query


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Title> titleList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Title title = Title.toObject(document.getData(), document.getId());
                        titleList.add(title);
                    }
                    titlesLiveData.setValue(titleList);
                } else {
                    titlesLiveData.setValue(null);
                }
            }
        });
        return titlesLiveData;
    }

    @Override
    public LiveData<List<Title>> getRecentTitles(String userId, int limit) {
        MutableLiveData<List<Title>> recentTitlesLiveData = new MutableLiveData<>();

        ReadingHistoryViewModel readingHistoryViewModel = new ReadingHistoryViewModel();

        // Fetch recent reading history titles associated with userId
        readingHistoryViewModel.getRecentReadingTitles(userId, limit).observeForever(recentReadingList -> {
            if (recentReadingList != null && !recentReadingList.isEmpty()) {
                List<String> titleIds = new ArrayList<>();
                for (ReadingHistory readingHistory : recentReadingList) {
                    titleIds.add(readingHistory.getTitleId());
                }
                // Now fetch corresponding titles using titleIds
                List<Title> recentTitles = new ArrayList<>();
                for (String titleId : titleIds) {
                    getTitleReference().document(titleId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null && document.exists()) {
                                            Title title = Title.toObject(document.getData(), titleId);
                                            recentTitles.add(title);
                                        }
                                    } else {
                                        // Handle error fetching document
                                    }
                                }
                            });
                }
                recentTitlesLiveData.setValue(recentTitles);
            } else {
                recentTitlesLiveData.setValue(null);
            }
        });

        return recentTitlesLiveData;
    }

    private CollectionReference getTitleReference() {
        return FirebaseFirestore.getInstance().collection(collectionName);
    }
}
