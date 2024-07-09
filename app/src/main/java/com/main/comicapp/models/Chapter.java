package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Chapter {
    @PropertyName("chapter_number")
    private int chapterNumber;
    @PropertyName("content")
    private String description;
    @PropertyName("uploaded_date")
    private Date uploadedDate;
    @PropertyName("title_id")
    private String titleId;

    public Chapter() {

    }

    public Chapter(int chapterNumber, String description, Date uploadedDate, String titleId) {
        this.chapterNumber = chapterNumber;
        this.description = description;
        this.uploadedDate = uploadedDate;
        this.titleId = titleId;
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
