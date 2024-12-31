package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordPageController {

    // 顯示忘記密碼頁面
    @GetMapping("/password/forgot")
    public String forgotPasswordPage() {
        return "/loginOut/forgotPassword";  // 返回 "forgotPassword.html" 模板頁面
    }

    // 顯示重設密碼頁面，並接受 token 作為請求參數
    @GetMapping("/password/reset")
    public String resetPasswordPage(@RequestParam("token") String token) {
        // 可以在這裡驗證 token 的有效性
        return "/loginOut/resetPassword";  // 返回 "resetPassword.html" 模板頁面
    }
}
