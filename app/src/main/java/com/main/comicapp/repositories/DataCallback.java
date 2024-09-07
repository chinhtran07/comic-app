package com.main.comicapp.repositories;

public interface DataCallback<T> {
    void onSuccess(T data);
    void onFailure(Exception e);
}
