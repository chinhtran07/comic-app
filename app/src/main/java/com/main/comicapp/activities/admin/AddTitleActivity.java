package com.main.comicapp.activities.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.R;
import com.main.comicapp.config.CloudinaryConfig;
import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.GenreViewModel;
import com.main.comicapp.viewmodels.TitleViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AddTitleActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private String coverImageUrl;

    private EditText etTitle, etViews, etTitleFormat;
    private Button btnChooseImage, btnAddTitle, btnSelectGenres;
    private ImageView ivImagePreview;
    private TitleViewModel titleViewModel;
    private GenreViewModel genreViewModel;

    private List<String> selectedGenreIds = new ArrayList<>();
    private String[] genreNames;
    private String[] genreIds;
    private boolean[] selectedGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_title);

        titleViewModel = new ViewModelProvider(this).get(TitleViewModel.class);
        genreViewModel = new ViewModelProvider(this).get(GenreViewModel.class);

        etTitle = findViewById(R.id.et_title);
        etViews = findViewById(R.id.et_views);
        etTitleFormat = findViewById(R.id.et_title_format);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        btnAddTitle = findViewById(R.id.btn_add_title);
        btnSelectGenres = findViewById(R.id.btn_select_genres);
        ivImagePreview = findViewById(R.id.iv_image_preview);
        CloudinaryConfig.initCloudinary(this);

        genreViewModel.getAllGenres().observe(this, genres -> {
            genreNames = new String[genres.size()];
            genreIds = new String[genres.size()];
            selectedGenres = new boolean[genres.size()];

            for (int i = 0; i < genres.size(); i++) {
                genreNames[i] = genres.get(i).getName();
                genreIds[i] = genres.get(i).getId();
            }

            btnSelectGenres.setOnClickListener(v -> showGenreSelectionDialog());
        });

        btnChooseImage.setOnClickListener(v -> chooseImage());
        btnAddTitle.setOnClickListener(v -> addTitle());
    }

    private void showGenreSelectionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Genres");
        builder.setMultiChoiceItems(genreNames, selectedGenres, (dialog, which, isChecked) -> {
            if (isChecked) {
                selectedGenreIds.add(genreIds[which]);
            } else {
                selectedGenreIds.remove(genreIds[which]);
            }
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            Toast.makeText(this, "Selected genres: " + selectedGenreIds.toString(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivImagePreview.setImageBitmap(bitmap);
                ivImagePreview.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addTitle() {
        String title = etTitle.getText().toString();
        String views = etViews.getText().toString();
        String titleFormat = etTitleFormat.getText().toString();

        if (title.isEmpty() || views.isEmpty() || titleFormat.isEmpty() || filePath == null || selectedGenreIds.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields, select genres, and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImageAndSaveTitle(title, views, titleFormat, selectedGenreIds);
    }

    private void uploadImageAndSaveTitle(String title, String views, String titleFormat, List<String> selectedGenreIds) {
        MediaManager.get().upload(filePath)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(AddTitleActivity.this, "Upload started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        coverImageUrl = resultData.get("secure_url").toString();
                        saveTitleToFirestore(title, views, titleFormat, selectedGenreIds);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(AddTitleActivity.this, "Upload error: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        Log.d("Error:", error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                    }
                })
                .dispatch();
    }

    private void saveTitleToFirestore(String title, String views, String titleFormat, List<String> selectedGenreIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("titles").document().getId();
        Title newTitle = new Title();
        newTitle.setId(id);
        newTitle.setTitle(title);
        newTitle.setCover(coverImageUrl);
        newTitle.setViews(Integer.parseInt(views));
        newTitle.setTitleFormat(titleFormat);
        newTitle.setGenreIds(selectedGenreIds);
        newTitle.setPubStatus("COMPLETED");
        newTitle.setUploadedDate(new Date());

        titleViewModel.addTitle(newTitle).observe(this, isSuccess -> {
            if (isSuccess) {
                String documentId = newTitle.getId();
                newTitle.setId(documentId);
                Toast.makeText(AddTitleActivity.this, "Title added successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddTitleActivity.this, ManageTitleActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AddTitleActivity.this, "Failed to add title", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
