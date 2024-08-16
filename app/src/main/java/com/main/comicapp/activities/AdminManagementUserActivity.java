package com.main.comicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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
    private EditText searchEditText;
    private TextView noResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_usermanagement);

        recyclerView = findViewById(R.id.recycler_view_user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noResultsTextView = findViewById(R.id.tv_no_results);

        adapter = new UserAdminAdapter(this::navigateToUserProfile, this::toggleUserActiveStatus, this::updateNoResultsVisibility);
        recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Xử lý Intent để xóa người dùng nếu có ID được truyền từ AdminBlockUserActivity
        handleIntent(getIntent());

        userViewModel.getAllUser().addOnSuccessListener(queryDocumentSnapshots -> {
            adapter.setUsers(queryDocumentSnapshots.toObjects(User.class));
        }).addOnFailureListener(e -> {
            Toast.makeText(AdminManagementUserActivity.this, "Failed to load users: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });

        searchEditText = findViewById(R.id.et_search_user);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                adapter.filterUsers(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // Xử lý Intent để xóa người dùng khỏi danh sách nếu có ID được truyền về
    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("USER_ID")) {
            String userId = intent.getStringExtra("USER_ID");
            if (userId != null) {
                adapter.removeUserById(userId);
            }
        }
    }

    private void navigateToUserProfile(String userId) {
        Intent intent = new Intent(AdminManagementUserActivity.this, AdminBlockUserActivity.class);
        intent.putExtra("USER_ID", userId);
        Log.d("User Id", userId);
        startActivity(intent);
    }

    private void toggleUserActiveStatus(User user, int position) {
        userViewModel.updateUserStatus(user.getId()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.setisActive(!user.getisActive());
                adapter.notifyItemChanged(position);
            } else {
                Toast.makeText(this, "Failed to update user status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNoResultsVisibility(int itemCount) {
        if (itemCount == 0) {
            noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
        }
    }
}
