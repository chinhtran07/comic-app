package com.main.comicapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.activities.user.HomeActivity;
import com.main.comicapp.utils.ValidateUtil;
import com.main.comicapp.viewmodels.UserSessionViewModel;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private UserSessionViewModel userSessionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userSessionViewModel = new ViewModelProvider(this).get(UserSessionViewModel.class);

        new Handler().postDelayed(this::validateSession, 2000); // Delay 2 seconds for splash screen
    }

    private void validateSession() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (!prefs.contains("session_id")) {
            navigateToLogin();
            return;
        }

        String currentSessionId = prefs.getString("session_id", null);
        if (currentSessionId != null) {
            userSessionViewModel.fetchUserSession(currentSessionId);
            userSessionViewModel.getCurrentUserSession().observe(this, userSession -> {
                if (userSession != null) {
                    if ((System.currentTimeMillis() - userSession.getLastLoginTime()) > ValidateUtil.SESSION_LENGTH) {
                        Log.d("SplashActivity", "validateSession: Session expired");
                        navigateToLogin();
                    } else {
                        navigateToMain();
                    }
                } else {
                    navigateToLogin();
                }
            });
        } else {
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMain() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
