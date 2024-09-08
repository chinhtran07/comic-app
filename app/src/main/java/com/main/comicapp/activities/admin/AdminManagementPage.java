package com.main.comicapp.activities.admin;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.main.comicapp.R;
import com.main.comicapp.adapters.admin.AdminPageAdapter;
import com.main.comicapp.models.Page;
import com.main.comicapp.viewmodels.PageViewModel;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AdminManagementPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private PageViewModel pageViewModel;
    private RecyclerView pageRecyclerView;
    private AdminPageAdapter adapter;
    private TextView tvNoPage;
    private Button btnAddPage;
    private FirebaseStorage storage;
    private Context context;

    private ImageView currentImageView;
    private Uri imageUri;
    private Page newPage;

    private String chapterId;
    private String titleId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pages);

        storage = FirebaseStorage.getInstance();
        context = this;

        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);

        pageRecyclerView = findViewById(R.id.recycler_view_pages);
        tvNoPage = findViewById(R.id.tv_no_page);
        btnAddPage = findViewById(R.id.button_add_page);

        chapterId = getIntent().getStringExtra("chapter_id");
        titleId = getIntent().getStringExtra("title_id");


        pageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pageRecyclerView.setHasFixedSize(true);

        adapter = new AdminPageAdapter(this, new ArrayList<>());
        adapter.setListener(new AdminPageAdapter.OnPageClickListener() {
            @Override
            public void onPageEditClick(Page page) {

            }

            @Override
            public void onPageDeleteClick(Page page) {
                pageViewModel.deletePage(page.getId());
            }
        });

        pageRecyclerView.setAdapter(adapter);

        loadPages();

        btnAddPage.setOnClickListener(v -> {
            showAddPageDialog();
        });
    }

    private void loadPages() {
        String chapterId = getIntent().getStringExtra("chapter_id");
        pageViewModel.getPages(new HashMap<>(), chapterId).observe(this, pages -> {
            if (pages != null && !pages.isEmpty()) {
                adapter.setPages(pages);
                pageRecyclerView.setVisibility(View.VISIBLE);
                tvNoPage.setVisibility(View.INVISIBLE);
            } else {
                tvNoPage.setVisibility(View.VISIBLE);
                pageRecyclerView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), PICK_IMAGE_REQUEST);
    }

    private void showAddPageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_page, null);

        EditText etPageNumber = view.findViewById(R.id.et_dialog_page_number);
        ImageView ivPagePreviewDialog = view.findViewById(R.id.iv_dialog_page_preview);
        currentImageView = ivPagePreviewDialog;
        Button btnPickImage = view.findViewById(R.id.btn_pick_image);

        btnPickImage.setOnClickListener(v -> {
            openImagePicker();
        });

        builder.setView(view)
                .setTitle("Thêm trang")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String pageNumberStr = etPageNumber.getText().toString();
                    if (!pageNumberStr.isEmpty()) {
                        int pageNumber = Integer.parseInt(pageNumberStr);
                        try {
                            addPage(ivPagePreviewDialog, pageNumber);
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                }).create().show();
    }

    private void uploadImage(ImageView imageView, String titleId, String chapterId, String filename) throws ExecutionException, InterruptedException {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        byte[] data = outStream.toByteArray();
        String filePath = titleId + "/" + chapterId + "/" + filename + ".jpg";
        newPage.setImagePath(filePath);
        StorageReference fileRef = storage.getReference().child(filePath);
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();
        UploadTask uploadTask = fileRef.putBytes(data, metadata);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUri = downloadUri;
                } else {

                }
            }
        });
        new UploadImage().execute(urlTask);
    }

    private void addPage(ImageView imageView, int pageNumber) throws ExecutionException, InterruptedException {

        newPage = new Page();
        newPage.setId(UUID.randomUUID().toString());
        newPage.setPageNumber(pageNumber);
        newPage.setChapterId(chapterId);
        String filename = "page_" + pageNumber;
        uploadImage(imageView, titleId, chapterId, filename);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    Glide.with(this).load(imageUri).into(currentImageView);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UploadImage extends AsyncTask<Task<Uri>, Void, Uri> {

        @Override
        protected Uri doInBackground(Task<Uri>... tasks) {
            try {
                return Tasks.await(tasks[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Uri uri) {
            if (uri != null) {
                pageViewModel.addPage(newPage);
                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_LONG).show();
            }
        }
    }
}
