package com.main.comicapp.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.models.Title;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ManageTitleAdapter extends RecyclerView.Adapter<ManageTitleAdapter.TitleViewHolder> {

    private List<Title> titles;
    private List<Title> titlesFull;
    private OnTitleClickListener listener;
    private Context context;

    public interface OnTitleClickListener {
        void onViewChaptersClick(Title title);
    }

    public void setListener(OnTitleClickListener listener) {
        this.listener = listener;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles != null ? titles : new ArrayList<>();
        this.titlesFull = new ArrayList<>(this.titles);
        notifyDataSetChanged();
    }

    public ManageTitleAdapter(Context context, List<Title> titles) {
        this.context = context;
        this.titles = titles != null ? titles : new ArrayList<>();
        this.titlesFull = new ArrayList<>(this.titles);
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_title, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        Title title = titles.get(position);
        holder.tvComicTitle.setText(title.getTitle());
        Picasso.get().load(title.getCover()).into(holder.ivComicCover);

        holder.btnViewChapters.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewChaptersClick(title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles != null ? titles.size() : 0;
    }

    public void filterTitles(String query) {
        if (query == null || query.isEmpty()) {
            titles = new ArrayList<>(titlesFull);
        } else {
            List<Title> filteredList = new ArrayList<>();
            String lowerCaseQuery = query.toLowerCase();
            for (Title title : titlesFull) {
                if (title.getTitle().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(title);
                }
            }
            titles = filteredList;
        }
        notifyDataSetChanged();
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvComicTitle;
        ImageView ivComicCover;
        Button btnViewChapters;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComicTitle = itemView.findViewById(R.id.tv_comic_title);
            ivComicCover = itemView.findViewById(R.id.iv_comic_cover);
            btnViewChapters = itemView.findViewById(R.id.btn_view_chapters);
        }
    }
}
