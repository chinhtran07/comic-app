package com.main.comicapp.activities.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.CommentAdapter;
import com.main.comicapp.models.Comment;
import com.main.comicapp.viewmodels.CommentViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageCommentActivity extends AppCompatActivity implements CommentAdapter.OnCommentClickListener {

    private CommentViewModel commentViewModel;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private List<Comment> allComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comment);

        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);

        rvComments = findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, null, new HashMap<>(), new HashMap<>(), commentViewModel);
        commentAdapter.setListener(this);
        rvComments.setAdapter(commentAdapter);

        commentViewModel.getCommentsLiveData().observe(this, comments -> {
            allComments = comments;
            commentAdapter.setComments(comments);
        });

        commentViewModel.getUserNamesLiveData().observe(this, userNames -> {
            commentAdapter.setUserNames(userNames);
        });

        commentViewModel.getTitleNamesLiveData().observe(this, titleNames -> {
            commentAdapter.setTitleNames(titleNames);
        });

        commentViewModel.fetchComments();
        EditText etSearchComment = findViewById(R.id.et_search_comment);
        etSearchComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCommentsByUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterCommentsByUsername(String query) {
        List<Comment> filteredComments = new ArrayList<>();
        Map<String, String> userNames = commentViewModel.getUserNamesLiveData().getValue();

        if (userNames != null && allComments != null) {
            for (Comment comment : allComments) {
                String userName = userNames.get(comment.getUserId());
                if (userName != null && userName.toLowerCase().contains(query.toLowerCase())) {
                    filteredComments.add(comment);
                }
            }
        }

        commentAdapter.setComments(filteredComments);
    }

    @Override
    public void onDeleteClick(Comment comment) {
        Log.d("CMT id: ", comment.getId());
        Log.d("Status: ", String.valueOf(comment.getIsActive()));
        commentViewModel.updateStatusComment(comment.getId(), comment.getIsActive());
    }
}
