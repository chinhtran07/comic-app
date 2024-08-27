package com.main.comicapp.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.R;
import com.main.comicapp.activities.BaseActivity;
import com.main.comicapp.models.Comment;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.CommentViewModel;

import java.util.Date;
import java.util.UUID;

public class CommentActivity extends BaseActivity {

    private EditText etCommentContent;
    private Button btnConfirm;
    private Button btnCancel;
    private CommentViewModel commentViewModel;
    private Title title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        etCommentContent = findViewById(R.id.activity_comment_content_field);
        btnConfirm = findViewById(R.id.activity_comment_content_button_confirm);
        btnCancel = findViewById(R.id.activity_comment_content_button_cancel);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        title = (Title) getIntent().getSerializableExtra("title");

        btnConfirm.setOnClickListener(v -> {
            String textContent = etCommentContent.getText().toString();
            Intent intent = getIntent();
            if (!textContent.isEmpty()) {
                Comment comment = new Comment();
                comment.setId(UUID.randomUUID().toString());
                comment.setText(textContent);
                assert title != null;
                comment.setTitleId(title.getId());
                comment.setUserId(currentUserSession.getUserId());
                comment.setBaseCommentId(null);
                comment.setUploadedDate(new Date());
                commentViewModel.createComment(comment);
                intentToTitleDetail(title);
            }
            else {
                etCommentContent.setError("Comment content cannot be empty");
            }
        });

        btnCancel.setOnClickListener(v -> {
            intentToTitleDetail(title);
        });
    }

    private void intentToTitleDetail(Title title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Comment");
        builder.setMessage("Comment created successfully");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), TitleDetailActivity.class);
            intent.putExtra("title", title);
            startActivity(intent);
            finish();
        });
        builder.create().show();
    }

}