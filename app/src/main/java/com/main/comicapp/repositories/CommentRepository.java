package com.main.comicapp.repositories;

import androidx.lifecycle.LiveData;

import com.main.comicapp.models.Comment;

import java.util.List;

public interface CommentRepository {
    LiveData<Comment> getComment(String id);
    void addComment(Comment comment);
    void updateComment(String id, Comment comment);
    void deleteComment(String id);
    LiveData<List<Comment>> getCommentsByChapter(String chapterId);
    LiveData<List<Comment>> getCommentsByUser(String userId);
}
