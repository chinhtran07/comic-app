package com.main.comicapp.dto;

import com.main.comicapp.models.Title;
import com.main.comicapp.utils.FirebaseUtils;

import java.util.List;

public interface TitleDTO {
    void getAllTitleRealtime(FirebaseUtils.DataFetchListener<Title> listener);
    Title getTitle(String id);
}
