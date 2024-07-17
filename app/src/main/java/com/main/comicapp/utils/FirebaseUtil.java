package com.main.comicapp.utils;

import android.annotation.SuppressLint;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore firestore;

    public static FirebaseFirestore getFirestore() {
        if (firestore == null)
            firestore = FirebaseFirestore.getInstance();
        return firestore;
    }
}
