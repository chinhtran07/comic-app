package com.main.comicapp.activities.admin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.main.comicapp.R;
import com.main.comicapp.activities.BaseActivity;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends BaseActivity {
    private UserViewModel userViewModel;
    private TextView readerCountTextView;
    private TextView adminCountTextView;
    private PieChart userPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        readerCountTextView = findViewById(R.id.readerCountTextView);
        adminCountTextView = findViewById(R.id.adminCountTextView);
        userPieChart = findViewById(R.id.userPieChart);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getReaderCountLiveData().observe(this, readerCount -> {
            readerCountTextView.setText("Number of Readers: " + readerCount);
            updatePieChart();
        });

        userViewModel.getAdminCountLiveData().observe(this, adminCount -> {
            adminCountTextView.setText("Number of Admins: " + adminCount);
            updatePieChart();
        });

        userViewModel.fetchReaderCount();
        userViewModel.fetchAdminCount();
    }

    private void updatePieChart() {
        Integer readerCount = userViewModel.getReaderCountLiveData().getValue();
        Integer adminCount = userViewModel.getAdminCountLiveData().getValue();

        if (readerCount != null && adminCount != null) {
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(readerCount, "Readers"));
            entries.add(new PieEntry(adminCount, "Admins"));

            PieDataSet dataSet = new PieDataSet(entries, "User Roles");
            dataSet.setColors(
                    ContextCompat.getColor(this, R.color.purple_200),
                    ContextCompat.getColor(this, R.color.teal_200)
            );

            PieData data = new PieData(dataSet);
            userPieChart.setData(data);
            userPieChart.invalidate(); // refresh
        }
    }
}
