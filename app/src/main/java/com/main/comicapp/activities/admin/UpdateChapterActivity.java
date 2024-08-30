package com.main.comicapp.activities.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.util.Date;

public class UpdateChapterActivity extends AppCompatActivity {

    private EditText editTextChapterNumber, editTextDescription;
    private Button buttonUpdateChapter, buttonSelectContent;
    private RadioGroup radioGroupContentType;
    private TextView textViewSelectedFiles;
    private ChapterViewModel chapterViewModel;
    private String chapterId;
    private String titleId;  // Khai báo biến titleId để lưu giữ titleId ban đầu

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_chapter);

        editTextChapterNumber = findViewById(R.id.edit_text_chapter_number);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonUpdateChapter = findViewById(R.id.button_update_chapter);
        buttonSelectContent = findViewById(R.id.button_select_content);
        radioGroupContentType = findViewById(R.id.radio_group_content_type);
        textViewSelectedFiles = findViewById(R.id.text_view_selected_files);

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);
        chapterId = getIntent().getStringExtra("chapter_id");

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
    }

    private void loadChapterDetails(String chapterId) {
        chapterViewModel.getChapter(chapterId).observe(this, chapter -> {
            if (chapter != null) {
                editTextChapterNumber.setText(String.valueOf(chapter.getChapterNumber()));
                editTextDescription.setText(chapter.getDescription());
                titleId = chapter.getTitleId();  // Lưu giữ titleId từ chương đã tải
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_FILE_REQUEST);
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
                }
                textViewSelectedFiles.setText(selectedFiles.toString());
            }
        }
    }
}
