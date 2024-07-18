package com.main.comicapp.models;

import java.io.Serializable;

public class Page implements Serializable {
    private String id;
    private int pageNumber;
    private String imagePath;
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
