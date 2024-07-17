package com.main.comicapp.dtos;

import android.annotation.SuppressLint;
import android.util.Log;

import com.main.comicapp.models.Genre;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.GenreViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TitleDto extends BaseDTO {

    private final GenreViewModel genreViewModel;
    private static TitleDto instance;

    private TitleDto() {
        genreViewModel = new GenreViewModel();
    }

    public static synchronized TitleDto getInstance() {
        if (instance == null)
            instance =  new TitleDto();
        return instance;
    }

    public Title toObject(Map<String, Object> data, String id) {
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
