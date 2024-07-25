package com.main.comicapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReadingPositionViewModel extends AndroidViewModel {

    private final FirebaseFirestore firestore;
    private final String userId;

    public ReadingPositionViewModel(@NonNull Application application) {
        super(application);
        this.firestore = FirebaseFirestore.getInstance();
        this.userId = "1";
    }

    public void saveReadingPosition(String titleId, String chapterId, String pageId) {
        Map<String, Object> position = new HashMap<>();
        position.put("chapterId", chapterId);
        position.put("pageId", pageId);

        firestore.collection("users").document(userId)
                .collection("reading_positions").document(titleId)
                .set(position);
    }

    public void getReadingPosition(String titleId, OnSuccessListener<DocumentSnapshot> onSuccessListener) {
        firestore.collection("users").document(userId)
                .collection("reading_positions").document(titleId)
                .get().addOnSuccessListener(onSuccessListener);
    }
}
