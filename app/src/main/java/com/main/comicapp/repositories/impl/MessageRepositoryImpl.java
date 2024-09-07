package com.main.comicapp.repositories.impl;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.main.comicapp.models.Message;
import com.main.comicapp.repositories.MessageRepository;
import com.main.comicapp.repositories.DataCallback;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageRepositoryImpl implements MessageRepository {

    private final DatabaseReference databaseReference;

    public MessageRepositoryImpl() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("messages");
    }

    @Override
    public void saveMessage(Message message) {
        String messageId = databaseReference.push().getKey();
        if (messageId != null) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("senderId", message.getSenderId());
            messageData.put("chatRoomId", message.getChatRoomId());
            messageData.put("content", message.getContent());
            messageData.put("timestamp", message.getTimestamp());

            databaseReference.child(messageId).setValue(messageData);
        }
    }

    @Override
    public void getAllMessagesByChatRoomId(String chatRoomId, final DataCallback<List<Message>> callback) {
        databaseReference.orderByChild("chatRoomId").equalTo(chatRoomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Message> messageList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            if (message != null) {
                                messageList.add(message);
                            }
                        }
                        callback.onSuccess(messageList);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onFailure(error.toException());
                    }
                });
    }

    @Override
    public void getLastMessageByChatRoomId(String chatRoomId, final DataCallback<Message> callback) {
        Query lastMessageQuery = databaseReference.orderByChild("chatRoomId")
                .equalTo(chatRoomId)
                .limitToLast(1);

        lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message lastMessage = snapshot.getValue(Message.class);
                        if (lastMessage != null) {
                            callback.onSuccess(lastMessage);
                        } else {
                            callback.onFailure(new Exception("No message found"));
                        }
                    }
                } else {
                    callback.onFailure(new Exception("No message found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }
}
