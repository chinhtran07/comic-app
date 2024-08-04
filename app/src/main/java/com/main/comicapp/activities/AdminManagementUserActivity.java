package com.main.comicapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.main.comicapp.R;
import com.main.comicapp.adapters.admin.UserAdminAdapter;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

public class AdminManagementUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdminAdapter adapter;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_usermanagement);

        recyclerView = findViewById(R.id.recycler_view_user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdminAdapter();
        recyclerView.setAdapter(adapter);

        userViewModel = new UserViewModel();
        userViewModel.getAllUser().addOnSuccessListener(queryDocumentSnapshots -> {
            adapter.setUsers(queryDocumentSnapshots.toObjects(User.class));
        }).addOnFailureListener(e -> {
            Toast.makeText(AdminManagementUserActivity.this, "Failed to load users: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}
