package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Page;
import com.main.comicapp.repositories.PageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageRepositoryImpl implements PageRepository {

    private static PageRepositoryImpl instance;

    public static synchronized PageRepositoryImpl getInstance() {
        if (instance == null) {
            instance =  new PageRepositoryImpl();
        }
        return instance;
    }

    @Override
    public LiveData<List<Page>> getPages(Map<String, String> params, String chapterId) {
        MutableLiveData<List<Page>> pagesLiveData = new MutableLiveData<>();
        Query query = getPageReference().whereEqualTo("chapterId", chapterId);

        String limit = params.get("limit");
        if (limit != null && !limit.isEmpty())
            query.limit(Integer.parseInt(limit));

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Page> pages = new ArrayList<>();
                for (DocumentSnapshot document: queryDocumentSnapshots) {
                    Page page = document.toObject(Page.class);
                    pages.add(page);
                }
                pagesLiveData.setValue(pages);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pagesLiveData.setValue(null);
            }
        });

        return pagesLiveData;
    }

    private CollectionReference getPageReference() {
        return FirebaseFirestore.getInstance().collection("pages");
    }
}
