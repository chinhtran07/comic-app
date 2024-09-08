package com.main.comicapp.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.comicapp.R;
import com.main.comicapp.adapters.ListChatRoomAdapter;
import com.main.comicapp.models.ChatRoom;
import com.main.comicapp.models.User;
import com.main.comicapp.viewmodels.ChatRoomViewModel;
import com.main.comicapp.viewmodels.MessageViewModel;
import com.main.comicapp.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListChatRoomAdapter chatRoomAdapter;
    private UserViewModel userViewModel;
    private ChatRoomViewModel chatRoomViewModel;
    private MessageViewModel messageViewModel;
    private List<User> userList;
    private List<ChatRoom> chatRoomList;
    private EditText searchEditText;
    private TextView tvNoResults;
    private String currentUserId;
    private List<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        currentUserId = getIntent().getStringExtra("userId");
        if (currentUserId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recycler_view_chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.et_search_chat);
        tvNoResults = findViewById(R.id.tv_no_results);

        userList = new ArrayList<>();
        chatRoomList = new ArrayList<>();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        chatRoomViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        chatRoomAdapter = new ListChatRoomAdapter(currentUserId, userList, chatRoomList, this::onUserClick, this::onChatRoomClick);
        recyclerView.setAdapter(chatRoomAdapter);

        loadChatRooms();
        loadAllUsers();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterUsersOrShowChatRooms(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        findViewById(R.id.root_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                searchEditText.clearFocus();
                return false;
            }
        });
    }

    private void loadAllUsers() {
        userViewModel.getUsersLiveData().observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                allUsers = new ArrayList<>(users);
                chatRoomAdapter.updateUserList(users);
            }
        });
        userViewModel.loadAllUsers();
    }

    private void loadChatRooms() {
        chatRoomViewModel.loadChatRoomsForUser(currentUserId);
        chatRoomViewModel.getChatRoomsLiveData().observe(this, chatRooms -> {
            if (chatRooms != null && !chatRooms.isEmpty()) {
                chatRoomList = chatRooms;
                chatRoomAdapter.updateChatRoomList(chatRooms);
                tvNoResults.setVisibility(View.GONE);

                for (ChatRoom chatRoom : chatRooms) {
                    loadLastMessageForChatRoom(chatRoom);
                }
            } else {
                chatRoomList.clear();
                chatRoomAdapter.updateChatRoomList(chatRoomList);
                tvNoResults.setVisibility(View.VISIBLE);
                Toast.makeText(ChatRoomListActivity.this, "Không có phòng chat nào", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLastMessageForChatRoom(ChatRoom chatRoom) {
        messageViewModel.loadLastMessageByChatRoom(chatRoom.getRoomId());
        messageViewModel.getLastMessageLiveData().observe(this, lastMessage -> {
            if (lastMessage != null) {
                chatRoom.setLastMessage(lastMessage);
                chatRoomAdapter.notifyDataSetChanged();
            }
        });
    }

    private void filterUsersOrShowChatRooms(String query) {
        if (query.isEmpty()) {
            chatRoomAdapter.setShowingUsers(false);
            chatRoomAdapter.updateChatRoomList(chatRoomList);
            tvNoResults.setVisibility(chatRoomList.isEmpty() ? View.VISIBLE : View.GONE);
        } else {
            if (allUsers != null && !allUsers.isEmpty()) {
                List<User> filteredList = new ArrayList<>();
                for (User user : allUsers) {
                    if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(user);
                    }
                }
                chatRoomAdapter.setShowingUsers(true);
                chatRoomAdapter.updateUserList(filteredList);
                tvNoResults.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
            } else {
                Toast.makeText(ChatRoomListActivity.this, "Không có người dùng nào", Toast.LENGTH_SHORT).show();
                tvNoResults.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onUserClick(User user) {
        chatRoomViewModel.fetchOrCreateChatRoom(currentUserId, user.getId());
        chatRoomViewModel.getChatRoomLiveData().observe(this, chatRoom -> {
            if (chatRoom != null) {
                Intent intent = new Intent(ChatRoomListActivity.this, ChatRoomActivity.class);
                intent.putExtra("chatRoomId", chatRoom.getRoomId());
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("userId", user.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không thể tạo phòng chat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onChatRoomClick(ChatRoom chatRoom) {
        String otherUserId;
        if (chatRoom.getSenderId().equals(currentUserId)) {
            otherUserId = chatRoom.getReceiverId();
        } else {
            otherUserId = chatRoom.getSenderId();
        }
        Intent intent = new Intent(ChatRoomListActivity.this, ChatRoomActivity.class);
        intent.putExtra("chatRoomId", chatRoom.getRoomId());
        intent.putExtra("currentUserId", currentUserId);
        intent.putExtra("userId", otherUserId);
        startActivity(intent);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }
}
