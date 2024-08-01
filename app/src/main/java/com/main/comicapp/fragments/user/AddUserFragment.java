package com.main.comicapp.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.comicapp.databinding.FragmentAddUserBinding;

public class AddUserFragment extends Fragment {

    private EditText etUsername, etEmail;
    private Button btnSaveUser;
    private FragmentAddUserBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etUsername = binding.etUsername;
        etEmail = binding.etEmail;
        btnSaveUser = binding.btnSaveUser;

        btnSaveUser.setOnClickListener(v -> saveUser());

        return root;
    }

    private void saveUser() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();

        // Validate inputs and save user to database or ViewModel

        // Example of navigating back to UserManagementFragment
       requireActivity().getOnBackPressedDispatcher().onBackPressed();
    }
}
