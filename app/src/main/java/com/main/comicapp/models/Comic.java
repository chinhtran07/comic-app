package com.main.comicapp.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

public class Comic {
    private String name;
    private String cover;

    @PropertyName("created_date")
    private String createdDate;
    private boolean picture;

    @PropertyName("pub_status")
    private String pubStatus;
    private int views;

    @PropertyName("category_id")
    private String cateId;

    public Comic() {

    }

    public Comic(String name, String cover, String createdDate, Boolean picture, String pubStatus, int views, String cateId) {
        this.name = name;
        this.cover = cover;
        this.createdDate = createdDate;
        this.picture = picture;
        this.pubStatus = pubStatus;
        this.views = views;
        this.cateId = cateId;
    }

    public Comic(String name, String coverUrl) {
        this.setName(name);
        this.setCover(coverUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture = picture;
    }

    public String getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(String pubStatus) {
        this.pubStatus = pubStatus;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }
}
