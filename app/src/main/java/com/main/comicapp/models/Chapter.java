package com.main.comicapp.models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.main.comicapp.utils.ValidateUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

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

    public static Chapter toObject(Map<String, Object> data, String id) {
        Chapter chapter = new Chapter();
        chapter.setId(id);
        if (ValidateUtil.validateObject(data)) {
            chapter.setChapterNumber(((Long) Objects.requireNonNull(data.get("chapterNumber"))).intValue());
            chapter.setDescription(Objects.requireNonNull(data.get("description")).toString());

            Object uploadedDateObj = data.get("uploadedDate");
            if (uploadedDateObj instanceof Timestamp) {
                chapter.setUploadedDate(((Timestamp) uploadedDateObj).toDate());
            } else if (uploadedDateObj instanceof String) {
                try {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date uploadedDate = formatter.parse(uploadedDateObj.toString());
                    chapter.setUploadedDate(uploadedDate);
                } catch (Exception e) {
                    Log.e("Error", "onEvent: Parse Exception", e);
                }
            }

            chapter.setTitleId(Objects.requireNonNull(data.get("titleId")).toString());
        }
        return chapter;
    }
}
