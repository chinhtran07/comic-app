package com.main.comicapp.adapters.admin;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.models.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdminAdapter extends RecyclerView.Adapter<ChapterAdminAdapter.ChapterViewHolder> {

    private List<Chapter> chapters;
    private OnChapterClickListener listener;

    public void setListener(OnChapterClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
        notifyDataSetChanged();
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public interface OnChapterClickListener {
        void onChapterClick(Chapter chapter);
        void onUpdateClick(Chapter chapter);
        void onDeleteClick(Chapter chapter);
    }

    public ChapterAdminAdapter(List<Chapter> chapters) {
        if (chapters == null) {
            this.chapters = new ArrayList<>(); // Khởi tạo danh sách chapters nếu null
        } else {
            this.chapters = chapters;
        }
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter_admin, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        String chapterName = "Chương " + chapter.getChapterNumber();
        holder.txtChapterName.setText(chapterName);
        holder.txtDescription.setText(chapter.getDescription());
        holder.txtUploadDate.setText(chapter.getUploadedDate().toString());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChapterClick(chapter);
            }
        });

        holder.btnUpdate.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUpdateClick(chapter);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(chapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapters != null ? chapters.size() : 0;
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtChapterName;
        TextView txtDescription;
        TextView txtUploadDate;
        Button btnUpdate;
        Button btnDelete;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChapterName = itemView.findViewById(R.id.text_view_chapter_number);
            txtDescription = itemView.findViewById(R.id.text_view_chapter_description);
            txtUploadDate = itemView.findViewById(R.id.text_view_uploaded_date);
            btnUpdate = itemView.findViewById(R.id.button_update_chapter);
            btnDelete = itemView.findViewById(R.id.button_delete_chapter);
        }
    }

    public void clearChapters() {
        if (chapters != null) {
            chapters.clear();
            notifyDataSetChanged();
        }
    }
}
