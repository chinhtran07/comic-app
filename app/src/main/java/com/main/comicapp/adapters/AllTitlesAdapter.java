package com.main.comicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.models.Title;

import java.util.List;

public class AllTitlesAdapter extends RecyclerView.Adapter<AllTitlesAdapter.ComicViewHolder> {

    public interface OnTitleClickListener {
        void onTitleClick(Title title);
    }

    private final Context context;
    private final List<Title> titles;
    private OnTitleClickListener listener;

    public AllTitlesAdapter(Context context, List<Title> titles) {
        this.context = context;
        this.titles = titles;
    }

    public AllTitlesAdapter(Context context, List<Title> titles, OnTitleClickListener listener) {
        this.context = context;
        this.titles = titles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Title title = titles.get(position);
        // Set comic cover image and title
        holder.tvTitleName.setText(title.getTitle());
        Glide.with(context)
                .load(title.getCoverUrl())
                .into(holder.ivTitleCover);

        holder.itemView.setOnClickListener(v -> listener.onTitleClick(title));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTitleCover;
        TextView tvTitleName;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTitleCover = itemView.findViewById(R.id.iv_title_cover);
            tvTitleName = itemView.findViewById(R.id.tv_title_name);
        }
    }


}
