package com.main.comicapp.adapters.admin;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.models.Title;

import java.util.List;

public class TitleAdminAdapter extends RecyclerView.Adapter<TitleAdminAdapter.TitleAdminViewHolder>{

    private List<Title> titles;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Title title);
        void onDeleteClick(Title title);
        void onViewChaptersClick(Title title);
    }

    public TitleAdminAdapter(List<Title> titles, OnItemClickListener listener) {
        this.titles = titles;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTitles(List<Title> titles) {
        this.titles = titles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TitleAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_title, parent, false);
        return new TitleAdminViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull TitleAdminViewHolder holder, int position) {
        Title title = titles.get(position);
        holder.tvTitle.setText(title.getTitle());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(title));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(title));
        holder.btnViewChapters.setOnClickListener(v -> listener.onViewChaptersClick(title));
    }

    @Override
    public int getItemCount() {
        return titles != null ? titles.size():0;
    }

    public static class TitleAdminViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageButton btnEdit, btnDelete;
        public Button btnViewChapters;

        public TitleAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_comic_title);
            btnEdit = itemView.findViewById(R.id.btn_edit_comic);
            btnDelete = itemView.findViewById(R.id.btn_delete_comic);
            btnViewChapters = itemView.findViewById(R.id.btn_view_chapters);
        }
    }
}
