package com.main.comicapp.models;

import com.google.firebase.Timestamp;
import com.main.comicapp.utils.ValidateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comment implements Serializable {
    private String id;
    private String text;
    private String baseCommentId;
    private String userId;
    private String titleId;
    private Date uploadedDate;
    private Boolean isActive;

    public Comment() {
        this.isActive = true;
    }

    public Comment(String text, String baseCommendId, String userId, String titleId) {
        this.text = text;
        this.baseCommentId = baseCommendId;
        this.userId = userId;
        this.titleId = titleId;
        this.isActive = true;
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

    public Date getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public static Comment toObject(Map<String, Object> data, String id) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setBaseCommentId((String) data.get("baseCommentId"));
        data.remove("baseCommentId");
        if (ValidateUtil.validateObject(data)) {
            comment.setText((String) data.get("text"));
            comment.setUserId((String) data.get("userId"));
            comment.setTitleId((String) data.get("titleId"));
            Timestamp uploadedDate = (Timestamp) data.get("uploadedDate");
            if (uploadedDate != null)
                comment.setUploadedDate(uploadedDate.toDate());
            Boolean isActive = (Boolean) data.get("isActive");
            if (isActive != null) {
                comment.setIsActive(isActive);
            } else {
                comment.setIsActive(true);
            }
        }
        return comment;
    }

    public static Map<String, Object> toMap(Comment comment) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", comment.getId());
        data.put("text", comment.getText());
        data.put("baseCommentId", comment.getBaseCommentId());
        data.put("userId", comment.getUserId());
        data.put("titleId", comment.getTitleId());
        data.put("isActive", comment.getIsActive());
        Timestamp uploadedDate = new Timestamp(comment.getUploadedDate());
        data.put("uploadedDate", uploadedDate);
        return data;
    }
}
