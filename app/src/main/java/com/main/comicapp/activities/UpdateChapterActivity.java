package com.main.comicapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.models.Chapter;
import com.main.comicapp.viewmodels.ChapterViewModel;

import java.util.Date;

public class UpdateChapterActivity extends AppCompatActivity {

    private EditText editTextChapterNumber, editTextDescription, editTextTitleId;
    private Button buttonUpdateChapter;
    private ChapterViewModel chapterViewModel;
    private String chapterId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_chapter);

        editTextChapterNumber = findViewById(R.id.edit_text_chapter_number);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextTitleId = findViewById(R.id.edit_text_title_id);
        buttonUpdateChapter = findViewById(R.id.button_update_chapter);

        chapterViewModel = new ViewModelProvider(this).get(ChapterViewModel.class);

        chapterId = getIntent().getStringExtra("chapter_id");
        if (chapterId != null) {
            loadChapterDetails(chapterId);
        }

        buttonUpdateChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateChapter();
            }
        });
    }

    private void loadChapterDetails(String chapterId) {
        chapterViewModel.getChapter(chapterId).observe(this, new Observer<Chapter>() {
            @Override
            public void onChanged(Chapter chapter) {
                if (chapter != null) {
                    editTextChapterNumber.setText(String.valueOf(chapter.getChapterNumber()));
                    editTextDescription.setText(chapter.getDescription());
                    editTextTitleId.setText(chapter.getTitleId());
                }
            }
        });
    }

    private void updateChapter() {
        int chapterNumber = Integer.parseInt(editTextChapterNumber.getText().toString());
        String description = editTextDescription.getText().toString();
        String titleId = editTextTitleId.getText().toString();

        Chapter chapter = new Chapter(chapterNumber, description, new Date(), titleId);
        chapter.setId(chapterId);
        chapterViewModel.updateChapter(chapterId, chapter);

        Toast.makeText(this, "Chapter updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}
