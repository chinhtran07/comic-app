package com.main.comicapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.viewmodels.UserViewModel;

public class UserProfileActivity extends BaseActivity {
    private UserViewModel userViewModel;

    private ImageView ivProfilePicture;
    private TextView tvFirstName, tvLastName, tvUsername, tvGender, tvBirthDate, tvEmail, tvUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        tvFirstName = findViewById(R.id.tv_first_name);
        tvLastName = findViewById(R.id.tv_last_name);
        tvUsername = findViewById(R.id.tv_username);
        tvGender = findViewById(R.id.tv_gender);
        tvBirthDate = findViewById(R.id.tv_birth_date);
        tvEmail = findViewById(R.id.tv_email);
        tvUserRole = findViewById(R.id.tv_user_role);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Fetch user with ID 1
        String userId = "dIX6wnccEfaPLddqqPWS5MvpfZz2";
        userViewModel.fetchUserById(userId);

        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                tvFirstName.setText(user.getFirstName());
                tvLastName.setText(user.getLastName());
                tvUsername.setText(user.getUsername());
                tvGender.setText(user.getGender());
                tvBirthDate.setText(user.getBirthDate());
                tvEmail.setText(user.getEmail());
                tvUserRole.setText(user.getUserRole());
                // Bạn có thể tải ảnh từ URL nếu cần thiết
                // Glide.with(this).load(user.getProfilePictureUrl()).into(ivProfilePicture);
            }
        });
    }
}
