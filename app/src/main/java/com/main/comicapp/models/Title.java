package com.main.comicapp.models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.main.comicapp.enums.PubStatus;
import com.main.comicapp.enums.TitleFormat;
import com.main.comicapp.viewmodels.GenreViewModel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Title extends BaseModel implements Serializable {
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public static Title toObject(Map<String, Object> data, String id) {
        GenreViewModel genreViewModel = new GenreViewModel();
        Title title = new Title();
        title.setId(id);
        if (validateObject(data)) {
            title.setTitle(Objects.requireNonNull(data.get("title")).toString());
            title.setCover(data.get("cover").toString());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date uploadedDate = formatter.parse(Objects.requireNonNull(data.get("uploadedDate")).toString());
                title.setUploadedDate(uploadedDate);
            } catch (ParseException e) {
                Log.e("Error", "onEvent: Parse Exception", e);
            }
            title.setPubStatus((String)data.get("pubStatus"));
            title.setTitleFormat((String)data.get("titleFormat"));

            List<Genre> genres = new ArrayList<>();

            List<String> genreIds = (List<String>) data.get("genres");
            assert genreIds != null;
            for (String genreId : genreIds) {
                genres.add(genreViewModel.getGenre(id).getValue());
            }

            title.setGenres(genres);
        }
        return title;
    }

}
