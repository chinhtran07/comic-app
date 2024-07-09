package com.main.comicapp.models;

import com.google.firebase.firestore.PropertyName;

public class TitleAuthor {
    @PropertyName("author_id")
    private String author;
    @PropertyName("title_id")
    private String title;
}
