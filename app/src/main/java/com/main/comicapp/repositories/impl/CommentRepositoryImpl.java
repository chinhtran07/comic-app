package com.main.comicapp.repositories.impl;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.Comment;
import com.main.comicapp.models.User;
import com.main.comicapp.repositories.CommentRepository;
import com.main.comicapp.repositories.SendMailRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;

public class CommentRepositoryImpl implements CommentRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
    private final SendMailRepository sendMailRepository = new SendMailRepositoryImpl();


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
        DocumentReference commentDocRef = db.collection("comments").document(commentId);

        return commentDocRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Boolean isActive = document.getBoolean("isActive");
                    if (isActive != null) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("isActive", !isActive);

                        return commentDocRef.update(updates).addOnSuccessListener(aVoid -> {
                            String userId = document.getString("userId");
                            if (userId != null) {
                                db.collection("users").document(userId).get().addOnSuccessListener(userTask -> {
                                    User user = userTask.toObject(User.class);
                                    if (user != null) {
                                        sendEmailAsync(user, !isActive);
                                    }
                                }).addOnFailureListener(e -> {
                                    Log.e("UpdateStatus", "Failed to fetch user information", e);
                                });
                            }
                        });
                    } else {
                        throw new RuntimeException("Field 'isActive' is missing or null.");
                    }
                } else {
                    throw new RuntimeException("Document does not exist.");
                }
            } else {
                throw new RuntimeException("Failed to fetch the document.", task.getException());
            }
        });
    }

    private void sendEmailAsync(User user, boolean newStatus) {
        emailExecutor.submit(() -> {
            try {
                sendMailRepository.sendStatusComment(user, !newStatus);
            } catch (MessagingException e) {
                Log.e("EmailSender", "Failed to send status change email", e);
            }
        });
    }


    @Override
    public Task<Void> createComment(Comment comment) {
        Map<String, Object> data = Comment.toMap(comment);
        return db.collection("comments").document(comment.getId()).set(data);
    }
}
