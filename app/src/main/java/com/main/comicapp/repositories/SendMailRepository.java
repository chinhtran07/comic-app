package com.main.comicapp.repositories;

import com.main.comicapp.models.User;

import javax.mail.MessagingException;

public interface SendMailRepository {
    void sendStatusChangeEmail(User user, boolean newStatus) throws MessagingException;
}
