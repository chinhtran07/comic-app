package com.main.comicapp.activities;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private PieChart userPieChart;
    private PieChart comicPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        userPieChart = findViewById(R.id.user_pie_chart);
        comicPieChart = findViewById(R.id.comic_pie_chart);

        fetchUserData();
        fetchComicData();
    }

    private void fetchUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int userCount = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userCount++;
                            }
                            setupPieChart(userPieChart, "User Distribution", userCount, "Users");
                        } else {
                            Log.w("StatisticsActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void fetchComicData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comics")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int comicCount = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                comicCount++;
                            }
                            setupPieChart(comicPieChart, "Comic Distribution", comicCount, "Comics");
                        } else {
                            Log.w("StatisticsActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setupPieChart(PieChart pieChart, String label, int count, String description) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(count, description));

        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setColors(new int[]{
                R.color.purple_200,
                R.color.teal_200,
                R.color.red_200,
                R.color.blue_200,
                R.color.green_200
        }, this);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }

}
