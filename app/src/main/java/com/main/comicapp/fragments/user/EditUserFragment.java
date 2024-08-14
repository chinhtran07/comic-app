package com.main.comicapp.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.main.comicapp.R;
import com.main.comicapp.databinding.FragmentEditUserBinding;
import com.main.comicapp.models.User;

public class EditUserFragment extends Fragment {

    private EditText etUsername, etEmail;
    private Button btnSaveUser;
    private User user; //
    private FragmentEditUserBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etUsername = binding.etUsername;
        etEmail = binding.etEmail;
        btnSaveUser = binding.btnSaveUser;

        // Retrieve User object from arguments
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
            displayUserDetails(user);
        }

        btnSaveUser.setOnClickListener(v -> saveUser());

        return root;
    }

    private void displayUserDetails(User user) {
        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());
    }

    private void saveUser() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();

        // Update user information and save to database or ViewModel

        // Example of navigating back to UserManagementFragment
//        NavController navController = Navigation.findNavController(getActivity(), R.id.action_edit_user_to_user_management);
//        navController.popBackStack(); // Go back to previous fragment (UserManagementFragment)
    }
}
