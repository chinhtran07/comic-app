package com.main.comicapp.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.models.UserSession;
import com.main.comicapp.utils.ValidateUtil;
import com.main.comicapp.viewmodels.UserSessionViewModel;

public class BaseActivity extends AppCompatActivity {
    private UserSessionViewModel userSessionViewModel;
    protected UserSession currentUserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userSessionViewModel = new ViewModelProvider(this).get(UserSessionViewModel.class);
        validateSession();
        super.onCreate(savedInstanceState);
    }

    protected void validateSession() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (!prefs.contains("session_id")) {
            // Force to login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        String currentSessionId = prefs.getString("session_id", null);
        userSessionViewModel.fetchUserSession(currentSessionId);
        userSessionViewModel.getCurrentUserSession().observe(this, userSession -> {
            if (userSession != null) {
                if ((System.currentTimeMillis() - userSession.getLastLoginTime()) > ValidateUtil.SESSION_LENGTH) {
                    Log.d(TAG, "validateSession: Session expired");
                    userSessionViewModel.deleteUserSession(currentSessionId);
                    forceLogout();
                }
                else {
                   currentUserSession = userSession;
                }
            }
        });
    }

    protected void forceLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Your session has expired. Forcing logout")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}