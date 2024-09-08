package com.main.comicapp.activities.admin;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.main.comicapp.R;
import com.main.comicapp.enums.TitleFormat;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class UpdateChapterActivity extends AppCompatActivity {

    private EditText editTextChapterNumber, editTextDescription;
    private Button buttonUpdateChapter, buttonSelectContent, buttonManagePages;
    private RadioGroup radioGroupContentType;
    private TextView textViewSelectedFiles;
    private ChapterViewModel chapterViewModel;
    private String chapterId;
    private String titleId;  // Khai báo biến titleId để lưu giữ titleId ban đầu
    private int chapterNumber;
    private FirebaseStorage storage;
    private TitleFormat titleFormat;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;

    private File textFileData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_chapter);

        editTextChapterNumber = findViewById(R.id.edit_text_chapter_number);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonUpdateChapter = findViewById(R.id.button_update_chapter);
        buttonSelectContent = findViewById(R.id.button_select_content);
        buttonManagePages = findViewById(R.id.button_manage_pages);
        radioGroupContentType = findViewById(R.id.radio_group_content_type);
        textViewSelectedFiles = findViewById(R.id.text_view_selected_files);

        storage = FirebaseStorage.getInstance();

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
        chapterId = getIntent().getStringExtra("chapter_id");
        titleFormat = (TitleFormat) getIntent().getSerializableExtra("title_format");

        if (chapterId != null) {
            loadChapterDetails(chapterId);
        }

        buttonUpdateChapter.setOnClickListener(v -> {
            if (validateInputs()) {
                updateChapter();
            }
        });

        buttonSelectContent.setOnClickListener(v -> {
            int selectedType = radioGroupContentType.getCheckedRadioButtonId();
            if (selectedType == R.id.radio_image) {
                openImagePicker();
            } else if (selectedType == R.id.radio_file) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Vui lòng chọn loại nội dung", Toast.LENGTH_SHORT).show();
            }
        });

        buttonManagePages.setOnClickListener(v -> {
            if (titleFormat.equals(TitleFormat.NOVEL)) {
                Toast.makeText(this, "Truyện không hợp lệ để thêm trang", Toast.LENGTH_LONG).show();
            }
            else if (titleFormat.equals(TitleFormat.COMIC)) {
                openPageManager();
            }
            else {
                Toast.makeText(this, "Truyện không hợp lệ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadChapterDetails(String chapterId) {
        chapterViewModel.getChapter(chapterId).observe(this, chapter -> {
            if (chapter != null) {
                editTextChapterNumber.setText(String.valueOf(chapter.getChapterNumber()));
                editTextDescription.setText(chapter.getDescription());
                titleId = chapter.getTitleId();  // Lưu giữ titleId từ chương đã tải
                chapterNumber = chapter.getChapterNumber();
            } else {
                Toast.makeText(UpdateChapterActivity.this, "Không thể tải thông tin chương", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private boolean validateInputs() {
        String chapterNumberStr = editTextChapterNumber.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (chapterNumberStr.isEmpty()) {
            editTextChapterNumber.setError("Vui lòng nhập số chương");
            return false;
        }
        if (description.isEmpty()) {
            editTextDescription.setError("Vui lòng nhập mô tả chương");
            return false;
        }

        try {
            Integer.parseInt(chapterNumberStr);
        } catch (NumberFormatException e) {
            editTextChapterNumber.setError("Số chương phải là một số hợp lệ");
            return false;
        }

        return true;
    }

    private void updateChapter() {
        int chapterNumber = Integer.parseInt(editTextChapterNumber.getText().toString().trim());
        String description = editTextDescription.getText().toString().trim();

        Chapter chapter = new Chapter(chapterNumber, description, new Date(), titleId); // Sử dụng lại titleId từ chương đã tải
        chapter.setId(chapterId);

        if (textFileData != null) {
            StorageReference fileRef = storage.getReference().child(titleId + "/" + textFileData.getName());
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("text/plain")
                    .build();
            UploadTask uploadTask = fileRef.putFile(Uri.fromFile(textFileData), metadata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("com.main.comicapp", "onFailure: " + e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("com.main.comicapp", "onFailure: " + taskSnapshot.getMetadata());
                }
            });
        }

        chapterViewModel.updateChapter(chapterId, chapter);

        Toast.makeText(this, "Chapter đã được cập nhật", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private void openPageManager() {
        Intent intent = new Intent(this, AdminManagementPage.class);
        intent.putExtra("chapter_id", chapterId);
        intent.putExtra("title_id", titleId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            StringBuilder selectedFiles = new StringBuilder();
            if (requestCode == PICK_IMAGE_REQUEST || requestCode == PICK_FILE_REQUEST) {
                if (data.getClipData() != null) {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
                        selectedFiles.append(fileUri.getPath()).append("\n");
                    }
                } else if (data.getData() != null) {
                    Uri fileUri = data.getData();
                    selectedFiles.append(fileUri.getPath()).append("\n");
                    try {
                        textFileData = createFile(fileUri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                textViewSelectedFiles.setText(selectedFiles.toString());
            }
        }
    }

    private File createFile(Uri filePath) throws IOException {
        StringBuilder buffer = new StringBuilder();
        File newFile = new File(this.getCacheDir(), "ch" + chapterNumber + ".txt");
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(filePath, "r");
            assert pfd != null;
            FileInputStream inputStream = new FileInputStream(pfd.getFileDescriptor());
            FileOutputStream outputStream = new FileOutputStream(newFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            outputStream.write(buffer.toString().getBytes());
            outputStream.close();
            Log.d("com.main.comicapp", "createFile: " + buffer);
            reader.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return newFile;
    }
}
