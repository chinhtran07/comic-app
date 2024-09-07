package com.main.comicapp.models;

public class ChatRoom {
    private String roomId;
    private String senderId;
    private String receiverId;

    public ChatRoom() {
    }

    public ChatRoom(String roomId, String senderId, String receiverId) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
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
}
