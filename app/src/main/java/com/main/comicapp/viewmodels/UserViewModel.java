package com.main.comicapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.models.User;
import com.main.comicapp.repositories.UserRepository;
import com.main.comicapp.repositories.impl.UserRepositoryImpl;

import java.util.List;
import java.util.Map;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<Integer> readerCountLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> adminCountLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> usernameOrEmailTakenLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();

    private boolean isUsernameTaken = false;
    private boolean isEmailTaken = false;

    public UserViewModel() {
        userRepository = new UserRepositoryImpl();
    }

    public LiveData<Integer> getReaderCountLiveData() {
        return readerCountLiveData;
    }

    public LiveData<Integer> getAdminCountLiveData() {
        return adminCountLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<User> getCurrentUserLiveData() {
        return currentUserLiveData;
    }

    public LiveData<Boolean> getUsernameOrEmailTakenLiveData() {
        return usernameOrEmailTakenLiveData;
    }

    public LiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    public boolean isUsernameTaken() {
        return isUsernameTaken;
    }

    public boolean isEmailTaken() {
        return isEmailTaken;
    }

    public void fetchReaderCount() {
        userRepository.getAllUser().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                int count = task.getResult().size();
                readerCountLiveData.setValue(count);
            } else {
                readerCountLiveData.setValue(0);
            }
        });
    }

    public void fetchAdminCount() {
        userRepository.getAllAdmin().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                int count = task.getResult().size();
                adminCountLiveData.setValue(count);
            } else {
                adminCountLiveData.setValue(0);
            }
        });
    }

    public void fetchUserById(String userId) {
        userRepository.getUserById(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                User user = User.toObject(document.getData(), document.getId());
                userLiveData.setValue(user);
            } else {
                userLiveData.setValue(null);
            }
        });
    }

    public void fetchUserByEmail(String email) {
        userRepository.getUserByEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    User user = User.toObject(document.getData(), document.getId());
                    currentUserLiveData.setValue(user);
                } else {
                    currentUserLiveData.setValue(null);
                }
            }
        });
    }

    public void fetchUserByUsername(String username) {
        userRepository.fetchUserByUsername(username).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    User user = User.toObject(document.getData(), document.getId());
                    currentUserLiveData.setValue(user);
                } else {
                    currentUserLiveData.setValue(null);
                }
            }
        });
    }

    public void checkIfUsernameOrEmailTaken(String username, String email) {
        userRepository.fetchUserByUsername(username).addOnCompleteListener(usernameTask -> {
            if (usernameTask.isSuccessful() && !usernameTask.getResult().isEmpty()) {
                isUsernameTaken = true;
                usernameOrEmailTakenLiveData.setValue(true);
            } else {
                isUsernameTaken = false;
                userRepository.getUserByEmail(email).addOnCompleteListener(emailTask -> {
                    if (emailTask.isSuccessful() && !emailTask.getResult().isEmpty()) {
                        isEmailTaken = true;
                        usernameOrEmailTakenLiveData.setValue(true);
                    } else {
                        isEmailTaken = false;
                        usernameOrEmailTakenLiveData.setValue(false);
                    }
                });
            }
        });
    }

    public Task<Void> saveUser(String userId, Map<String, Object> userData) {
        return userRepository.save(userData, userId);
    }

    public Task<Void> updateUserStatus(String userId) {
        return userRepository.updateUserStatus(userId);
    }

    public Task<QuerySnapshot> getAllUser() {
        return userRepository.getAllUser();
    }

    public void loadAllUsers() {
        userRepository.getAllUser().addOnSuccessListener(querySnapshot -> {
            List<User> userList = querySnapshot.toObjects(User.class);
            usersLiveData.setValue(userList);
        }).addOnFailureListener(e -> {
            Log.e("UserViewModel", "Error loading users", e);
        });
    }

    public Task<Void> updateUserRole(String id) {
        return userRepository.updateUserRole(id);
    }
}
