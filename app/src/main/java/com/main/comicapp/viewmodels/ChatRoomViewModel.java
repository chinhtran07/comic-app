package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.main.comicapp.models.ChatRoom;
import com.main.comicapp.repositories.ChatRoomRepository;
import com.main.comicapp.repositories.impl.ChatRoomRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomViewModel extends ViewModel {

    private final ChatRoomRepository chatRoomRepository;
    private final MutableLiveData<List<ChatRoom>> chatRoomsLiveData = new MutableLiveData<>();
    private final MutableLiveData<ChatRoom> chatRoomLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ChatRoomViewModel() {
        chatRoomRepository = new ChatRoomRepositoryImpl();
    }

    public LiveData<List<ChatRoom>> getChatRoomsLiveData() {
        return chatRoomsLiveData;
    }

    public LiveData<ChatRoom> getChatRoomLiveData() {
        return chatRoomLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchOrCreateChatRoom(String currentUserId, String selectedUserId) {
        chatRoomRepository.getAllChatRooms().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatRoom existingChatRoom = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                    if (chatRoom != null) {
                        String senderId = chatRoom.getSenderId();
                        String receiverId = chatRoom.getReceiverId();

                        if ((senderId.equals(currentUserId) && receiverId.equals(selectedUserId)) ||
                                (senderId.equals(selectedUserId) && receiverId.equals(currentUserId))) {
                            existingChatRoom = chatRoom;
                            break;
                        }
                    }
                }

                if (existingChatRoom != null) {
                    chatRoomLiveData.setValue(existingChatRoom);
                } else {
                    ChatRoom newChatRoom = new ChatRoom(null, currentUserId, selectedUserId);
                    createChatRoom(newChatRoom);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                errorMessage.setValue("Lỗi khi tìm hoặc tạo phòng chat: " + databaseError.getMessage());
            }
        });
    }


    public void createChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.createChatRoom(chatRoom).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoomLiveData.setValue(chatRoom);
            } else {
                errorMessage.setValue("Failed to create chat room");
            }
        });
    }

    public void loadChatRoomsForUser(String currentUserId) {
        chatRoomRepository.getAllChatRooms().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatRoom> chatRooms = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                    if (chatRoom != null) {
                        String senderId = chatRoom.getSenderId();
                        String receiverId = chatRoom.getReceiverId();

                        if (senderId.equals(currentUserId) || receiverId.equals(currentUserId)) {
                            chatRooms.add(chatRoom);
                        }
                    }
                }
                chatRoomsLiveData.setValue(chatRooms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }


}
