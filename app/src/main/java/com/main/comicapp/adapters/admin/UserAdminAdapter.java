package com.main.comicapp.adapters.admin;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserViewHolder> {
    private List<User> userList;
    private List<User> filteredUserList;
    private OnUserClickListener listener;
    private OnUserStatusClickListener statusClickListener;
    private NoResultsCallback noResultsCallback;

    public interface OnUserClickListener {
        void onUserClick(String userId);
    }

    public interface OnUserStatusClickListener {
        void onUserStatusClick(User user, int position);
    }

    public interface NoResultsCallback {
        void onUpdateNoResultsVisibility(int itemCount);
    }

    public UserAdminAdapter(OnUserClickListener listener, OnUserStatusClickListener statusClickListener, NoResultsCallback noResultsCallback) {
        this.listener = listener;
        this.statusClickListener = statusClickListener;
        this.noResultsCallback = noResultsCallback;
        this.userList = new ArrayList<>();
        this.filteredUserList = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        this.userList = users;
        if (filteredUserList == null) {
            filteredUserList = new ArrayList<>();
        }
        this.filteredUserList.clear();
        this.filteredUserList.addAll(users);
        notifyDataSetChanged();
        if (noResultsCallback != null) {
            noResultsCallback.onUpdateNoResultsVisibility(getItemCount());
        }
    }

    public void filterUsers(String query) {
        if (filteredUserList == null) {
            filteredUserList = new ArrayList<>();
        }
        filteredUserList.clear();
        if (query.isEmpty()) {
            filteredUserList.addAll(userList);
        } else {
            String lowerCaseQuery = query.toLowerCase(Locale.ROOT);
            for (User user : userList) {
                if (user.getUsername().toLowerCase(Locale.ROOT).contains(lowerCaseQuery)) {
                    filteredUserList.add(user);
                }
            }
        }
        notifyDataSetChanged();
        if (noResultsCallback != null) {
            noResultsCallback.onUpdateNoResultsVisibility(getItemCount());
        }
    }

    public void removeUserById(String userId) {
        User userToRemove = null;

        for (User user : userList) {
            if (user.getId().equals(userId)) {
                userToRemove = user;
                break;
            }
        }

        if (userToRemove != null) {
            userList.remove(userToRemove);
            filteredUserList.remove(userToRemove);
            notifyDataSetChanged();
            if (noResultsCallback != null) {
                noResultsCallback.onUpdateNoResultsVisibility(getItemCount());
            }
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_user, parent, false);
        return new UserViewHolder(itemView, listener, statusClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = filteredUserList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return filteredUserList != null ? filteredUserList.size() : 0;
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
