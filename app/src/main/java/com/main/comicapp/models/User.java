package com.main.comicapp.models;

import com.main.comicapp.enums.UserRole;
import com.main.comicapp.utils.ValidateUtil;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthDate;

    public User() {
    }

    public User(String username, String password, String email, String userRole, String firstName, String lastName, String gender, String birthDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = UserRole.valueOf(userRole);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public static User toObject(Map<String, Object> data, String id) {
        User user = new User();
        user.setId(id);
        if (ValidateUtil.validateObject(data)) {
            user.setPassword((String)data.get("password"));
            user.setUsername((String)data.get("username"));
            user.setEmail((String)data.get("email"));
            user.setUserRole((String)data.get("userRole"));
        }
        return user;
    }
}
