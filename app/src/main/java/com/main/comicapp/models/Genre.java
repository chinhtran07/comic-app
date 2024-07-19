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
        if (ValidateUtil.validateObject(data)) {
            genre.setName(Objects.requireNonNull(data.get("name")).toString());
        }
        return genre;
    }
}
