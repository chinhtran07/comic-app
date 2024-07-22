package com.main.comicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.main.comicapp.R;
import com.main.comicapp.models.Page;

import java.util.List;


public class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.PageViewHolder>{

    private List<Page> pages;
    private Context context;
    private final FirebaseStorage storage;

    public PagesAdapter(Context context, List<Page> pages) {
        this.pages = pages;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        Page page = pages.get(position);

        StorageReference storageRef = storage.getReference().child(page.getImagePath());
        Glide.with(context).load(storageRef)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pages == null ? 0 : pages.size();
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
