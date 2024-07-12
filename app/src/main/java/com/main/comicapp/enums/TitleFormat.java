package com.main.comicapp.enums;

import androidx.annotation.NonNull;

public enum TitleFormat {
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
