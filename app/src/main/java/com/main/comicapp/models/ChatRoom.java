package com.main.comicapp.models;

public class ChatRoom {
    private String roomId;
    private String senderId;
    private String receiverId;
    private Message lastMessage;

    public ChatRoom() {
    }

    public ChatRoom(String roomId, String senderId, String receiverId) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public ChatRoom(String roomId, String senderId, String receiverId, Message lastMessage) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.lastMessage = lastMessage;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
