package com.main.comicapp.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.main.comicapp.R;
import com.main.comicapp.enums.UserRole;
import com.main.comicapp.models.User;
import com.main.comicapp.models.UserSession;
import com.main.comicapp.utils.ValidateUtil;
import com.main.comicapp.viewmodels.UserSessionViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

abstract class BaseActivity extends AppCompatActivity {
    private UserSessionViewModel userSessionViewModel;
    private UserViewModel userViewModel;
    protected UserSession currentUserSession;
    BottomNavigationView bottomNavigationView;
    private User currentUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userSessionViewModel = new ViewModelProvider(this).get(UserSessionViewModel.class);
        validateSession();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Bạn có muốn thoát ứng dụng ?");
                builder.setPositiveButton("Có", (dialog, which) -> {
                    finishAffinity();
                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                    dialog.dismiss();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(R.layout.activity_base);
        ViewGroup contentLayout = findViewById(R.id.content_frame);
        LayoutInflater.from(this).inflate(layoutResId, contentLayout, true);
     }

    @Override
    protected void onStart() {
        super.onStart();

        setupBottomNavView();
    }

    private void setupBottomNavView() {
        SharedPreferences actionPrefs = getSharedPreferences("action_history", MODE_PRIVATE);
        int currentAction = actionPrefs.getInt("current_action", R.id.action_home);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(currentAction);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
             int itemId = item.getItemId();
             SharedPreferences.Editor editor = actionPrefs.edit();

             editor.putInt("current_action", itemId);
             editor.apply();
             if (itemId == R.id.action_home) {
                 Intent intent = new Intent(this, HomeActivity.class);
                 startActivity(intent);
                 finish();
                 return true;
             }
             else if (itemId == R.id.action_history) {
                 Intent intent = new Intent(this, AllRecentComicActivity.class);
                 startActivity(intent);
                 finish();
                 return true;
             }
             else if (itemId == R.id.action_profile) {
                 Intent intent = new Intent(this, UserProfileActivity.class);
                 startActivity(intent);
                 finish();
                 return true;
             }
             else if (itemId == R.id.action_logout) {
                 logout();
                 return true;
             }
             else if (itemId == R.id.action_admin) {
                 Intent intent = new Intent(this, AdminActivity.class);
                 startActivity(intent);
                 finish();
                 return true;
             }
             else return false;
        });
        bottomNavigationView.getMenu().findItem(R.id.action_admin).setVisible(false);
        invalidateOptionsMenu();

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
        if (currentSessionId != null) {
            userSessionViewModel.fetchUserSession(currentSessionId);
            userSessionViewModel.getCurrentUserSession().observe(this, userSession -> {
                if (userSession != null) {
                    if ((System.currentTimeMillis() - userSession.getLastLoginTime()) > ValidateUtil.SESSION_LENGTH) {
                        Log.d(TAG, "validateSession: Session expired");
                        forceLogout();
                    } else {
                        currentUserSession = userSession;
                        userViewModel.fetchUserById(userSession.getUserId());
                        userViewModel.getUserLiveData().observe(this, user -> {
                            if (user != null) {
                                if (user.getUserRole().equals(UserRole.ADMIN.name())) {
                                    bottomNavigationView.getMenu().findItem(R.id.action_profile).setVisible(false);
                                    bottomNavigationView.getMenu().findItem(R.id.action_admin).setVisible(true);
                                    invalidateOptionsMenu();
                                }
                            }
                        });

                    }
                }
            });


        }
    }

    protected void forceLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Your session has expired. Forcing logout")
                .setPositiveButton("OK", (dialog, which) -> {
                    clearSession();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout")
                .setMessage("Do you want to end your session ?")
                .setPositiveButton("OK", (dialog, which) -> {
                    clearSession();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }).setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    protected void clearSession() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String currentSessionId = prefs.getString("session_id", null);
        userSessionViewModel.deleteUserSession(currentSessionId);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("session_id");
        editor.apply();
    }


}