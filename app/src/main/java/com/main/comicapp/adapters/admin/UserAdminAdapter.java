package com.main.comicapp.adapters.admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.List;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserViewHolder> {
    private List<User> userList;
    private OnUserClickListener listener;
    private OnUserStatusClickListener statusClickListener;

    public interface OnUserClickListener {
        void onUserClick(String userId);
    }

    public interface OnUserStatusClickListener {
        void onUserStatusClick(User user, int position);
    }

    public UserAdminAdapter(OnUserClickListener listener, OnUserStatusClickListener statusClickListener) {
        this.listener = listener;
        this.statusClickListener = statusClickListener;
    }

    public void setUsers(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_user, parent, false);
        return new UserViewHolder(itemView, listener, statusClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail;
        Button userStatusButton;
        ImageView avatar;
        User currentUser;

        UserViewHolder(View itemView, OnUserClickListener listener, OnUserStatusClickListener statusClickListener) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_user_name);
            userEmail = itemView.findViewById(R.id.tv_user_email);
            userStatusButton = itemView.findViewById(R.id.btn_user_status);
            avatar = itemView.findViewById(R.id.iv_user_avatar);

            itemView.setOnClickListener(v -> {
                if (listener != null && currentUser != null) {
                    listener.onUserClick(currentUser.getId());
                }
            });

            userStatusButton.setOnClickListener(v -> {
                if (statusClickListener != null && currentUser != null) {
                    statusClickListener.onUserStatusClick(currentUser, getAdapterPosition());
                }
            });
        }

        void bind(User user) {
            currentUser = user;
            userName.setText(user.getUsername());
            userEmail.setText(user.getEmail());
            userStatusButton.setText(user.getisActive() ? "Active" : "Inactive");

            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(user.getAvatar())
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(avatar);
            } else {
                avatar.setImageResource(R.drawable.ic_user_placeholder);
            }
        }
    }
}
