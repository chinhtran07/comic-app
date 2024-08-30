package com.main.comicapp.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Comment;

public interface CommentRepository {
    Task<QuerySnapshot> getComments();
    Task<QuerySnapshot> getAllComments();
    Task<DocumentSnapshot> getUserById(String userId);
    Task<DocumentSnapshot> getTitleById(String titleId);
    Task<Void> updateStatusComment(String commentId, boolean status);
    Task<Void> createComment(Comment comment);
}
