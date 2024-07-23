package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.main.comicapp.models.Comment;
import com.main.comicapp.repositories.CommentRepository;
import com.main.comicapp.repositories.impl.CommentRepositoryImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentViewModel extends ViewModel {
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

    public void deleteComment(String commentId) {
        commentRepository.deleteComment(commentId).addOnCompleteListener(task -> {
            fetchComments();
        });
    }
}
