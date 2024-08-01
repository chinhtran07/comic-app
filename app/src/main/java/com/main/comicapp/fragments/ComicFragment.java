package com.main.comicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.PageAdapter;
import com.main.comicapp.models.Page;
import com.main.comicapp.viewmodels.PageViewModel;

import java.util.HashMap;
import java.util.List;

public class ComicFragment extends Fragment {

    private RecyclerView recyclerView;
    private PageAdapter pageAdapter;
    private PageViewModel pageViewModel;
    private String chapterId;

    public ComicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comic, container, false);
        recyclerView = view.findViewById(R.id.recycler_img);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        pageAdapter = new PageAdapter(getContext(), null);
        recyclerView.setAdapter(pageAdapter);

        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);


        if (getArguments() != null) {
            chapterId = getArguments().getString("chapterId");
            loadPages();
        }

        return view;
    }

    private void loadPages() {
        if (chapterId != null) {
            pageViewModel.getPages(new HashMap<>(), chapterId).observe(getViewLifecycleOwner(), new Observer<List<Page>>() {
                @Override
                public void onChanged(@Nullable List<Page> pages) {
                    if (pages != null) {
                        pageAdapter.setPages(pages);
                    }
                }
            });
        }
    }
}