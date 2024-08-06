package com.main.comicapp.utils;

import com.main.comicapp.config.SendMailConfig;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSenderUtils {

    public static void sendEmail(String recipientEmail, String subject, String messageBody) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", SendMailConfig.SMTP_HOST_NAME);
        props.put("mail.smtp.port", SendMailConfig.SMTP_PORT);
        props.put("mail.smtp.auth", SendMailConfig.SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", SendMailConfig.SMTP_STARTTLS_ENABLE);

        // Tạo phiên SMTP
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SendMailConfig.SMTP_AUTH_USER, SendMailConfig.SMTP_AUTH_PWD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SendMailConfig.SMTP_AUTH_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
