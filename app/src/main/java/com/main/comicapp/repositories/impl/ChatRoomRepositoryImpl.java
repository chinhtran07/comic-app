package com.main.comicapp.repositories.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.main.comicapp.models.ChatRoom;
import com.main.comicapp.repositories.ChatRoomRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.main.comicapp.repositories.DataCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final DatabaseReference db;

    public ChatRoomRepositoryImpl() {
        db = FirebaseDatabase.getInstance().getReference("chatRooms");
    }

    @Override
    public Query getAllChatRooms() {
        return db;
    }

    @Override
    public Task<Void> createChatRoom(ChatRoom chatRoom) {
        String roomId = db.push().getKey();
        chatRoom.setRoomId(roomId);
        return db.child(roomId).setValue(chatRoom);
    }

    @Override
    public void getChatRoomsForUser(String userId, DataCallback<List<ChatRoom>> callback) {
        Query senderQuery = db.orderByChild("senderId").equalTo(userId);
        Query receiverQuery = db.orderByChild("receiverId").equalTo(userId);

        List<ChatRoom> chatRooms = new ArrayList<>();

        // Lấy chat room nơi user là người gửi
        senderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot senderSnapshot) {
                for (DataSnapshot snapshot : senderSnapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                    if (chatRoom != null) {
                        chatRooms.add(chatRoom);
                    }
                }

                // Sau khi lấy xong, lấy tiếp chat room nơi user là người nhận
                receiverQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot receiverSnapshot) {
                        for (DataSnapshot snapshot : receiverSnapshot.getChildren()) {
                            ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                            if (chatRoom != null && !chatRooms.contains(chatRoom)) {
                                chatRooms.add(chatRoom);
                            }
                        }

                        // Gọi callback với danh sách các chat rooms
                        callback.onSuccess(chatRooms);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onFailure(databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }
        });
    }
}
