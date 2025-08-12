package com.hostelManagementSystem.HostelManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Hostel Management: Password Reset Code");
        message.setText("Your reset code is: " + code);
        message.setFrom("yourgmail@gmail.com"); // same as spring.mail.username
        mailSender.send(message);
    }
}
