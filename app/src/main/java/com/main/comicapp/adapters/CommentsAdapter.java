package com.main.comicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.models.Comment;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private List<User> users;
    public OnCommentClickListener listener;

    public CommentsAdapter(List<Comment> comments, List<User> users) {
        this.comments = comments;
        this.users = users;
    }

    public void setListener(OnCommentClickListener listener) {this.listener = listener; }

    public void setComments(List<Comment> comments) {
        this.comments = new ArrayList<>();
        comments.forEach(comment -> {
            this.comments.add(comment);
            notifyItemInserted(this.comments.size() - 1);
        });
    }

    public void setUsers(List<User> users) {
        this.users = new ArrayList<>();
        users.forEach(user -> {
            this.users.add(user);
            notifyItemChanged(this.users.size() - 1);
        });
    }

    public List<Comment> getComments() {
        return comments;
    }



    @NonNull
    @Override
    public CommentsAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        if (users != null && !users.isEmpty()) {
            User user = users.get(position);
            holder.txtUsername.setText(user.getUsername());
        }
        holder.txtContent.setText(comment.getText());
        holder.itemView.setOnClickListener(v -> listener.onCommentClick(comment));
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        TextView txtUsername;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.title_detail_comments_list_content);
            txtUsername = itemView.findViewById(R.id.title_detail_comments_list_username);
        }


    }

    public interface OnCommentClickListener {
        void onCommentClick(Comment comment);
    }

    public void clearComments() {
        comments.clear();
        notifyDataSetChanged();
    }
}
