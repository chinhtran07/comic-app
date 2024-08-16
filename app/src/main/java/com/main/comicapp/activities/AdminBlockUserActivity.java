package com.main.comicapp.activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

public class AdminBlockUserActivity extends AppCompatActivity {
    private UserViewModel userViewModel;

    private ImageView ivProfilePicture;
    private TextView tvFirstName, tvLastName, tvUsername, tvGender, tvBirthDate, tvEmail, tvUserRole;
    private Button btUserRole;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile);

        initializeViews();
        retrieveAndDisplayUserData();
    }

    private void initializeViews() {
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        tvFirstName = findViewById(R.id.tv_first_name);
        tvLastName = findViewById(R.id.tv_last_name);
        tvUsername = findViewById(R.id.tv_username);
        tvGender = findViewById(R.id.tv_gender);
        tvBirthDate = findViewById(R.id.tv_birth_date);
        tvEmail = findViewById(R.id.tv_email);
        tvUserRole = findViewById(R.id.tv_user_role);
        btUserRole = findViewById(R.id.btn_change_role);

        btUserRole.setOnClickListener(v -> changeUserRole());
    }

    private void retrieveAndDisplayUserData() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String userId = getIntent().getStringExtra("USER_ID");
        if (userId != null) {
            userViewModel.fetchUserById(userId);
            observeUserData();
        } else {
            showError("No user ID found.");
        }
    }

    private void observeUserData() {
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                updateUIWithUserData(user);
            } else {
                showError("Failed to load user data.");
            }
        });
    }

    private void updateUIWithUserData(User user) {
        tvFirstName.setText(user.getFirstName());
        tvLastName.setText(user.getLastName());
        tvUsername.setText(user.getUsername());
        tvGender.setText(user.getGender());
        tvBirthDate.setText(user.getBirthDate());
        tvEmail.setText(user.getEmail());
        tvUserRole.setText(user.getUserRole());
        id = user.getId();

        Glide.with(this)
                .load(user.getAvatar())
                .circleCrop()
                .error(R.drawable.ic_user_placeholder)
                .into(ivProfilePicture);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void changeUserRole() {
        if (id != null) {
            userViewModel.updateUserRole(id).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Role updated successfully", Toast.LENGTH_SHORT).show();
                    navigateToAdminManagementUserActivity(id);
                } else {
                    showError("Failed to update role.");
                }
            });
        } else {
            showError("User ID is null.");
        }
    }

    private void navigateToAdminManagementUserActivity(String userId) {
        Intent intent = new Intent(this, AdminManagementUserActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }

}
