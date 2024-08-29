package com.main.comicapp.models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.enums.TitleFormat;
import com.main.comicapp.utils.ValidateUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Title implements Serializable {
    private String id;
    private String title;
    private Date uploadedDate;
    private String cover;
    private int views;
    private PubStatus pubStatus;
    private TitleFormat titleFormat;
    private List<String> genreIds;

    public Title() {
    }

    public Title(String title, Date uploadedDate, String cover, int views, String pubStatus, String titleFormat, List<String> genreIds) {
        this.title = title;
        this.uploadedDate = uploadedDate;
        this.cover = cover;
        this.views = views;
        this.pubStatus = PubStatus.valueOf(pubStatus);
        this.titleFormat = TitleFormat.valueOf(titleFormat);
        this.genreIds = genreIds;
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

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genres) {
        this.genreIds = genres;
    }

    public void setGenre(String genreId) {
        this.genreIds = new ArrayList<>();
        this.genreIds.add(genreId);
    }

    public static Title toObject(Map<String, Object> data, String id) throws ClassCastException {
        Title title = new Title();
        title.setId(id);
        if (ValidateUtil.validateObject(data)) {
            title.setTitle(Objects.requireNonNull(data.get("title")).toString());
            title.setCover(Objects.requireNonNull(data.get("cover")).toString());

            Object uploadedDateObj = data.get("uploadedDate");
            if (uploadedDateObj instanceof com.google.firebase.Timestamp) {
                title.setUploadedDate(((com.google.firebase.Timestamp) uploadedDateObj).toDate());
            } else if (uploadedDateObj instanceof String) {
                try {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                    Date uploadedDate = formatter.parse(uploadedDateObj.toString());
                    title.setUploadedDate(uploadedDate);
                } catch (ParseException e) {
                    Log.e("Error", "onEvent: Parse Exception", e);
                }
            }

            title.setViews(((Long) Objects.requireNonNull(data.get("views"))).intValue());
            title.setPubStatus(Objects.requireNonNull(data.get("pubStatus")).toString());
            title.setTitleFormat(Objects.requireNonNull(data.get("titleFormat")).toString());

            List<String> genreIds = (List<String>) data.get("genres");
            if (genreIds != null) {
                title.setGenreIds(genreIds);
            } else {
                title.setGenreIds(new ArrayList<>());
            }
        }
        return title;
    }
}
