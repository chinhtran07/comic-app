package com.main.comicapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.models.ChatRoom;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.MessageViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.List;

public class ListChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_CHAT_ROOM = 1;

    private List<User> userList;
    private List<ChatRoom> chatRoomList;
    private OnUserClickListener onUserClickListener;
    private OnChatRoomClickListener onChatRoomClickListener;
    private boolean showingUsers = false;
    private UserViewModel userViewModel;
    private String currentUserId;
    private MessageViewModel messageViewModel;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    public ListChatRoomAdapter(String currentUserId, List<User> userList, List<ChatRoom> chatRoomList,
                               OnUserClickListener userListener, OnChatRoomClickListener chatRoomListener,
                               UserViewModel userViewModel, MessageViewModel messageViewModel) {
        this.currentUserId = currentUserId;
        this.userList = userList;
        this.chatRoomList = chatRoomList;
        this.onUserClickListener = userListener;
        this.onChatRoomClickListener = chatRoomListener;
        this.userViewModel = userViewModel;
        this.messageViewModel = messageViewModel;
    }

    @Override
    public int getItemViewType(int position) {
        return showingUsers ? VIEW_TYPE_USER : VIEW_TYPE_CHAT_ROOM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_user_chat, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_chat_room, parent, false);
            return new ChatRoomViewHolder(view, messageViewModel);
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
        MessageViewModel messageViewModel;

        public ChatRoomViewHolder(@NonNull View itemView, MessageViewModel messageViewModel) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            this.messageViewModel = messageViewModel;
        }

        public void bind(ChatRoom chatRoom) {
            String otherUserId;
            if (chatRoom.getSenderId().equals(currentUserId)) {
                otherUserId = chatRoom.getReceiverId();
            } else {
                otherUserId = chatRoom.getSenderId();
            }

            userViewModel.fetchUserById(otherUserId);
            userViewModel.getUserLiveData().observe((LifecycleOwner) itemView.getContext(), user -> {
                if (user != null) {
                    tvUserName.setText(user.getUsername());

                    Glide.with(itemView.getContext())
                            .load(user.getAvatar())
                            .placeholder(R.drawable.ic_user_placeholder)
                            .error(R.drawable.ic_user_placeholder)
                            .into(ivUserAvatar);
                } else {
                    tvUserName.setText("Unknown User");
                }
            });

            messageViewModel.loadLastMessageByChatRoom(chatRoom.getRoomId());
            messageViewModel.getLastMessageLiveData().observe((LifecycleOwner) itemView.getContext(), lastMessage -> {
                if (lastMessage != null) {
                    tvLastMessage.setText(lastMessage.getContent());
                } else {
                    tvLastMessage.setText("No messages yet");
                }
            });

            itemView.setOnClickListener(v -> onChatRoomClickListener.onChatRoomClick(chatRoom));
        }
    }

    public void updateUserList(List<User> newUserList) {
        this.userList.clear();
        this.userList.addAll(newUserList);
        notifyDataSetChanged();
    }

    public void updateChatRoomList(List<ChatRoom> newChatRooms) {
        this.chatRoomList.clear();
        this.chatRoomList.addAll(newChatRooms);
        notifyDataSetChanged();
    }
}
