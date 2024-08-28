package com.main.comicapp.activities.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.main.comicapp.R;
import com.main.comicapp.adapters.admin.GenreAdminAdapter;
import com.main.comicapp.models.Genre;
import com.main.comicapp.viewmodels.GenreViewModel;

public class AdminManagementGenreActivity extends AppCompatActivity {

    private GenreViewModel genreViewModel;
    private GenreAdminAdapter genreAdminAdapter;
    private RecyclerView recyclerView;
    private EditText etSearchGenre;
    private TextView tvNoResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_genre);

        genreViewModel = new ViewModelProvider(this).get(GenreViewModel.class);

        initViews();
        setupRecyclerView();
        setupSearch();
        loadGenres();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_genre_list);
        etSearchGenre = findViewById(R.id.et_search_genre);
        tvNoResults = findViewById(R.id.tv_no_results);

        Button btnAddGenre = findViewById(R.id.btn_add_genre);
        btnAddGenre.setOnClickListener(v -> showAddGenreDialog());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        genreAdminAdapter = new GenreAdminAdapter(
                genre -> {
                },
                genre -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xóa thể loại")
                            .setMessage("Bạn có chắc chắn muốn xóa thể loại này không?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                genreViewModel.deleteGenre(genre.getId());
                                Toast.makeText(this, "Đã xóa thể loại", Toast.LENGTH_SHORT).show();
                                loadGenres();
                            })
                            .setNegativeButton("No", null)
                            .show();
                },
                itemCount -> {
                    if (itemCount == 0) {
                        tvNoResults.setVisibility(View.VISIBLE);
                    } else {
                        tvNoResults.setVisibility(View.GONE);
                    }
                }
        );

        recyclerView.setAdapter(genreAdminAdapter);
    }

    private void setupSearch() {
        etSearchGenre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                genreAdminAdapter.filterGenres(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadGenres() {
        genreViewModel.getAllGenres().observe(this, genres -> {
            if (genres != null && !genres.isEmpty()) {
                genreAdminAdapter.setGenres(genres);
            } else {
                tvNoResults.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showAddGenreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_add_genre, null);
        builder.setView(dialogView);
        EditText etGenreName = dialogView.findViewById(R.id.et_genre_name);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);

        AlertDialog dialog = builder.create();
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnAdd.setOnClickListener(v -> {
            String genreName = etGenreName.getText().toString().trim();
            if (!genreName.isEmpty()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String id = db.collection("genres").document().getId();

                Genre newGenre = new Genre(genreName);
                newGenre.setId(id);
                genreViewModel.addGenre(newGenre);
                dialog.dismiss();
                Toast.makeText(this, "Thể loại đã được thêm", Toast.LENGTH_SHORT).show();
                loadGenres();
            } else {
                etGenreName.setError("Tên thể loại không được để trống");
            }
        });


        dialog.show();
    }

}
