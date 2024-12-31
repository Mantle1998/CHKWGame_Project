package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;



@Component
public class EmailTest {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo("ieet189kutest666@gmail.com"); // 收件人
            helper.setSubject("測試郵件"); // 主旨
            helper.setText("<p>這是一封測試郵件。</p>", true); // 內容（支援 HTML）
            mailSender.send(message);
            System.out.println("郵件發送成功！");
        } catch (MessagingException e) {
            System.err.println("郵件發送失敗：" + e.getMessage());
        }
    }
}
