package com.main.comicapp.repositories.impl;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.main.comicapp.models.ReadingHistory;
import com.main.comicapp.repositories.ReadingHistoryRepository;
import com.main.comicapp.repositories.TitleRepository;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;
import com.main.comicapp.utils.FirebaseUtil;
import com.main.comicapp.viewmodels.ReadingHistoryViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                        Title title = document.toObject(Title.class);
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
                    FirebaseUtil.getFirestore().collection(collectionName).document(titleId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null && document.exists()) {
                                            Title title = document.toObject(Title.class);
                                            if (title != null) {
                                                recentTitles.add(title);
                                            }
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
        return FirebaseUtil.getFirestore().collection(collectionName);
    }

//    @Override
//    public void getAllTitleRealtime(FirebaseUtils.DataFetchListener<Title> listener)  {
//        List<Title> titles = new ArrayList<>();
//
//        FirebaseUtils.getInstance().getDb().collection("titles")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e(TAG, "onEvent: Listen failed", error);
//                    return;
//                }
//                assert value != null;
//                for (DocumentSnapshot document: value.getDocuments()) {
//                    Title title = new Title();
//                    title.setId(document.getId());
//                    Map<String, Object> data = document.getData();
//                    assert data != null;
//                    if (validateTitle(data)) {
//                        title.setTitle((String)data.get("title"));
//                        title.setCover((String)data.get("cover"));
//                        @SuppressLint("SimpleDateFormat")
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                        try {
//                            Date uploadedDate = formatter.parse((String)data.get("uploadedDate"));
//                            title.setUploadedDate(uploadedDate);
//                        } catch (ParseException e) {
//                            Log.e("Error", "onEvent: Parse Exception", e);
//                        }
//                        title.setPubStatus((String)data.get("pubStatus"));
//                        title.setTitleFormat((String)data.get("titleFormat"));
//                        List<Genre> genres = new ArrayList<>();
//                        //noinspection unchecked
//                        List<String> genreIds = (List<String>) data.get("genres");
//                        assert genreIds != null;
//                        for (String id : genreIds) {
//                            Genre genre = new Genre();
//                            genre.setId(id);
//                            FirebaseUtils.getInstance().getDb().collection("genres").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    genre.setName((String)documentSnapshot.get("name"));
//                                    genres.add(genre);
//                                }
//                            });
//                        }
//                        title.setGenres(genres);
//                        titles.add(title);
//                    }
//                }
//
//                listener.onDataFetched(titles);
//            }
//        });
//    }
//
//    @Override
//    public Title getTitle(String id) {
//        return null;
//    }
//
//    private boolean validateTitle(Map<String, Object> data) {
//        for (Map.Entry<String, Object> entry : data.entrySet()) {
//            if (entry.getValue() == null) return false;
//        }
//        return true;
//    }
}
