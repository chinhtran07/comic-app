package com.main.comicapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.main.comicapp.models.Message;
import com.main.comicapp.repositories.DataCallback;
import com.main.comicapp.repositories.MessageRepository;
import com.main.comicapp.repositories.impl.MessageRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageViewModel extends ViewModel {

    private final MessageRepository messageRepository;
    private final Map<String, MutableLiveData<List<Message>>> messagesLiveDataMap;
    private final MutableLiveData<Message> lastMessageLiveData;
    private final MutableLiveData<String> errorMessageLiveData;

    public MessageViewModel() {
        messageRepository = new MessageRepositoryImpl();
        messagesLiveDataMap = new HashMap<>();
        lastMessageLiveData = new MutableLiveData<>();
        errorMessageLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Message>> getMessagesLiveData(String chatRoomId) {
        if (!messagesLiveDataMap.containsKey(chatRoomId)) {
            messagesLiveDataMap.put(chatRoomId, new MutableLiveData<>());
        }
        return messagesLiveDataMap.get(chatRoomId);
    }

    public LiveData<Message> getLastMessageLiveData() {
        return lastMessageLiveData;
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public void loadMessagesByChatRoom(String chatRoomId) {
        if (!messagesLiveDataMap.containsKey(chatRoomId)) {
            messagesLiveDataMap.put(chatRoomId, new MutableLiveData<>());
        }
        messagesLiveDataMap.get(chatRoomId).setValue(null);

        messageRepository.getAllMessagesByChatRoomId(chatRoomId, new DataCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> data) {
                messagesLiveDataMap.get(chatRoomId).setValue(data);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessageLiveData.setValue(e.getMessage());
            }
        });
    }

    public void loadLastMessageByChatRoom(String chatRoomId) {
        lastMessageLiveData.setValue(null);

        messageRepository.getLastMessageByChatRoomId(chatRoomId, new DataCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                lastMessageLiveData.setValue(message);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessageLiveData.setValue(e.getMessage());
            }
        });
    }

    public void saveMessage(Message message) {
        messageRepository.saveMessage(message);
    }
}
