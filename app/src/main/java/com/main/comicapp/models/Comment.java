package com.main.comicapp.models;

import com.main.comicapp.utils.ValidateUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    public static Comment toObject(Map<String, Object> data, String id) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setBaseCommentId((String)data.get("baseCommentId"));
        data.remove("baseCommentId");
        if (ValidateUtil.validateObject(data)) {
            comment.setText((String)data.get("text"));
            comment.setUserId((String)data.get("userId"));
            comment.setTitleId((String)data.get("titleId"));
        }
        return comment;
    }

    public static Map<String, String> toMap(Comment comment) {
        Map<String, String> data = new HashMap<>();
        data.put("text", comment.getText());
        data.put("baseCommentId", comment.getBaseCommentId());
        data.put("userId", comment.getUserId());
        data.put("titleId", comment.getTitleId());
        return data;
    }
}
