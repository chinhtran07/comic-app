package com.main.comicapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.util.Date;

public class AddChapterActivity extends AppCompatActivity {

    private EditText editTextChapterNumber, editTextDescription, editTextTitleId;
    private Button buttonAddChapter;
    private ChapterViewModel chapterViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);

        editTextChapterNumber = findViewById(R.id.edit_text_chapter_number);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextTitleId = findViewById(R.id.edit_text_title_id);
        buttonAddChapter = findViewById(R.id.button_add_chapter);

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);

        buttonAddChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChapter();
            }
        });
    }

    private void addChapter() {
        int chapterNumber = Integer.parseInt(editTextChapterNumber.getText().toString());
        String description = editTextDescription.getText().toString();
        String titleId = editTextTitleId.getText().toString();
        Date uploadedDate = new Date(); // Lấy ngày hiện tại

        Chapter chapter = new Chapter(chapterNumber, description, uploadedDate, titleId);
        chapterViewModel.addChapter(chapter);

        Toast.makeText(this, "Chapter added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
