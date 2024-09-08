package com.main.comicapp.enums;

import androidx.annotation.NonNull;

import java.io.Serializable;

public enum TitleFormat implements Serializable {
    COMIC,
    NOVEL;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case COMIC:
                return "Truyện tranh";
            case NOVEL:
                return "Truyện chữ";
        }
        return "";
    }
}
