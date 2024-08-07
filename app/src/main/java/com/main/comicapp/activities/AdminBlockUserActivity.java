package com.main.comicapp.activities;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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

        Glide.with(this)
                .load(user.getAvatar())
                .circleCrop()
                .error(R.drawable.ic_user_placeholder)
                .into(ivProfilePicture);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
