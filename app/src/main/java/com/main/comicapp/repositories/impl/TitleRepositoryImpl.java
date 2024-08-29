package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.models.Title;
import com.main.comicapp.repositories.TitleRepository;
import com.main.comicapp.viewmodels.ReadingHistoryViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Title title = Title.toObject(documentSnapshot.getData(), documentSnapshot.getId());
                        titleLiveData.setValue(title);
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

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Title> titleList = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Title title = Title.toObject(document.getData(), document.getId());
                    titleList.add(title);
                }
                titlesLiveData.setValue(titleList);
            }
        });

        return titlesLiveData;
    }

    @Override
    public LiveData<List<Title>> getRecentTitles(String userId, int limit) {
        MutableLiveData<List<Title>> recentTitlesLiveData = new MutableLiveData<>();
        ReadingHistoryViewModel readingHistoryViewModel = new ReadingHistoryViewModel();

        readingHistoryViewModel.getRecentReadingTitles(userId, limit).observeForever(recentReadingList -> {
            if (recentReadingList != null && !recentReadingList.isEmpty()) {
                List<String> titleIds = new ArrayList<>();
                for (ReadingHistory readingHistory : recentReadingList) {
                    titleIds.add(readingHistory.getTitleId());
                }

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

    @Override
    public LiveData<Integer> getTitleCount() {
        MutableLiveData<Integer> countLiveData = new MutableLiveData<>();
        getTitleReference().get().addOnSuccessListener(querySnapshot -> {
            int count = querySnapshot.size();
            countLiveData.setValue(count);
        });
        return countLiveData;
    }

    @Override
    public LiveData<Integer> getTitleCountByGenre(String genreId) {
        MutableLiveData<Integer> countLiveData = new MutableLiveData<>();
        Query query = getTitleReference().whereArrayContains("genreIds", genreId); // Sử dụng whereArrayContains để kiểm tra danh sách genreIds

        query.get().addOnSuccessListener(querySnapshot -> {
            int count = querySnapshot.size();
            countLiveData.setValue(count);
        });

        return countLiveData;
    }

    private CollectionReference getTitleReference() {
        return FirebaseFirestore.getInstance().collection(collectionName);
    }

    @Override
    public LiveData<List<Title>> getTitlesUpdatedThisMonth() {
        MutableLiveData<List<Title>> titlesLiveData = new MutableLiveData<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();

        Query query = getTitleReference().whereGreaterThanOrEqualTo("uploadedDate", firstDayOfMonth);

        query.get().addOnSuccessListener(querySnapshot -> {
            List<Title> titleList = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                Title title = Title.toObject(document.getData(), document.getId());
                titleList.add(title);
            }
            titlesLiveData.setValue(titleList);
        });

        return titlesLiveData;
    }


    @Override
    public LiveData<Integer> getTitleCountByGenreId(String genreId) {
        MutableLiveData<Integer> countLiveData = new MutableLiveData<>();
        Query query = getTitleReference().whereArrayContains("genres", genreId);

        query.get().addOnSuccessListener(querySnapshot -> {
            int count = querySnapshot.size();
            countLiveData.setValue(count);
        });

        return countLiveData;
    }


    private CollectionReference getGenreReference() {
        return FirebaseFirestore.getInstance().collection("genres");
    }

}
