package com.main.comicapp.models;

import java.io.Serializable;

public class Comment implements Serializable {
    private String id;
    private String text;
    private String baseCommentId;
    private String userId;
    private String titleId;

    public Comment() {

    }

    public Comment(String text, String baseCommendId, String userId, String titleId) {
        this.text = text;
        this.baseCommentId = baseCommendId;
        this.userId = userId;
        this.titleId = titleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBaseCommentId() {
        return baseCommentId;
    }

    public void setBaseCommentId(String baseCommentId) {
        this.baseCommentId = baseCommentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }
}
