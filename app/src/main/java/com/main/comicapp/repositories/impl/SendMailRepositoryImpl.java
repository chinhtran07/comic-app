package com.main.comicapp.repositories.impl;

import com.main.comicapp.models.User;
import com.main.comicapp.repositories.SendMailRepository;
import com.main.comicapp.utils.EmailSenderUtils;

import javax.mail.MessagingException;

public class SendMailRepositoryImpl implements SendMailRepository {

    @Override
    public void sendStatusChangeEmail(User user, boolean newStatus) throws MessagingException {
        String subject;
        String messageBody;

        if (newStatus) {
            subject = "Tài Khoản Đã Được Kích Hoạt";
            messageBody = "Thân chào " + user.getLastName() + " " + user.getFirstName() + ",\n\n"
                    + "Tài khoản của bạn đã được kích hoạt thành công. Bạn giờ đây có thể truy cập vào dịch vụ của chúng tôi.\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ";
        } else {
            subject = "Thông Báo Về Tình Trạng Tài Khoản";
            messageBody = "Thân chào " + user.getLastName() + " " + user.getFirstName() + ",\n\n"
                    + "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ với quản trị viên để được hỗ trợ và biết thêm thông tin chi tiết.\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ";
        }

        EmailSenderUtils.sendEmail(user.getEmail(), subject, messageBody);
    }

    @Override
    public void sendStatusRole(User user, String role) throws MessagingException {
        String subject = "Thông báo về cập nhật vai trò của user";
        String messageBody;

        if ("ADMIN".equalsIgnoreCase(role)) {
            messageBody = "Thân chào " + user.getLastName() + " " + user.getFirstName() + ",\n\n"
                    + "Chúc mừng bạn đã hợp tác với công ty của chúng tôi. Hiện tại, bạn là quản trị viên của ứng dụng đọc truyện Comic App.\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ";
        } else {
            messageBody = "Thân chào " + user.getLastName() + " " + user.getFirstName() + ",\n\n"
                    + "Vai trò của bạn trong ứng dụng đọc truyện Comic App hiện tại là: " + role + ".\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ";
        }

        EmailSenderUtils.sendEmail(user.getEmail(), subject, messageBody);
    }

}
