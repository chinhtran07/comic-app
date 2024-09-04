package com.main.comicapp.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ReadingHistory implements Serializable {
    private String id;
    private String userId;
    private String titleId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadingHistory that = (ReadingHistory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(titleId, that.titleId) &&
                Objects.equals(lastTimeReading, that.lastTimeReading);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, titleId, lastTimeReading);
    }

    @Override
    public String toString() {
        return "ReadingHistory{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", titleId='" + titleId + '\'' +
                ", lastTimeReading=" + lastTimeReading +
                '}';
    }
}
