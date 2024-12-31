package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.EmailRequest;
import com.example.demo.model.ResetPasswordRequest;
import com.example.demo.service.PasswordResetService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
public class PasswordController {
    
    @Autowired
    private PasswordResetService passwordResetService;

    // 處理用戶提交忘記密碼請求
    @PostMapping("/forgot")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody EmailRequest emailRequest) {
        boolean emailSent = passwordResetService.sendResetPasswordLink(emailRequest.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (emailSent) {
            response.put("success", true);
            response.put("message", "重設密碼連結已發送到您的電子郵箱！");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "該電子郵件未註冊！");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // 處理用戶提交的新密碼請求
    @PutMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean resetSuccessful = passwordResetService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
        Map<String, Object> response = new HashMap<>();
        if (resetSuccessful) {
            response.put("success", true);
            response.put("message", "密碼重設成功！");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "無效或過期的重設令牌！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}