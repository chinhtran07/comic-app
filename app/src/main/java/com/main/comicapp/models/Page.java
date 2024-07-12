package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Page implements Serializable {
    private String id;
    @PropertyName("page_number")
    private int pageNumber;
    @PropertyName("image_path")
    private String imagePath;
    @PropertyName("chapter_id")
    private String chapterId;

    public Page() {

    }

    public Page(int pageNumber, String imagePath, String chapterId) {
        this.pageNumber = pageNumber;
        this.imagePath = imagePath;
        this.chapterId = chapterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
}
