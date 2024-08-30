package com.main.comicapp.fragments.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.main.comicapp.models.Genre;
import com.main.comicapp.viewmodels.GenreViewModel;
import com.main.comicapp.viewmodels.TitleViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private LinearLayout statsContainer;
    private FragmentHomeBinding binding;
    private PieChart pieChartTotalComics;
    private BarChart barChartTotalUsers;
    private LineChart lineChartNewComics;
    private BarChart barChartUserActivities;
    private LineChart lineChartReadingTrends;
    private GenreViewModel genreViewModel;
    private UserViewModel userViewModel;
    private TitleViewModel titleViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        genreViewModel = new ViewModelProvider(this).get(GenreViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        titleViewModel = new ViewModelProvider(this).get(TitleViewModel.class);

        statsContainer = binding.linearStatsContainer;
        pieChartTotalComics = binding.pieChartTotalComics;
        barChartTotalUsers = binding.barChartTotalUsers;
        lineChartNewComics = binding.lineChartNewComics;
        barChartUserActivities = binding.barChartUserActivities;
        lineChartReadingTrends = binding.lineChartReadingTrend;

        observeData();

        return root;
    }

    private void observeData() {
        userViewModel.getReaderCountLiveData().observe(getViewLifecycleOwner(), readerCount -> {
            titleViewModel.getTitleCount().observe(getViewLifecycleOwner(), totalComics -> {
                if (totalComics != null) {
                    updateStats(totalComics);
                }
            });

            titleViewModel.getTitlesUpdatedThisMonth().observe(getViewLifecycleOwner(), titlesUpdatedThisMonth -> {
                int newComicsThisMonth = titlesUpdatedThisMonth != null ? titlesUpdatedThisMonth.size() : 0;
                updateNewComicsStats(newComicsThisMonth);
            });

            updateCharts();
        });

        userViewModel.getAdminCountLiveData().observe(getViewLifecycleOwner(), adminCount -> {
            titleViewModel.getTitleCount().observe(getViewLifecycleOwner(), totalComics -> {
                if (totalComics != null) {
                    updateStats(totalComics);
                }
            });

            titleViewModel.getTitlesUpdatedThisMonth().observe(getViewLifecycleOwner(), titlesUpdatedThisMonth -> {
                int newComicsThisMonth = titlesUpdatedThisMonth != null ? titlesUpdatedThisMonth.size() : 0;
                updateNewComicsStats(newComicsThisMonth);
            });

            updateCharts();
        });

        userViewModel.fetchReaderCount();
        userViewModel.fetchAdminCount();
    }

    private void updateStats(int totalComics) {
        Integer readerCount = userViewModel.getReaderCountLiveData().getValue();
        Integer adminCount = userViewModel.getAdminCountLiveData().getValue();

        statsContainer.removeAllViews();

        addStatCard("Total Comics", String.valueOf(totalComics));
        addStatCard("Total Users", readerCount != null ? readerCount.toString() : "0");
        addStatCard("Total Admins", adminCount != null ? adminCount.toString() : "0");
    }

    private void updateNewComicsStats(int newComicsThisMonth) {
        removeStatCard("New Comics This Month");
        // Add updated "New Comics This Month" card
        addStatCard("New Comics This Month", String.valueOf(newComicsThisMonth));
    }

    private void removeStatCard(String title) {
        for (int i = 0; i < statsContainer.getChildCount(); i++) {
            View cardView = statsContainer.getChildAt(i);
            TextView tvStatTitle = cardView.findViewById(R.id.tv_stat_title);
            if (tvStatTitle.getText().toString().equals(title)) {
                statsContainer.removeViewAt(i);
                break;
            }
        }
    }

    private void addStatCard(String title, String value) {
        View cardView = LayoutInflater.from(getContext()).inflate(R.layout.item_stat_card, statsContainer, false);
        TextView tvStatTitle = cardView.findViewById(R.id.tv_stat_title);
        TextView tvStatValue = cardView.findViewById(R.id.tv_stat_value);

        tvStatTitle.setText(title);
        tvStatValue.setText(value);
        statsContainer.addView(cardView);
    }

    private void updateCharts() {
        Integer readerCount = userViewModel.getReaderCountLiveData().getValue();
        Integer adminCount = userViewModel.getAdminCountLiveData().getValue();

        if (readerCount != null && adminCount != null) {
            setupPieChart(pieChartTotalComics, "Total comics");
            loadPieChartData(pieChartTotalComics);

            setupBarChart(barChartTotalUsers, "Total users");
            loadBarChartData(barChartTotalUsers, readerCount, adminCount);

            setupLineChart(lineChartNewComics, "New comics");
            loadLineChartData(lineChartNewComics);

            setupBarChart(barChartUserActivities, "User activities");
            loadBarChartData(barChartUserActivities, readerCount, adminCount);

            setupLineChart(lineChartReadingTrends, "Reading trends");
            loadLineChartData(lineChartReadingTrends);
        }
    }

    private void setupPieChart(PieChart pieChart, String descriptionText) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
    }

    private void loadPieChartData(PieChart pieChart) {
        genreViewModel.getAllGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null && !genres.isEmpty()) {
                List<PieEntry> entries = new ArrayList<>();
                Map<String, Integer> genreCounts = new HashMap<>();

                for (Genre genre : genres) {
                    String genreName = genre.getName();
                    titleViewModel.getTitleCountByGenreId(genre.getId()).observe(getViewLifecycleOwner(), count -> {
                        if (count != null) {
                            genreCounts.put(genreName, count);
                            Log.d("PieChartData", "Ten danh muc: " + genreName + genre.getId() + " - SL: " + count);

                            entries.add(new PieEntry(count, genreName));

                            if (genreCounts.size() == genres.size()) {
                                updatePieChart(entries, pieChart);
                            }
                        }
                    });
                }
            }
        });
    }

    private void updatePieChart(List<PieEntry> entries, PieChart pieChart) {
        PieDataSet dataSet = new PieDataSet(entries, "Genres");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            int color = Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
            colors.add(color);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        pieChart.invalidate();
    }



    private void setupBarChart(BarChart barChart, String descriptionText) {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);
    }

    private void loadBarChartData(BarChart barChart, int readerCount, int adminCount) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, readerCount));
        entries.add(new BarEntry(1, adminCount));

        BarDataSet dataSet = new BarDataSet(entries, "User Roles");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        barChart.setData(data);
        barChart.invalidate();
    }

    private void setupLineChart(LineChart lineChart, String descriptionText) {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);
    }

    private void loadLineChartData(LineChart lineChart) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 30));
        entries.add(new Entry(1, 90));
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
        lineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
