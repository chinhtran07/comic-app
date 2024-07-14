package com.main.comicapp.models;

import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.enums.TitleFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Title implements Serializable {
    private String id;
    private String name;
    private Date uploadedDate;
    private String cover;
    private int views;
    private PubStatus pubStatus;
    private TitleFormat titleFormat;
    private List<Genre> genres;

    public Title() {

    }

    public Title(String name, Date uploadedDate, String cover, int views, PubStatus pubStatus, TitleFormat titleFormat, List<Genre> genres) {
        this.name = name;
        this.uploadedDate = uploadedDate;
        this.cover = cover;
        this.views = views;
        this.pubStatus = pubStatus;
        this.titleFormat = titleFormat;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Date createdDate) {
        this.uploadedDate = createdDate;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public PubStatus getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(PubStatus pubStatus) {
        this.pubStatus = pubStatus;
    }

    public TitleFormat getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(TitleFormat titleFormat) {
        this.titleFormat = titleFormat;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


}
