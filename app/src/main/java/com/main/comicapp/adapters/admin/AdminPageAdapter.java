package com.main.comicapp.adapters.admin;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.main.comicapp.R;
import com.main.comicapp.models.Page;

import java.util.ArrayList;
import java.util.List;

public class AdminPageAdapter extends RecyclerView.Adapter<AdminPageAdapter.PageViewHolder> {

    private List<Page> pages;
    private OnPageClickListener listener;
    private Context context;

    public interface  OnPageClickListener {
        void onPageEditClick(Page page);
        void onPageDeleteClick(Page page);
    }

    public void setListener(OnPageClickListener listener) {this.listener = listener;}

    public void setPages(List<Page> pages) {
        this.pages = pages != null ? pages : new ArrayList<>();
        notifyDataSetChanged();
    }

    public AdminPageAdapter(Context context, List<Page> pages) {
        this.context = context;
        this.pages = pages != null ? pages : new ArrayList<>();
    }


    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        Page page = pages.get(position);
        String pageTitle = "Trang" + page.getPageNumber();
        holder.tvPageTitle.setText(pageTitle);
        holder.btnEditPage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPageEditClick(page);
            }
        });
        holder.btnDeletePage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPageDeleteClick(page);
            }
        });
        StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(page.getImagePath());
        final long ONE_MEGABYTE = 1024 * 1024;
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Glide.with(context).load(bytes).into(holder.ivPagePreview);
        }).addOnFailureListener(e -> {});
    }

    @Override
    public int getItemCount() {
        return pages != null ? pages.size() : 0;
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        TextView tvPageTitle;
        ImageView ivPagePreview;
        Button btnEditPage;
        Button btnDeletePage;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPageTitle = itemView.findViewById(R.id.tv_page_title);
            ivPagePreview = itemView.findViewById(R.id.iv_page_preview);
            btnEditPage = itemView.findViewById(R.id.btn_edit_page);
            btnDeletePage = itemView.findViewById(R.id.btn_delete_page);
        }
    }
}
