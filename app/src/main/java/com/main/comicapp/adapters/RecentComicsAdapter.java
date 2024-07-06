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
import com.main.comicapp.models.Comic;

import java.util.List;

public class RecentComicsAdapter extends RecyclerView.Adapter<RecentComicsAdapter.ComicViewHolder> {

    public interface OnComicClickListener {
        void onComicClick(Comic comic);
    }

    private Context context;
    private List<Comic> comics;
    private AllComicsAdapter.OnComicClickListener listener;

    public RecentComicsAdapter(Context context, List<Comic> comics) {
        this.context = context;
        this.comics = comics;
    }

    public RecentComicsAdapter(Context context, List<Comic> comics, AllComicsAdapter.OnComicClickListener listener) {
        this.context = context;
        this.comics = comics;
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
        Comic comic = comics.get(position);
        // Set comic cover image and title
        holder.tvComicTitle.setText(comic.getTitle());
        Glide.with(context)
                .load(comic.getCoverUri())
                .into(holder.ivComicCover);

        holder.itemView.setOnClickListener(v -> listener.onComicClick(comic));
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView ivComicCover;
        TextView tvComicTitle;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivComicCover = itemView.findViewById(R.id.iv_comic_cover);
            tvComicTitle = itemView.findViewById(R.id.tv_comic_title);
        }
    }
}
