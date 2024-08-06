package com.main.comicapp.repositories.impl;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.User;
import com.main.comicapp.repositories.UserRepository;
import com.main.comicapp.utils.EmailSenderUtils;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                        userDocRef.update(updates).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                try {
                                    User user = document.toObject(User.class);
                                    if (user != null) {
                                        sendStatusChangeEmail(user, !isActive);
                                    }
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return null;
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

    private void sendStatusChangeEmail(User user, boolean newStatus) throws MessagingException {
        String subject;
        String messageBody;

        if (newStatus) {
            subject = "Tài Khoản Đã Được Kích Hoạt";
            messageBody = "Thân chào " + user.getLastName() + " " + user.getFirstName() + ",\n\n"
                    + "Tài khoản của bạn đã được kích hoạt thành công. Bạn giờ đây có thể truy cập vào dịch vụ của chúng tôi.\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ";
        } else {
            subject = "Thông Báo Về Tình Trạng Tài Khoản";
            messageBody = "Thân chào " + user.getLastName() + " " + user.getFirstName() + ",\n\n"
                    + "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ với quản trị viên để được hỗ trợ và biết thêm thông tin chi tiết.\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ";
        }

        EmailSenderUtils.sendEmail(user.getEmail(), subject, messageBody);
    }
}
