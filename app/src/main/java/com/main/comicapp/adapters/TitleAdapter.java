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

import java.util.ArrayList;
import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.TitleViewHolder> {

    private List<Title> titles;
    private OnTitleClickListener listener;
    private final Context context;

    public void setListener(OnTitleClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTitles(List<Title> titles) {
        this.titles = titles;
        notifyDataSetChanged();
    }

    // Phương thức để thêm truyện mới vào danh sách hiện có
    @SuppressLint("NotifyDataSetChanged")
    public void addTitle(Title title) {
        if (this.titles == null) {
            this.titles = new ArrayList<>();  // Khởi tạo danh sách nếu nó là null
        }
        this.titles.add(title);
        notifyDataSetChanged();
    }


    public interface OnTitleClickListener {
        void onTitleClick(Title title);
    }

    public TitleAdapter(Context context, List<Title> titles) {
        this.titles = titles;
        this.context = context;
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
        holder.tvTitleName.setText(title.getTitle());

        Glide.with(context)
                .load(title.getCover())
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

