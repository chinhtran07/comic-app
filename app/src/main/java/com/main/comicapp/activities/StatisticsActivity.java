package com.main.comicapp.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.main.comicapp.R;
import com.main.comicapp.viewmodels.UserViewModel;

public class StatisticsActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private TextView readerCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        readerCountTextView = findViewById(R.id.readerCountTextView);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getReaderCountLiveData().observe(this, count -> {
            readerCountTextView.setText("Number of Readers: " + count);
        });

        userViewModel.fetchReaderCount();
    }
}
