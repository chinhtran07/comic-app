package com.main.comicapp.models;

import android.net.Uri;

public class Comic {
    private String title;
    private String coverUrl;

    public Comic(String title, String coverUrl) {
        this.title = title;
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUri() {
        return coverUrl;
    }
}
