package com.main.comicapp.activities.ui.usermanaging;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.main.comicapp.databinding.FragmentUserManagingBinding;

public class UserManagingFragment extends Fragment {

    private FragmentUserManagingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserManagingViewModel userManagingViewModel =
                new ViewModelProvider(this).get(UserManagingViewModel.class);

        binding = FragmentUserManagingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        userManagingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}