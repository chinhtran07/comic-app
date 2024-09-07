package com.main.comicapp.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ChatRoomListActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        currentUserId = getIntent().getStringExtra("userId");

        recyclerView = findViewById(R.id.recycler_view_chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.et_search_chat);

        userList = new ArrayList<>();
        chatRoomList = new ArrayList<>();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        chatRoomViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        chatRoomAdapter = new ListChatRoomAdapter(currentUserId, userList, chatRoomList, this::onUserClick, this::onChatRoomClick, userViewModel, messageViewModel);
        recyclerView.setAdapter(chatRoomAdapter);

        loadChatRooms();

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

    private void filterUsersOrShowChatRooms(String query) {
        if (query.isEmpty()) {
            chatRoomAdapter.setShowingUsers(false);
            chatRoomAdapter.updateChatRoomList(chatRoomList);
        } else {
            userViewModel.getUsersLiveData().observe(this, users -> {
                if (users != null && !users.isEmpty()) {
                    List<User> filteredList = new ArrayList<>();
                    for (User user : users) {
                        if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(user);
                        }
                    }
                    chatRoomAdapter.setShowingUsers(true);
                    chatRoomAdapter.updateUserList(filteredList);
                } else {
                    Toast.makeText(ChatRoomListActivity.this, "Không có người dùng nào", Toast.LENGTH_SHORT).show();
                }
            });
            userViewModel.loadAllUsers();
        }
    }

    private void loadChatRooms() {
        chatRoomViewModel.loadChatRoomsForUser(currentUserId);
        chatRoomViewModel.getChatRoomsLiveData().observe(this, chatRooms -> {
            if (chatRooms != null && !chatRooms.isEmpty()) {
                chatRoomAdapter.updateChatRoomList(chatRooms);
            } else {
                Toast.makeText(ChatRoomListActivity.this, "Không có phòng chat nào", Toast.LENGTH_SHORT).show();
            }
        });
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
        intent.putExtra("userId", otherUserId); // Gửi ID của user còn lại
        startActivity(intent);
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
