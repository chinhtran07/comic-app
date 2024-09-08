package com.main.comicapp.repositories.impl;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.main.comicapp.models.ChatRoom;
import com.main.comicapp.repositories.ChatRoomRepository;
import com.google.android.gms.tasks.Task;

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



}
