package com.main.comicapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.main.comicapp.R;
import com.main.comicapp.models.Title;

import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.TitleViewHolder> {

    private List<Title> titles;
    private OnTitleClickListener listener;
    private final Context context;
    private final FirebaseStorage storage;

    public void setListener(OnTitleClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTitles(List<Title> titles) {
        this.titles = titles;
        notifyDataSetChanged();
    }

    public interface OnTitleClickListener {
        void onTitleClick(Title title);
    }

    public TitlesAdapter(Context context, List<Title> titles) {
        this.titles = titles;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        Title title = titles.get(position);
        // Set comic cover image and title
        holder.tvTitleName.setText(title.getTitle());

        StorageReference storageRef = storage.getReference().child(title.getCover());
        Glide.with(context)
                .load(storageRef)
                .into(holder.ivTitleCover);

        holder.itemView.setOnClickListener(v -> listener.onTitleClick(title));
    }

    @Override
    public int getItemCount() {
        return titles != null ? titles.size() : 0;
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTitleCover;
        TextView tvTitleName;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTitleCover = itemView.findViewById(R.id.iv_title_cover);
            tvTitleName = itemView.findViewById(R.id.tv_title_name);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearTitles() {
        titles.clear();
        notifyDataSetChanged();
    }
}
