package com.main.comicapp.activities;

import android.os.Bundle;
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

        rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, null, new HashMap<>(), new HashMap<>());
        commentAdapter.setListener(this);
        rvComments.setAdapter(commentAdapter);

        // Sử dụng ViewModelProvider để khởi tạo CommentViewModel
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        // Quan sát dữ liệu từ ViewModel
        commentViewModel.getCommentsLiveData().observe(this, comments -> {
            commentAdapter.setComments(comments);
        });

        commentViewModel.getUserNamesLiveData().observe(this, userNames -> {
            commentAdapter.setUserNames(userNames);
        });

        commentViewModel.getTitleNamesLiveData().observe(this, titleNames -> {
            commentAdapter.setTitleNames(titleNames);
        });

        // Fetch comments from ViewModel
        commentViewModel.fetchComments();
    }

    @Override
    public void onEditClick(Comment comment) {
        // Handle edit comment
    }

    @Override
    public void onDeleteClick(Comment comment) {
        // Handle delete comment
        commentViewModel.deleteComment(comment.getId());
    }
}
