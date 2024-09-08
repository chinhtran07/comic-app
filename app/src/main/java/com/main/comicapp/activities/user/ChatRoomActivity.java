package com.main.comicapp.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.main.comicapp.R;
import com.main.comicapp.adapters.MessageAdapter;
import com.main.comicapp.models.Message;
import com.main.comicapp.viewmodels.MessageViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private MessageViewModel messageViewModel;
    private UserViewModel userViewModel;
    private String chatRoomId;
    private String currentUserId;
    private String userId;

    private TextView tvChatUserName;
    private ImageView ivUserAvatar, ivBack;
    private EditText messageInput;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        tvChatUserName = findViewById(R.id.tv_chat_user_name);
        ivUserAvatar = findViewById(R.id.img_user_avatar);
        messageInput = findViewById(R.id.et_message_input);
        sendButton = findViewById(R.id.btn_send_message);
        ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(ChatRoomActivity.this, ChatRoomListActivity.class);
            intent.putExtra("userId", currentUserId);
            startActivity(intent);
            finish();
        });

        chatRoomId = getIntent().getStringExtra("chatRoomId");
        currentUserId = getIntent().getStringExtra("currentUserId");
        userId = getIntent().getStringExtra("userId");

        if (chatRoomId == null || chatRoomId.isEmpty() || userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewMessages = findViewById(R.id.recycler_view_messages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(new ArrayList<>(), currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        messageViewModel.loadMessagesByChatRoom(chatRoomId);

        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                tvChatUserName.setText(user.getUsername());
                Glide.with(ChatRoomActivity.this)
                        .load(user.getAvatar())
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(ivUserAvatar);
            }
        });

        userViewModel.fetchUserById(userId);

        messageViewModel.getMessagesLiveData().observe(this, messages -> {
            if (messages != null) {
                messageAdapter.updateMessages(messages);
                recyclerViewMessages.scrollToPosition(messages.size() - 1);
            }
        });

        sendButton.setOnClickListener(v -> {
            String content = messageInput.getText().toString().trim();
            if (!content.isEmpty()) {
                Message message = new Message(currentUserId, chatRoomId, content, System.currentTimeMillis());
                messageViewModel.saveMessage(message);
                messageAdapter.addMessage(message);
                recyclerViewMessages.scrollToPosition(messageAdapter.getItemCount() - 1);
                messageInput.setText("");
            } else {
                Toast.makeText(ChatRoomActivity.this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
