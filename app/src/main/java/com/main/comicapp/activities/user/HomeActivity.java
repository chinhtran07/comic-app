package com.main.comicapp.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.activities.BaseActivity;
import com.main.comicapp.adapters.TitleAdapter;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.TitleViewModel;
import com.main.comicapp.viewmodels.ReadingHistoryViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    private RecyclerView rvRecentComics;
    private TitleAdapter adapter;
    private TitleViewModel titleViewModel;
    private ReadingHistoryViewModel readingHistoryViewModel;
    private ImageView icMessage; // Khai báo ImageView cho ic_message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();


        Map<String, String> params = new HashMap<>();
        titleViewModel.getTitles(params).observeForever(new Observer<List<Title>>() {
            @Override
            public void onChanged(List<Title> titles) {
                if (titles != null) {
                    adapter.setTitles(titles);
                }
            }
        });

        icMessage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatRoomListActivity.class);
            intent.putExtra("userId", currentUserSession.getUserId());
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews() {
        rvRecentComics = findViewById(R.id.rv_recent_comics);
        rvRecentComics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new TitleAdapter(this, null);
        adapter.setListener(new TitleAdapter.OnTitleClickListener() {
            @Override
            public void onTitleClick(Title title) {
                openTitleDetailActivity(title);
            }
        });
        rvRecentComics.setAdapter(adapter);

        titleViewModel = new ViewModelProvider(this).get(TitleViewModel.class);
        readingHistoryViewModel = new ViewModelProvider(this).get(ReadingHistoryViewModel.class);

        icMessage = findViewById(R.id.ic_message);
    }

    private void openTitleDetailActivity(Title title) {
        if (currentUserSession != null && currentUserSession.getUserId() != null) {
            String userId = currentUserSession.getUserId();
            readingHistoryViewModel.addHistory(userId, title.getId());
        }

        Intent intent = new Intent(getApplicationContext(), TitleDetailActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
