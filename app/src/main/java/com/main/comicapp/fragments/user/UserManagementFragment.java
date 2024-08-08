package com.main.comicapp.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.main.comicapp.R;
import com.main.comicapp.adapters.UserAdapter;
import com.main.comicapp.databinding.FragmentUserManagementBinding;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserManagementFragment extends Fragment {

    private FragmentUserManagementBinding binding;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddUser;
    private SearchView searchView;
    private UserAdapter userAdapter;
    private List<User> userList; // Assuming User is a model class
    private List<User> filteredUserList;
    private NavController navController;
    private UserViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new UserViewModel();

        binding = FragmentUserManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.recyclerUsers;
        fabAddUser = binding.fabAddUser;
        searchView = binding.searchView;
        navController = Navigation.findNavController(view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(new ArrayList<>(), this::navigateToUserDetail);

        recyclerView.setAdapter(userAdapter);

        fabAddUser.setOnClickListener(v -> navigateToAddUser());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });

        loadUsers();
    }

    private void loadUsers() {
        viewModel.getAllUser().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userList = new ArrayList<>();
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    User user = User.toObject(document.getData(), document.getId());
                    userList.add(user);
                }
                userAdapter.update(userList);
            }
        });
    }

    private void filterUsers(String query) {
        if (userList == null) return;

        String lowerCaseQuery = query.toLowerCase();
        filteredUserList = userList.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        userAdapter.update(filteredUserList);
    }

    private void navigateToUserDetail(User user) {
        // Navigate to UserDetailFragment
        Bundle args = new Bundle();
        args.putSerializable("user", user); // Assuming User implements Serializable
        navController.navigate(R.id.action_to_detail_user, args);
    }

    private void navigateToAddUser() {
        // Navigate to AddUserFragment
        navController.navigate(R.id.action_to_add_user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}