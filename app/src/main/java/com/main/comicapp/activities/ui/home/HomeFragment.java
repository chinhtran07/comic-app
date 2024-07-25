package com.main.comicapp.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.comicapp.R;
import com.main.comicapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private LinearLayout statsContainer;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        statsContainer = binding.linearStatsContainer;

        loadStats();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}