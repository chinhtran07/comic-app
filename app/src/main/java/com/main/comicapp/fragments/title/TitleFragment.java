package com.main.comicapp.fragments.title;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.admin.TitleAdminAdapter;
import com.main.comicapp.databinding.FragmentTitleManagementBinding;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.TitleViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TitleFragment extends Fragment{

    private List<Title> titles;
    private RecyclerView recyclerView;
    private TitleAdminAdapter adapter;
    private Button btnAddComic;
    private SearchView searchView;
    private TitleViewModel viewModel;
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

        viewModel = new TitleViewModel();
        navController = Navigation.findNavController(view);

        recyclerView = binding.recyclerComics;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TitleAdminAdapter(new ArrayList<>(), new TitleAdminAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Title title) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", title);
                navController.navigate(R.id.nav_edit_comic, bundle);
            }

            @Override
            public void onDeleteClick(Title title) {

            }

            @Override
            public void onViewChaptersClick(Title title) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", title);
                navController.navigate(R.id.nav_comic_detail, bundle);
            }
        });

        recyclerView.setAdapter(adapter);

        viewModel.getTitles(new HashMap<>()).observe(getViewLifecycleOwner(), new Observer<List<Title>>() {
            @Override
            public void onChanged(List<Title> titles) {
                if (titles != null) {
                    adapter.setTitles(titles);
                }
            }
        });

        btnAddComic = binding.btnAddComic;

        btnAddComic.setOnClickListener(v -> {
            navController.navigate(R.id.nav_add_comic);
        });

        SearchView searchView = binding.searchView;
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

    }
}