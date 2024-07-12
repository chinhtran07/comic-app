package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;

public class ReadingHistory implements Serializable {
    private String id;
    @PropertyName("user_id")
    private String userId;
    @PropertyName("title_id")
    private String titleId;
    @PropertyName("lastest_read")
    private Date lastTimeReading;

    public ReadingHistory() {

    }

    public ReadingHistory(String userId, String titleId, Date lastTimeReading) {
        this.userId = userId;
        this.titleId = titleId;
        this.lastTimeReading = lastTimeReading;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getLastTimeReading() {
        return lastTimeReading;
    }

    public void setLastTimeReading(Date lastTimeReading) {
        this.lastTimeReading = lastTimeReading;
    }
}
