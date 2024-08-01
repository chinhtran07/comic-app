package com.main.comicapp.fragments.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.main.comicapp.R;
import com.main.comicapp.databinding.FragmentHomeBinding;
import com.main.comicapp.viewmodels.GenreViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private LinearLayout statsContainer;
    private FragmentHomeBinding binding;
    private PieChart pieChartTotalComics;
    private BarChart barChartTotalUsers;
    private LineChart lineChartNewComics;
    private BarChart barChartUserActivities;
    private LineChart lineChartReadingTrends;
    private GenreViewModel genreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        genreViewModel = new GenreViewModel();
        statsContainer = binding.linearStatsContainer;
        pieChartTotalComics = binding.pieChartTotalComics;
        barChartTotalUsers = binding.barChartTotalUsers;
        lineChartNewComics = binding.lineChartNewComics;
        barChartUserActivities = binding.barChartUserActivities;
        lineChartReadingTrends = binding.lineChartReadingTrend;

        loadStats();
        loadChart();

        return root;
    }

    private void loadStats() {
        // Example: Adding stat cards programmatically
        addStatCard("Total Comics", "120");
        addStatCard("Total Users", "350");
        addStatCard("New Comics This Month", "20");
        addStatCard("Reports Pending", "5");
    }

    private void addStatCard(String title, String value) {
        // Inflate the card view layout
        View cardView = LayoutInflater.from(getContext()).inflate(R.layout.item_stat_card, statsContainer, false);

        // Find and set data for TextViews
        TextView tvStatTitle = cardView.findViewById(R.id.tv_stat_title);
        TextView tvStatValue = cardView.findViewById(R.id.tv_stat_value);

        tvStatTitle.setText(title);
        tvStatValue.setText(value);

        // Add the card view to the container
        statsContainer.addView(cardView);
    }

    private void loadChart() {
        setupPieChart(pieChartTotalComics, "Total comics");
        loadPieChartData(pieChartTotalComics);

        setupBarChart(barChartTotalUsers, "Total users");
        loadBarChartData(barChartTotalUsers);

        setupLineChart(lineChartNewComics, "New comics");
        loadLineChartData(lineChartNewComics);

        setupBarChart(barChartUserActivities, "User activities");
        loadBarChartData(barChartUserActivities);

        setupLineChart(lineChartReadingTrends, "Reading trends");
        loadLineChartData(lineChartReadingTrends);
    }

    private void setupPieChart(PieChart pieChart, String descriptionText) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.getDescription().setText(descriptionText);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
    }

    private void loadPieChartData(PieChart pieChart) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(34f, "Lãng mạn"));
        entries.add(new PieEntry(23f, "Kinh dị"));
        entries.add(new PieEntry(14f, "Trinh thám"));
        entries.add(new PieEntry(35f, "Thiếu nhi"));
        entries.add(new PieEntry(40f, "Hero"));

        PieDataSet dataSet = new PieDataSet(entries, "Tổng");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }

    private void setupBarChart(BarChart barChart, String descriptionText) {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setText(descriptionText);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);
    }

    private void loadBarChartData(BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 30));
        entries.add(new BarEntry(1, 80));
        entries.add(new BarEntry(2, 60));
        entries.add(new BarEntry(3, 50));
        entries.add(new BarEntry(4, 70));
        entries.add(new BarEntry(5, 60));

        BarDataSet dataSet = new BarDataSet(entries, "Data Set");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        barChart.setData(data);
        barChart.invalidate(); // refresh
    }

    private void setupLineChart(LineChart lineChart, String descriptionText) {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.getDescription().setText(descriptionText);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);
    }

    private void loadLineChartData(LineChart lineChart) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 30));
        entries.add(new Entry(1, 80));
        entries.add(new Entry(2, 60));
        entries.add(new Entry(3, 50));
        entries.add(new Entry(4, 70));
        entries.add(new Entry(5, 60));

        LineDataSet dataSet = new LineDataSet(entries, "Data Set");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(3f);
        dataSet.setValueTextSize(10f);

        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.invalidate(); // refresh
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}