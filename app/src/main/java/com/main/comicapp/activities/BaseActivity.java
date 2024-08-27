package com.main.comicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.main.comicapp.R;
import com.main.comicapp.activities.admin.AdminActivity;
import com.main.comicapp.activities.user.AllRecentComicActivity;
import com.main.comicapp.activities.user.HomeActivity;
import com.main.comicapp.activities.user.UserProfileActivity;
import com.main.comicapp.enums.UserRole;
import com.main.comicapp.models.UserSession;
import com.main.comicapp.viewmodels.UserSessionViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    private UserSessionViewModel userSessionViewModel;
    private UserViewModel userViewModel;
    protected UserSession currentUserSession;
    protected BottomNavigationView bottomNavigationView;
    private Context context;
    private String uid;
    private Map<Integer, Runnable> navigationActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initializeViewModels();
        setupBackPressedHandler();
        initializeNavigationActions();
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

    private void initializeViewModels() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userSessionViewModel = new ViewModelProvider(this).get(UserSessionViewModel.class);
    }

    private void setupBackPressedHandler() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Bạn có muốn thoát ứng dụng?")
                .setPositiveButton("Có", (dialog, which) -> finishAffinity())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void setupBottomNavView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        SharedPreferences actionPrefs = getSharedPreferences("action_history", MODE_PRIVATE);
        int currentAction = actionPrefs.getInt("current_action", R.id.action_home);

        bottomNavigationView.setSelectedItemId(currentAction);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> handleBottomNavSelection(item.getItemId(), actionPrefs));
        bottomNavigationView.getMenu().findItem(R.id.action_admin).setVisible(false);
        invalidateOptionsMenu();
    }

    private void initializeNavigationActions() {
        navigationActions = new HashMap<>();
        navigationActions.put(R.id.action_home, this::navigateToHome);
        navigationActions.put(R.id.action_history, this::navigateToHistory);
        navigationActions.put(R.id.action_profile, this::startUserProfileActivity);
        navigationActions.put(R.id.action_logout, this::logout);
        navigationActions.put(R.id.action_admin, this::navigateToAdmin);
    }

    private boolean handleBottomNavSelection(int itemId, SharedPreferences actionPrefs) {
        SharedPreferences.Editor editor = actionPrefs.edit();
        editor.putInt("current_action", itemId);
        editor.apply();

        Runnable action = navigationActions.get(itemId);
        if (action != null) {
            action.run();
            return true;
        }
        return false;
    }

    private void navigateToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void navigateToHistory() {
        startActivity(new Intent(this, AllRecentComicActivity.class));
        finish();
    }

    private void navigateToAdmin() {
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }

    private void startUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("USER_ID", uid);
        startActivity(intent);
        finish();
    }

    protected void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to end your session?")
                .setPositiveButton("OK", (dialog, which) -> {
                    clearSession();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    protected void clearSession() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String currentSessionId = prefs.getString("session_id", null);
        userSessionViewModel.deleteUserSession(currentSessionId);
        prefs.edit().remove("session_id").apply();
    }
}
