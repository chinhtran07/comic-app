package com.main.comicapp.repositories.impl;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.repositories.ChapterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        getChapterReference().document(id).get().addOnCompleteListener(task -> {
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
        });
        return chapterLiveData;
    }

    @Override
    public void addChapter(Chapter chapter) {
        getChapterReference().add(chapter)
                .addOnSuccessListener(documentReference -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    @Override
    public void updateChapter(String id, Chapter chapter) {
        getChapterReference().document(id).set(chapter)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    @Override
    public void deleteChapter(String id) {
        getChapterReference().document(id).delete()
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    @Override
    public LiveData<List<Chapter>> getChapters(String titleId) {
        MutableLiveData<List<Chapter>> chaptersLiveData = new MutableLiveData<>();
        Query query = getChapterReference().whereEqualTo("titleId", titleId);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Chapter> chapters = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Map<String, Object> data = documentSnapshot.getData();
                Chapter chapter = Chapter.toObject(data, documentSnapshot.getId());
                chapters.add(chapter);
            }
            chaptersLiveData.setValue(chapters);
        }).addOnFailureListener(e -> chaptersLiveData.setValue(null));
        return chaptersLiveData;
    }

    @Override
    public LiveData<List<String>> getChapterDocumentIds(String titleId) {
        MutableLiveData<List<String>> result = new MutableLiveData<>();
        getChapterReference().whereEqualTo("titleId", titleId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> documentIds = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        documentIds.add(document.getId());
                    }
                    result.setValue(documentIds);
                }).addOnFailureListener(e -> result.setValue(null));
        return result;
    }

    @Override
    public LiveData<List<Chapter>> getChaptersByIds(List<String> chapterIds) {
        MutableLiveData<List<Chapter>> chaptersLiveData = new MutableLiveData<>();
        if (chapterIds == null || chapterIds.isEmpty()) {
            chaptersLiveData.setValue(new ArrayList<>());
            return chaptersLiveData;
        }

        getChapterReference().whereIn("__name__", chapterIds).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Chapter> chapters = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Map<String, Object> data = documentSnapshot.getData();
                Chapter chapter = Chapter.toObject(data, documentSnapshot.getId());
                chapters.add(chapter);
            }
            chaptersLiveData.setValue(chapters);
        }).addOnFailureListener(e -> chaptersLiveData.setValue(null));
        return chaptersLiveData;
    }

    @Override
    public LiveData<List<Chapter>> getAllChapters() {
        MutableLiveData<List<Chapter>> allChaptersLiveData = new MutableLiveData<>();
        getChapterReference().get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Chapter> chapters = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Map<String, Object> data = documentSnapshot.getData();
                Chapter chapter = Chapter.toObject(data, documentSnapshot.getId());
                chapters.add(chapter);
            }
            allChaptersLiveData.setValue(chapters);
        }).addOnFailureListener(e -> allChaptersLiveData.setValue(null));
        return allChaptersLiveData;
    }

    private CollectionReference getChapterReference() {
        return FirebaseFirestore.getInstance().collection("chapters");
    }
}
