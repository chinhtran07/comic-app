package com.main.comicapp.fragments.title;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.main.comicapp.R;
import com.main.comicapp.adapters.TitleAdapter;
import com.main.comicapp.databinding.FragmentTitleManagementBinding;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.TitleViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TitleFragment extends Fragment{

    private RecyclerView recyclerView;
    private TitleAdapter titleAdapter;
    private TitleViewModel titleViewModel;
    private FragmentTitleManagementBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTitleManagementBinding.inflate(inflater, container, false);
        View root =  binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleViewModel = new TitleViewModel();
        navController = Navigation.findNavController(view);

        recyclerView = binding.recyclerComics;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        titleAdapter = new TitleAdapter(getContext(), new ArrayList<>());

        titleViewModel.getTitles(new HashMap<>()).observe(getViewLifecycleOwner(), new Observer<List<Title>>() {
            @Override
            public void onChanged(List<Title> titles) {
                if (titles != null) {
                    titleAdapter.setTitles(titles);
                }
            }
        });

        titleAdapter.setListener(new TitleAdapter.OnTitleClickListener() {
            @Override
            public void onTitleClick(Title title) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", title);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_admin);
                navController.navigate(R.id.nav_comic_detail, bundle);
            }
        });

        recyclerView.setAdapter(titleAdapter);


        SearchView searchView = binding.searchComic;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Implement search logic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Implement search logic
                return false;
            }
        });

        FloatingActionButton fabAddComic = binding.fabAddComic;
        fabAddComic.setOnClickListener(v -> {
            navController.navigate(R.id.nav_add_comic);
        });
    }
}