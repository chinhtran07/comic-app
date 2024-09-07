package com.main.comicapp.models;

public class Message {
    private String senderId;
    private String content;
    private long timestamp;
    private String chatRoomId;

    public Message() {
    }

    public Message(String senderId, String chatRoomId, String content, long timestamp) {
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
