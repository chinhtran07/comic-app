package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;
import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.enums.TitleFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Title implements Serializable {
    private String id;
    private String title;
    private Date uploadedDate;
    private String cover;
    private int views;
    private PubStatus pubStatus;
    private TitleFormat titleFormat;
    private List<Genre> genres;

    public Title() {

    }

    public Title(String title, Date uploadedDate, String cover, int views, String pubStatus, String titleFormat, List<Genre> genres) {
        this.title = title;
        this.uploadedDate = uploadedDate;
        this.cover = cover;
        this.views = views;
        this.pubStatus = PubStatus.valueOf(pubStatus);
        this.titleFormat = TitleFormat.valueOf(titleFormat);
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getCoverUrl() {
        return cover;
    }

    public void setCoverUrl(String cover) {
        this.cover = cover;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getPubStatus() {
        return pubStatus.name();
    }

    public void setPubStatus(String pubStatus) {
        this.pubStatus = PubStatus.valueOf(pubStatus);
    }

    public String getTitleFormat() {
        return titleFormat.name();
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = TitleFormat.valueOf(titleFormat);
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


}
