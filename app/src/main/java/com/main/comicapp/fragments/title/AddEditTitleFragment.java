package com.main.comicapp.fragments.title;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.main.comicapp.R;
import com.main.comicapp.models.Title;
import com.main.comicapp.viewmodels.TitleViewModel;

public class AddEditTitleFragment extends Fragment {

    private EditText etTitle, etAuthor;
    private Button btnSave;
    private boolean isEditMode = false;
    private Title comic;
    private TitleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_title, container, false);

        etTitle = view.findViewById(R.id.et_comic_title);
        etAuthor = view.findViewById(R.id.et_comic_author);
        btnSave = view.findViewById(R.id.btn_save_comic);

        viewModel = new TitleViewModel();

        if (getArguments() != null) {
            comic = (Title) getArguments().getSerializable("comic");
            if (comic != null) {
                isEditMode = true;
                etTitle.setText(comic.getTitle());
                etAuthor.setText(comic.getTitleFormat());
            }
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author)) {
                Toast.makeText(getContext(), "Please type information", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEditMode) {
                viewModel.updateTitle(comic.getId(), comic);
            } else {
                viewModel.addTitle(comic);
            }

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_admin);
            navController.navigate(R.id.nav_title_management);
        });

        return view;
    }
}