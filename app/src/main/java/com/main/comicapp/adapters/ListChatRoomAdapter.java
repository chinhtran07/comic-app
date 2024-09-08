package com.main.comicapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.models.ChatRoom;
import com.main.comicapp.models.Message;
import com.main.comicapp.models.User;

import java.util.List;

public class ListChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_CHAT_ROOM = 0;
    private static final int VIEW_TYPE_USER = 1;

    private List<User> userList;
    private List<ChatRoom> chatRoomList;
    private String currentUserId;
    private boolean showingUsers = false;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    private final OnUserClickListener onUserClickListener;
    private final OnChatRoomClickListener onChatRoomClickListener;

    public ListChatRoomAdapter(String currentUserId, List<User> userList, List<ChatRoom> chatRoomList, OnUserClickListener onUserClickListener, OnChatRoomClickListener onChatRoomClickListener) {
        this.currentUserId = currentUserId;
        this.userList = userList;
        this.chatRoomList = chatRoomList;
        this.onUserClickListener = onUserClickListener;
        this.onChatRoomClickListener = onChatRoomClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return showingUsers ? VIEW_TYPE_USER : VIEW_TYPE_CHAT_ROOM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_chat_room, parent, false);
            return new ChatRoomViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_USER) {
            User user = userList.get(position);
            ((UserViewHolder) holder).bind(user);
        } else {
            ChatRoom chatRoom = chatRoomList.get(position);
            ((ChatRoomViewHolder) holder).bind(chatRoom);
        }
    }

    @Override
    public int getItemCount() {
        return showingUsers ? userList.size() : chatRoomList.size();
    }

    public void setShowingUsers(boolean showingUsers) {
        this.showingUsers = showingUsers;
        notifyDataSetChanged();
    }

    public void updateUserList(List<User> newUserList) {
        this.userList.clear();
        this.userList.addAll(newUserList);
        notifyDataSetChanged();
    }

    public void updateChatRoomList(List<ChatRoom> newChatRoomList) {
        this.chatRoomList.clear();
        this.chatRoomList.addAll(newChatRoomList);
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        ImageView ivUserAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
        }

        public void bind(User user) {
            tvUserName.setText(user.getUsername());

            Glide.with(itemView.getContext())
                    .load(user.getAvatar())
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .into(ivUserAvatar);

            itemView.setOnClickListener(v -> onUserClickListener.onUserClick(user));
        }
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLastMessage;
        ImageView ivUserAvatar;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
        }

        public void bind(ChatRoom chatRoom) {
            String otherUserId = chatRoom.getSenderId().equals(currentUserId) ? chatRoom.getReceiverId() : chatRoom.getSenderId();
            User otherUser = getUserById(otherUserId);
            if (otherUser != null) {
                tvUserName.setText(otherUser.getUsername());

                Glide.with(itemView.getContext())
                        .load(otherUser.getAvatar())
                        .placeholder(R.drawable.ic_user_placeholder)
                        .error(R.drawable.ic_user_placeholder)
                        .into(ivUserAvatar);
            }

            Message lastMessage = chatRoom.getLastMessage();
            if (lastMessage != null) {
                tvLastMessage.setText(lastMessage.getContent());
            } else {
                tvLastMessage.setText("No messages yet");
            }

            itemView.setOnClickListener(v -> onChatRoomClickListener.onChatRoomClick(chatRoom));
        }

        private User getUserById(String userId) {
            for (User user : userList) {
                if (user.getId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }
}
