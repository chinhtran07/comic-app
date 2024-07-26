package com.main.comicapp.models;

import java.util.HashMap;
import java.util.Map;

public class UserSession {
    private String id;
    private String userId;
    private long lastLoginTime;

    public UserSession(String id, String userId, long lastLoginTime) {
        this.id = id;
        this.userId = userId;
        this.lastLoginTime = lastLoginTime;
    }

    public UserSession() {
        // Default constructor required for Firebase
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public static Map<String, Object> toMap(UserSession userSession) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userSession.getUserId());
        data.put("lastLoginTime", userSession.getLastLoginTime());
        return data;
    }

}
