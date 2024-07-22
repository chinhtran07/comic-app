package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.repositories.ChapterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ChapterRepositoryImpl implements ChapterRepository {

    private static ChapterRepositoryImpl instance;

    public static synchronized ChapterRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ChapterRepositoryImpl();
        }
        return instance;
    }

    @Override
    public LiveData<Chapter> getChapter(String id) {
        MutableLiveData<Chapter> chapterLiveData = new MutableLiveData<>();
        getChapterReference().document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Chapter chapter = Chapter.toObject(document.getData(), id);
                        chapterLiveData.setValue(chapter);
                    } else {
                        chapterLiveData.setValue(null);
                    }
                } else {
                    chapterLiveData.setValue(null);
                }
            }
        });
        return chapterLiveData;
    }

    @Override
    public void addChapter(Chapter chapter) {

    }

    @Override
    public void updateChapter(String id, Chapter chapter) {

    }

    @Override
    public void deleteChapter(String id) {

    }

    @Override
    public LiveData<List<Chapter>> getChapters(String titleId) {
        MutableLiveData<List<Chapter>> chaptersLiveData = new MutableLiveData<>();
        Query query = FirebaseFirestore.getInstance().collection("chapters").whereEqualTo("titleId", titleId);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Chapter> chapters = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map<String, Object> data = documentSnapshot.getData();
                    Chapter chapter = Chapter.toObject(data, documentSnapshot.getId());
                    chapters.add(chapter);
                }
                chaptersLiveData.setValue(chapters);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                chaptersLiveData.setValue(null);
            }
        });
        return chaptersLiveData;
    }

    @Override
    public CompletableFuture<List<String>> getChapterDocumentIds(String titleId) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        getChapterReference().whereEqualTo("titleId", titleId)
                .get().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<String> documentIds = new ArrayList<>();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                documentIds.add(document.getId());
                            }
                            future.complete(documentIds);
                        } else {
                            future.completeExceptionally(new Exception("No documents found"));

                        }
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });
        return future;
    }

    private CollectionReference getChapterReference() {
        return FirebaseFirestore.getInstance().collection("chapters");
    }
}
