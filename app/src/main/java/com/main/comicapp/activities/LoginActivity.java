package com.main.comicapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.models.User;
import com.main.comicapp.models.UserSession;
import com.main.comicapp.viewmodels.UserSessionViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private UserViewModel userViewModel;
    private UserSessionViewModel userSessionViewModel;
    private User currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userSessionViewModel = new ViewModelProvider(this).get(UserSessionViewModel.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (login(email, password)) {
                        createUserSession(currentUser.getId());
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean login(String email, String password) {
        if (validateInfo(email, password)) {
            userViewModel.fetchUserByEmail(email);
            currentUser = userViewModel.getCurrentUserLiveData().getValue();
            if (currentUser != null) {
                if (BCrypt.checkpw(password, currentUser.getPassword())) {
                    // Login successful
                    return true;
                } else {
                    etPassword.setError("Incorrect password");
                }
            }
        }
        return false;
    }

    public boolean validateInfo(String email, String password) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(email).matches()) {
            etEmail.setError("Invalid email address");
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    public void createUserSession(String userId) {
        // Create a new UserSession object
        UserSession userSession = new UserSession();
        userSession.setId(UUID.randomUUID().toString());
        userSession.setUserId(userId);
        userSession.setLastLoginTime(System.currentTimeMillis());
        userSessionViewModel.createUserSession(userSession);
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("session_id", userSession.getId());
        editor.apply();
    }


}
