package com.main.comicapp.activities.admin;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.main.comicapp.R;
import com.main.comicapp.adapters.CommentAdapter;
import com.main.comicapp.models.Comment;
import com.main.comicapp.viewmodels.CommentViewModel;

import java.util.HashMap;

public class ManageCommentActivity extends AppCompatActivity implements CommentAdapter.OnCommentClickListener {

    private CommentViewModel commentViewModel;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comment);

        // Khởi tạo RecyclerView và Adapter
        rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, null, new HashMap<>(), new HashMap<>());
        commentAdapter.setListener(this);
        rvComments.setAdapter(commentAdapter);

        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        commentViewModel.getCommentsLiveData().observe(this, comments -> {
            commentAdapter.setComments(comments);
        });

        commentViewModel.getUserNamesLiveData().observe(this, userNames -> {
            commentAdapter.setUserNames(userNames);
        });

        commentViewModel.getTitleNamesLiveData().observe(this, titleNames -> {
            commentAdapter.setTitleNames(titleNames);
        });

        commentViewModel.fetchComments();
    }

    @Override
    public void onEditClick(Comment comment) {
    }
    @Override
    public void onDeleteClick(Comment comment) {
        Log.d("CMT id: ", comment.getId());
        Log.d("Status: ", String.valueOf(comment.getIsActive()));
        commentViewModel.updateStatusComment(comment.getId(), comment.getIsActive());
    }
}
