package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;

public class Chapter implements Serializable {
    private String id;
    private int chapterNumber;
    private String description;
    private Date uploadedDate;
    private String titleId;

    public Chapter() {

    }

    public Chapter(int chapterNumber, String description, Date uploadedDate, String titleId) {
        this.chapterNumber = chapterNumber;
        this.description = description;
        this.uploadedDate = uploadedDate;
        this.titleId = titleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
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
}
