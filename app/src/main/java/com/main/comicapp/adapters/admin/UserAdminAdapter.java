package com.main.comicapp.adapters.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.models.User;
import java.util.List;

import android.widget.ImageView;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserViewHolder> {
    private List<User> userList;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getUsername());
        holder.userEmail.setText(user.getEmail());

        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(user.getAvatar())
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_user_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void setUsers(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail;
        ImageView avatar;

        UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_user_name);
            userEmail = itemView.findViewById(R.id.tv_user_email);
            avatar = itemView.findViewById(R.id.iv_user_avatar);
        }
    }
}
