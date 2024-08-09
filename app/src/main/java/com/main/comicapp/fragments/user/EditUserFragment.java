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
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.HashMap;
import java.util.Map;

public class EditUserFragment extends Fragment {

    private EditText etUsername, etEmail;
    private Button btnSaveUser;
    private User user; //
    private FragmentEditUserBinding binding;
    private UserViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new UserViewModel();

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
        Map<String, Object> params = new HashMap<>();
        params.put("username", etUsername.getText().toString());
        params.put("email", etEmail.getText().toString());
        viewModel.saveUser(user.getId(), params);


        // Example of navigating back to UserManagementFragment
        NavController navController = Navigation.findNavController(requireActivity(), R.id.action_edit_user_to_user_management);
        navController.popBackStack(); // Go back to previous fragment (UserManagementFragment)
    }
}
