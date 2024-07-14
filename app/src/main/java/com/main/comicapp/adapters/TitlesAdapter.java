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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.main.comicapp.R;
import com.main.comicapp.models.Title;

import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.TitleViewHolder> {

    public void setListener(OnTitleClickListener listener) {
        this.listener = listener;
    }

    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://comic-app-b344c.appspot.com");


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
        StorageReference storageReference = storage.getReference().child(title.getCover());

        // Set comic cover image and title
        holder.tvTitleName.setText(title.getTitle());
        Glide.with(context)
                .load("https://firebasestorage.googleapis.com/v0/b/comic-app-b344c.appspot.com/o/page1_image1.png?alt=media&token=9dbd6a76-190a-4ad0-a313-a07dcb8828e3")
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
