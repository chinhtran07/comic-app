package com.main.comicapp.fragments.title;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.main.comicapp.R;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.TitleViewModel;

public class TitleDetailFragment extends Fragment {

    private TextView tvTitle, tvAuthor;
    private Button btnEdit, btnDelete;
    private Title title;
    private TitleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_detail, container, false);

        viewModel = new TitleViewModel();
        tvTitle = view.findViewById(R.id.tv_comic_detail_title);
        tvAuthor = view.findViewById(R.id.tv_comic_detail_author);
        btnEdit = view.findViewById(R.id.btn_edit_comic);
        btnDelete = view.findViewById(R.id.btn_delete_comic);

        if (getArguments() != null) {
            title = (Title) getArguments().getSerializable("title");
            if (title != null) {
                tvTitle.setText(title.getTitle());
                tvAuthor.setText(title.getTitleFormat());
            }
        }

        btnEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("title", title);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_admin);
            navController.navigate(R.id.nav_add_comic, bundle);
        });

        btnDelete.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Title")
                    .setMessage("Are you sure you want to delete this title?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete title from database
                        viewModel.deleteTitle(title.getId());
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_admin);
                        navController.navigate(R.id.nav_title_management);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return view;
    }
}
