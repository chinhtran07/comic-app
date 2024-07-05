package com.main.comicapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView ivProfilePicture;
    private TextView tvFirstName, tvLastName, tvUsername, tvGender, tvBirthDate;

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

        // Dữ liệu demo
        loadUserProfile();
    }

    private void loadUserProfile() {
        tvFirstName.setText("John");
        tvLastName.setText("Doe");
        tvUsername.setText("johndoe123");
        tvGender.setText("Male");
        tvBirthDate.setText("01/01/1990");
    }
}
