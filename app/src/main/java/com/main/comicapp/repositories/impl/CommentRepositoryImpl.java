package com.main.comicapp.repositories.impl;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Comment;
import com.main.comicapp.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentRepositoryImpl implements CommentRepository {
    private static final String TAG = "com.main.comicapp.repositories.impl.CommentRepositoryImpl";
    private String collectionName = "comments";
    private static CommentRepositoryImpl instance;

    public static synchronized CommentRepositoryImpl getInstance() {
        if (instance == null)
            instance = new CommentRepositoryImpl();
        return instance;
    }

    public CommentRepositoryImpl() {

    }

    @Override
    public LiveData<Comment> getComment(String id) {
        return null;
    }

    @Override
    public void addComment(Comment comment) {

    }

    @Override
    public void updateComment(String id, Comment comment) {

    }

    @Override
    public void deleteComment(String id) {

    }

    @Override
    public LiveData<List<Comment>> getCommentsByTitle(String titleId) {
        MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<>();

        getDbReference().whereEqualTo("titleId", titleId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Comment> comments = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Comment comment = Comment.toObject(data, document.getId());
                        comments.add(comment);
                    } else {
                        Log.e(TAG, "onSuccess: Document does not exist.");
                    }
                }
                commentsLiveData.setValue(comments);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                commentsLiveData.setValue(null);
                Log.e(TAG, "onFailure: ", e.getCause());
            }
        });
        return commentsLiveData;
    }

    @Override
    public LiveData<List<Comment>> getCommentsByUser(String userId) {
        return null;
    }

    private CollectionReference getDbReference() {
        return FirebaseFirestore.getInstance().collection(collectionName);
    }
}
