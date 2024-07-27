package com.main.comicapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.models.Chapter;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private List<Chapter> chapters;
    private OnChapterClickListener listener;

    public void setListener(OnChapterClickListener listener) { this.listener = listener; }

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
    }

    public ChapterAdapter(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        String chapterName = "Chương " + chapter.getChapterNumber();
        holder.txtChapterName.setText(chapterName);
        holder.txtUploadDate.setText(chapter.getUploadedDate().toString());
        holder.itemView.setOnClickListener(v -> listener.onChapterClick(chapter));
    }

    @Override
    public int getItemCount() {
        return chapters != null ? chapters.size() : 0;
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtChapterName;
        TextView txtUploadDate;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChapterName = itemView.findViewById(R.id.title_detail_chapter_list_title);
            txtUploadDate = itemView.findViewById(R.id.title_detail_chapter_list_upload);
        }
    }

    public void clearChapters() {
        chapters.clear();
        notifyDataSetChanged();
    }
}
