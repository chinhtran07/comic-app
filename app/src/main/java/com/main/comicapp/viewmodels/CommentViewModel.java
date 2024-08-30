package com.main.comicapp.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.main.comicapp.models.Comment;
import com.main.comicapp.repositories.CommentRepository;
import com.main.comicapp.repositories.impl.CommentRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentViewModel extends ViewModel {
    private static final String TAG = "com.main.comicapp.viewmodels.CommentViewModel";
    private final CommentRepository commentRepository;
    private final MutableLiveData<List<Comment>> commentsLiveData;
    private final MutableLiveData<Map<String, String>> userNamesLiveData;
    private final MutableLiveData<Map<String, String>> titleNamesLiveData;

    public CommentViewModel() {
        commentRepository = new CommentRepositoryImpl();
        commentsLiveData = new MutableLiveData<>();
        userNamesLiveData = new MutableLiveData<>(new HashMap<>());
        titleNamesLiveData = new MutableLiveData<>(new HashMap<>());
    }

    public LiveData<List<Comment>> getCommentsLiveData() {
        return commentsLiveData;
    }

    public LiveData<Map<String, String>> getUserNamesLiveData() {
        return userNamesLiveData;
    }

    public LiveData<Map<String, String>> getTitleNamesLiveData() {
        return titleNamesLiveData;
    }

    public void fetchComments() {
        commentRepository.getComments().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Comment> comments = task.getResult().toObjects(Comment.class);
                commentsLiveData.setValue(comments);
                for (Comment comment : comments) {
                    fetchUserName(comment.getUserId());
                    fetchTitleName(comment.getTitleId());
                }
            } else {
                commentsLiveData.setValue(null);
            }
        });
    }

    public void fetchAllComments() {
        commentRepository.getAllComments().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Comment> comments = task.getResult().toObjects(Comment.class);
                commentsLiveData.setValue(comments);
                for (Comment comment : comments) {
                    fetchUserName(comment.getUserId());
                    fetchTitleName(comment.getTitleId());
                }
            } else {
                commentsLiveData.setValue(null);
            }
        });
    }

    public LiveData<List<Comment>> getCommentsByTitle(String titleId) {
        MutableLiveData<List<Comment>> commentsByTitleLiveData = new MutableLiveData<>();
        commentRepository.getComments().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Comment> comments = task.getResult().toObjects(Comment.class);
                List<Comment> filteredComments = new ArrayList<>();
                for (Comment comment : comments) {
                    if (comment.getTitleId().equals(titleId)) {
                        filteredComments.add(comment);
                        fetchUserName(comment.getUserId());
                        fetchTitleName(comment.getTitleId());
                    }
                }
                commentsByTitleLiveData.setValue(filteredComments);
            } else {
                commentsByTitleLiveData.setValue(null);
            }
        });
        return commentsByTitleLiveData;
    }

    private void fetchUserName(String userId) {
        commentRepository.getUserById(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String userName = task.getResult().getString("username");
                Map<String, String> userNames = userNamesLiveData.getValue();
                userNames.put(userId, userName);
                userNamesLiveData.setValue(userNames);
            }
        });
    }

    private void fetchTitleName(String titleId) {
        commentRepository.getTitleById(titleId).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String titleName = task.getResult().getString("title");
                Map<String, String> titleNames = titleNamesLiveData.getValue();
                titleNames.put(titleId, titleName);
                titleNamesLiveData.setValue(titleNames);
            }
        });
    }

    public void updateStatusComment(String commentId, boolean status) {
        commentRepository.updateStatusComment(commentId, status).addOnCompleteListener(task -> {
            fetchComments();
        });
    }

    public void createComment(Comment comment) {
        commentRepository.createComment(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Create comment - success : Comment created successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Create comment - failed: Comment created unsuccessfully");
            }
        });
    }
}
