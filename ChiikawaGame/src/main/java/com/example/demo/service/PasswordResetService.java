package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.LoginBean;
import com.example.demo.model.LoginRepository;




@Service
public class PasswordResetService {
	
	@Autowired
    private LoginRepository loginRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    public boolean sendResetPasswordLink(String email) {
        LoginBean user = loginRepository.findByUserEmail(email);
        if (user != null) {
            // 生成重設密碼的令牌
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusHours(1)); // 設置過期時間
            loginRepository.save(user);
            
         // 構建重設密碼連結
            String resetUrl = "http://localhost:8080/password/reset?token=" + token;

            
         // 發送電子郵件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("重設密碼");
            message.setText("請點擊以下連結來重設您的密碼：\n" + resetUrl);
            mailSender.send(message);
            
            return true;
        }
        return false;
    }
    
    public boolean resetPassword(String token, String newPassword) {
    	LoginBean user = loginRepository.findByResetToken(token);
        if (user != null && user.getTokenExpiry().isAfter(LocalDateTime.now())) {
            // 更新用戶密碼
        	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setUserPassword(encoder.encode(newPassword)); // 加密密碼並設置
            user.setResetToken(null);       // 清除令牌
            user.setTokenExpiry(null);      // 清除過期時間
            loginRepository.save(user);
            return true;
        }
        return false;
    }
    
}


