package com.main.comicapp.models;

import com.main.comicapp.utils.ValidateUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class Genre implements Serializable {
    private String id;
    private String name;

    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public Genre(String id, String name) {
        this.name = name;
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

    public static Genre toObject(Map<String, Object> data, String id) {
        Genre genre = new Genre();
        genre.setId(id);
        if (data != null && data.containsKey("name") && data.get("name") != null) {
            genre.setName(data.get("name").toString());
        } else {
            genre.setName("Unknown");
        }
        return genre;
    }

}
