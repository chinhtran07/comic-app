package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.Comment;
import com.main.comicapp.repositories.CommentRepository;
import com.main.comicapp.repositories.impl.CommentRepositoryImpl;

import java.util.List;

public class CommentViewModel extends ViewModel {
    private final CommentRepository commentRepository;

    public CommentViewModel() {
        commentRepository = CommentRepositoryImpl.getInstance();
    }

    public void addComment(Comment comment) {
        commentRepository.addComment(comment);
    }

    public void updateComment(String id, Comment comment) {
        commentRepository.updateComment(id, comment);
    }

    public void deleteComment(String id) {}

    public LiveData<List<Comment>> getCommentsByTitle(String titleId) {
        return commentRepository.getCommentsByTitle(titleId);
    }
}
