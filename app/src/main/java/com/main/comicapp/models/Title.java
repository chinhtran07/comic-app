package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;
import java.util.Set;

public class Title {
    @PropertyName("name")
    private String title;
    @PropertyName("created_date")
    private Date createdDate;
    @PropertyName("cover")
    private String coverUrl;
    private int views;
    @PropertyName("pub_status")
    private PubStatus pubStatus;
    @PropertyName("format")
    private TitleFormat titleFormat;
    private Set<Genre> genres;

    public Title() {

    }

    public Title(String title, Date createdDate, String coverUrl, int views, String pubStatus, String titleFormat, Set<Genre> genres) {
        this.title = title;
        this.createdDate = createdDate;
        this.coverUrl = coverUrl;
        this.views = views;
        this.pubStatus = PubStatus.valueOf(pubStatus);
        this.titleFormat = TitleFormat.valueOf(titleFormat);
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUploadedDate() {
        return createdDate;
    }

    public void setUploadedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    private enum PubStatus {
        ONGOING, COMPLETED
    }

    private enum TitleFormat {
        COMIC, NOVEL
    }
}
