package com.main.comicapp.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.main.comicapp.R;
import com.main.comicapp.databinding.FragmentDetailUserBinding;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

public class UserDetailFragment extends Fragment {

    private TextView tvUsername, tvEmail;
    private Button btnDeleteUser, btnEditUser;
    private User user; // Assuming User is a model class
    private FragmentDetailUserBinding binding;
    private NavController navController;
    private UserViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new UserViewModel();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = binding.tvUsername;
        tvEmail = binding.tvEmail;
        btnDeleteUser = binding.btnDeleteUser;
        btnEditUser = binding.btnEditUser;

        navController = Navigation.findNavController(view);

        // Retrieve User object from arguments
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
            displayUserDetails(user);
        }

        btnDeleteUser.setOnClickListener(v -> confirmDeleteUser());
        btnEditUser.setOnClickListener(v -> navigateToEditUser());

    }

    private void displayUserDetails(User user) {
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
    }

    private void confirmDeleteUser() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteUser())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteUser() {
        requireActivity().getOnBackPressedDispatcher().onBackPressed(); // Go back to previous fragment (UserManagementFragment)
    }

    private void navigateToEditUser() {
        // Navigate to EditUserFragment with the current user
        Bundle args = new Bundle();
        args.putSerializable("user", user); // Assuming User implements Serializable
        navController.navigate(R.id.action_detail_user_to_edit_user);
    }
}
