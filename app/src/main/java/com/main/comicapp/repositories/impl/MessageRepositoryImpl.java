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

import java.util.ArrayList;
import java.util.List;

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
            databaseReference.child(messageId).setValue(message);
        }
    }

    @Override
    public void getAllMessagesByChatRoomId(String chatRoomId, final DataCallback<List<Message>> callback) {
        databaseReference.orderByChild("chatRoomId").equalTo(chatRoomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                        callback.onSuccess(lastMessage);
                        return;
                    }
                } else {
                    callback.onSuccess(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }
}
