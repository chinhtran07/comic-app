package com.main.comicapp.repositories.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Comment;
import com.main.comicapp.repositories.CommentRepository;

import java.util.HashMap;
import java.util.Map;

public class CommentRepositoryImpl implements CommentRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Task<QuerySnapshot> getComments() {
        return db.collection("comments").get();
    }

    @Override
    public Task<DocumentSnapshot> getUserById(String userId) {
        return db.collection("users").document(userId).get();
    }

    @Override
    public Task<DocumentSnapshot> getTitleById(String titleId) {
        return db.collection("titles").document(titleId).get();
    }

    @Override
    public Task<Void> updateStatusComment(String commentId, boolean status) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("isActive", status);
        return db.collection("comments").document(commentId).update(updates);
    }


    @Override
    public Task<Void> createComment(Comment comment) {
        Map<String, Object> data = Comment.toMap(comment);
        return db.collection("comments").document(comment.getId()).set(data);
    }
}
