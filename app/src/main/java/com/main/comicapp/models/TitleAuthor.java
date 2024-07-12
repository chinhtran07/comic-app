package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class TitleAuthor implements Serializable {
    private String id;
    @PropertyName("author_id")
    private String author;
    @PropertyName("title_id")
    private String title;
}
