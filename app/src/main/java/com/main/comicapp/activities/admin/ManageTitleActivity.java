package com.main.comicapp.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.main.comicapp.R;
import com.main.comicapp.adapters.admin.ManageTitleAdapter;
import com.main.comicapp.viewmodels.TitleViewModel;

import java.util.ArrayList;

public class ManageTitleActivity extends AppCompatActivity {

    private TitleViewModel titleViewModel;
    private ManageTitleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_title);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_comic_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ManageTitleAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        titleViewModel = new ViewModelProvider(this).get(TitleViewModel.class);
        titleViewModel.getTitles(null).observe(this, titles -> {
            if (titles != null) {
                adapter.setTitles(titles);
            }
        });

        EditText searchEditText = findViewById(R.id.et_search_comic);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filterTitles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adapter.setListener(title -> {
            Intent intent = new Intent(ManageTitleActivity.this, ManageChapterActivity.class);
            intent.putExtra("title_id", title.getId());
            startActivity(intent);
        });

        FloatingActionButton fabAddComic = findViewById(R.id.fab_add_comic);
        fabAddComic.setOnClickListener(v -> {
            Intent intent = new Intent(ManageTitleActivity.this, AddTitleActivity.class);
            startActivity(intent);
        });
    }
}
