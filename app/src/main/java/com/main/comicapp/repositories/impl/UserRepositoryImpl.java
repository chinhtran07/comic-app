package com.main.comicapp.repositories.impl;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.User;
import com.main.comicapp.repositories.SendMailRepository;
import com.main.comicapp.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;

public class UserRepositoryImpl implements UserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
    private final SendMailRepository sendMailRepository = new SendMailRepositoryImpl();

    @Override
    public Task<QuerySnapshot> getAllUser() {
        return db.collection("users")
                .whereEqualTo("userRole", "USER")
                .get();
    }

    @Override
    public Task<QuerySnapshot> getAllAdmin() {
        return db.collection("users")
                .whereEqualTo("userRole", "ADMIN")
                .get();
    }

    @Override
    public Task<DocumentSnapshot> getUserById(String userId) {
        return db.collection("users").document(userId).get();
    }

    @Override
    public Task<QuerySnapshot> getUserByEmail(String userEmail) {
        return db.collection("users")
                .whereEqualTo("email", userEmail)
                .get();
    }

    @Override
    public Task<QuerySnapshot> fetchUserByUsername(String username) {
        return db.collection("users")
                .whereEqualTo("username", username)
                .get();
    }

    @Override
    public Task<Void> save(Map<String, Object> userData, String userId) {
        return db.collection("users").document(userId).set(userData);
    }

    @Override
    public Task<Void> updateUserStatus(String userId) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        return userDocRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Boolean isActive = document.getBoolean("isActive");
                    if (isActive != null) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("isActive", !isActive);
                        return userDocRef.update(updates).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    sendEmailAsync(user, !isActive);
                                }
                            } else {
                                Log.e("UpdateStatus", "Failed to update user status", updateTask.getException());
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

    @Override
    public Task<Void> deleteUser(String userId) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        return userDocRef.delete();
    }

    @Override
    public boolean addUser(User user) {
        return db.collection("users").document(user.getId()).set(User.toMap(user)).isSuccessful();
    }

    private void sendEmailAsync(User user, boolean newStatus) {
        emailExecutor.submit(() -> {
            try {
                sendMailRepository.sendStatusChangeEmail(user, newStatus);
            } catch (MessagingException e) {
                Log.e("EmailSender", "Failed to send status change email", e);
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        emailExecutor.shutdown();
    }
}
