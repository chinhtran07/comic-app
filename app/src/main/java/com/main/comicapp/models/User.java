package com.main.comicapp.models;


import com.main.comicapp.enums.UserRole;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private UserRole userRole;

    public User() {

    }

    public User(String username, String password, String email, String userRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = UserRole.valueOf(userRole);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole.name();
    }

    public void setUserRole(String userRole) {
        this.userRole = UserRole.valueOf(userRole);
    }
}
