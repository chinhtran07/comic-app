package com.main.comicapp.repositories.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.repositories.CommentRepository;

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
    public Task<Void> deleteComment(String commentId) {
        return db.collection("comments").document(commentId).delete();
    }
}
