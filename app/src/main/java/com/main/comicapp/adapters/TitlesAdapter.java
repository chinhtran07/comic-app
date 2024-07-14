package com.main.comicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.models.Title;

import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.TitleViewHolder> {

    public void setListener(OnTitleClickListener listener) {
        this.listener = listener;
    }

    public interface OnTitleClickListener {
        void onTitleClick(Title title);
    }

    private final Context context;
    private final List<Title> titles;
    private OnTitleClickListener listener;

    public TitlesAdapter(Context context, List<Title> titles) {
        this.context = context;
        this.titles = titles;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        Title title = titles.get(position);
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

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTitleCover;
        TextView tvTitleName;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTitleCover = itemView.findViewById(R.id.iv_title_cover);
            tvTitleName = itemView.findViewById(R.id.tv_title_name);
        }
    }

}
