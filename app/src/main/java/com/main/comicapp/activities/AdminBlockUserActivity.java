package com.main.comicapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.admin.UserAdminAdapter;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminBlockUserActivity extends AppCompatActivity {

//    private UserViewModel userViewModel;
//    private RecyclerView recyclerView;
//    private UserAdminAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_usermanagement);
//
//        initRecyclerView();
//        initViewModel();
//    }
//
//    private void initRecyclerView() {
//        recyclerView = findViewById(R.id.recycler_view_user_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new UserAdminAdapter(new ArrayList<>(), userViewModel);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void initViewModel() {
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        loadUsers();
//    }
//
//    private void loadUsers() {
//        userViewModel.getAllUser().addOnSuccessListener(queryDocumentSnapshots -> {
//            List<User> users = queryDocumentSnapshots.toObjects(User.class);
//            adapter.setUsers(users);
//        }).addOnFailureListener(e -> {
//            Toast.makeText(AdminBlockUserActivity.this, "Failed to load users: " + e.getMessage(), Toast.LENGTH_LONG).show();
//        });
//    }
}
