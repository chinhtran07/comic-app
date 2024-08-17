package com.main.comicapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.R;
import com.main.comicapp.config.CloudinaryConfig;
import com.main.comicapp.viewmodels.UserViewModel;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Uri filePath;
    private String avatarUrl;

    private TextView tvBirthDate, tvUsernameError, tvEmailError;
    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etFirstName, etLastName;
    private Button btnPickDate, btnRegister, btnChooseImage;
    private Spinner spinnerGender;
    private ImageView ivImagePreview;
    private UserViewModel userViewModel;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        tvBirthDate = findViewById(R.id.tv_birth_date);
        tvUsernameError = findViewById(R.id.tv_username_error);
        tvEmailError = findViewById(R.id.tv_email_error);
        etUsername = findViewById(R.id.et_username);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnPickDate = findViewById(R.id.btn_pick_date);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        spinnerGender = findViewById(R.id.spinner_gender);
        btnRegister = findViewById(R.id.btn_register);
        ivImagePreview = findViewById(R.id.iv_image_preview);

        CloudinaryConfig.initCloudinary(this);

        btnPickDate.setOnClickListener(v -> showDatePickerDialog());

        btnChooseImage.setOnClickListener(v -> showImageOptions());

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegisterActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvBirthDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showImageOptions() {
        PopupMenu popupMenu = new PopupMenu(this, btnChooseImage);
        popupMenu.getMenuInflater().inflate(R.menu.menu_image_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_choose_image) {
                chooseImage();
                return true;
            } else if (id == R.id.action_take_photo) {
                takePhoto();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_IMAGE_REQUEST || requestCode == CAMERA_REQUEST_CODE) && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            if (filePath != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    ivImagePreview.setImageBitmap(bitmap);
                    ivImagePreview.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivImagePreview.setImageBitmap(imageBitmap);
                ivImagePreview.setVisibility(View.VISIBLE);
                // Convert bitmap to Uri
                filePath = getImageUri(imageBitmap);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void registerUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String username = etUsername.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String birthDate = tvBirthDate.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.checkIfUsernameOrEmailTaken(username, email);
        userViewModel.getUsernameOrEmailTakenLiveData().observe(this, isTaken -> {
            if (Boolean.TRUE.equals(isTaken)) {
                if (userViewModel.isUsernameTaken()) {
                    tvUsernameError.setVisibility(View.VISIBLE);
                    tvUsernameError.setText("Username is already taken");
                } else {
                    tvUsernameError.setVisibility(View.GONE);
                }

                if (userViewModel.isEmailTaken()) {
                    tvEmailError.setVisibility(View.VISIBLE);
                    tvEmailError.setText("Email is already taken");
                } else {
                    tvEmailError.setVisibility(View.GONE);
                }
            } else {
                tvUsernameError.setVisibility(View.GONE);
                tvEmailError.setVisibility(View.GONE);

                if (filePath == null) {
                    Toast.makeText(RegisterActivity.this, "Please upload an image first", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadImageAndRegister(email, password, username, firstName, lastName, gender, birthDate);
            }
        });
    }

    private void uploadImageAndRegister(String email, String password, String username, String firstName, String lastName, String gender, String birthDate) {
        MediaManager.get().upload(filePath)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(RegisterActivity.this, "Upload started", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        avatarUrl = resultData.get("secure_url").toString();
                        registerUserInFirebase(email, password, username, firstName, lastName, gender, birthDate);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(RegisterActivity.this, "Upload error: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        Log.d("Lỗi:", error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                    }
                })
                .dispatch();
    }

    private void registerUserInFirebase(String email, String password, String username, String firstName, String lastName, String gender, String birthDate) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user, username, firstName, lastName, gender, birthDate, password);
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser firebaseUser, String username, String firstName, String lastName, String gender, String birthDate, String password) {
        String id = firebaseUser.getUid();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", username);
        user.put("email", firebaseUser.getEmail());
        user.put("userRole", "USER");
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("gender", gender);
        user.put("birthDate", birthDate);
        user.put("password", hashedPassword);
        user.put("avatar", avatarUrl);
        user.put("isActive", false);

        db.collection("users").document(id)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
