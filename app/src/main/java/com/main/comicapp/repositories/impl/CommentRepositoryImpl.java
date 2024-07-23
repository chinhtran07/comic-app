package com.main.comicapp.repositories.impl;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.Comment;
import com.main.comicapp.repositories.CommentRepository;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepository {
    @Override
    public LiveData<Comment> getComment(String id) {
        return null;
    }

    @Override
    public void addComment(Comment comment) {

    }

    @Override
    public void updateComment(String id, Comment comment) {

    }

    @Override
    public void deleteComment(String id) {

    }

    @Override
    public LiveData<List<Comment>> getCommentsByChapter(String chapterId) {
        return null;
    }

    @Override
    public LiveData<List<Comment>> getCommentsByUser(String userId) {
        return null;
    }
}
