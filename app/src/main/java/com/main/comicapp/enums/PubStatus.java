package com.main.comicapp.enums;

import androidx.annotation.NonNull;

public enum PubStatus {
    ONGOING,
    COMPLETED;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case ONGOING:
                return "Đang xuất bản";
            case COMPLETED:
                return "Đã hoàn thành";
        }
        return "";
    }
}
