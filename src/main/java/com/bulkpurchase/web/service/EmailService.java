package com.bulkpurchase.web.service;

import com.bulkpurchase.domain.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String userRealName, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yourEmail@gmail.com");
        message.setTo(email);
        message.setSubject(userRealName);
        message.setText(text);
        mailSender.send(message);
    }
}
