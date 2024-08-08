package com.main.comicapp.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.main.comicapp.databinding.FragmentAddUserBinding;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

public class AddUserFragment extends Fragment {

    private EditText etUsername, etEmail, firstName, lastName, birthDate;
    private Button btnSaveUser;
    private FragmentAddUserBinding binding;
    private UserViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etUsername = binding.etUsername;
        etEmail = binding.etEmail;
        firstName = binding.etFirstName;
        lastName = binding.etLastName;
        birthDate = binding.etBirthDate;
        btnSaveUser = binding.btnSaveUser;

        btnSaveUser.setOnClickListener(v -> saveUser());

        return root;
    }

    private void saveUser() {
        if (validation()) {
            Toast.makeText(getContext(),"Enter the information", Toast.LENGTH_SHORT).show();
        } else {
            User newUser = new User();
            newUser.setUsername(etUsername.getText().toString());
            newUser.setEmail(etEmail.getText().toString());
            newUser.setFirstName(firstName.getText().toString());
            newUser.setLastName(lastName.getText().toString());
            newUser.setBirthDate(birthDate.getText().toString());
            //call viewmodel to add

            //return usermanagement
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        }


    }

    private boolean validation() {
        return etUsername.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty()
                || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty();
    }
}
