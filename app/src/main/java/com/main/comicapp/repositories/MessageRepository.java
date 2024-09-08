package com.main.comicapp.repositories;

import com.main.comicapp.models.Message;

import java.util.List;

public interface MessageRepository {
    void saveMessage(Message message);
    void getAllMessagesByChatRoomId(String chatRoomId, DataCallback<List<Message>> callback);
    void getLastMessageByChatRoomId(String chatRoomId, DataCallback<Message> callback);
}
