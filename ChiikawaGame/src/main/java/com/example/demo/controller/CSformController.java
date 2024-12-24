package com.example.demo.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.CSformRepository;
import com.example.demo.service.CSformService;
import com.example.demo.model.CSform;
import com.example.demo.model.LoginBean;

import jakarta.servlet.http.HttpSession;

@Controller
public class CSformController {

    @Autowired
    private CSformService csfs;
    
    @Autowired
    private CSformRepository CSformRepository;

 // 顯示會員資料總覽頁面 (http://localhost:8080/form_manage)
    @GetMapping("/form_manage")
    public String showform_manage() {
        return "Customer_Service/form_manage";
    }    
    
 // // 顯示填寫表單頁面 (http://localhost:8080/csform)
    @GetMapping("/csform")
    public String csform(HttpSession session, Model model) {
        LoginBean user = (LoginBean) session.getAttribute("user");

        // 檢查用戶是否登入
        if (user != null) {
            // 如果已登入，取得 userID 並傳遞到前端
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("showLoginPrompt", false);  // 隱藏登入提示
        } else {
            // 如果沒登入，顯示登入提示視窗
            model.addAttribute("showLoginPrompt", true);
        }

        return "Customer_Service/form";
    }
     
 // 新增表單資料 (http://localhost:8080/form_manage/formadd)
    @PostMapping("/form_manage/formadd")
    public String addform(
            @RequestParam String CSFormSort,
            @RequestParam String CSFormTitle,
            @RequestParam String CSFormContent,
            @RequestParam Date CSFormDate,
            HttpSession session) {

        LoginBean user = (LoginBean) session.getAttribute("user");

        if (user != null) {
            // 呼叫服務層，傳遞五個參數
            csfs.addform(
                CSFormSort,
                CSFormTitle,
                CSFormContent,
                CSFormDate,
                user.getUserId()  // 傳遞 userId
            );

            return "Customer_Service/form";  // 提交成功後返回表單頁面
        } else {
            // 如果用戶沒登入，提示用戶先登入
            return "redirect:/login";  // 重定向到登入頁面
        }
    }
    
    // 取得所有表單資料，回傳 JSON 格式 (http://localhost:8080/form_manage/json)
    @GetMapping("/form_manage/json")
    @ResponseBody
    public List<CSform> getform_manageJson() {
        return CSformRepository.findAll();
    }
      
    @PostMapping("/submitReply")
    @ResponseBody
    public ResponseEntity<String> submitReply(@RequestParam Integer CSFormId, @RequestParam String CSFormReply, @RequestParam Integer CSFormChack) {
        // 處理邏輯
        System.out.println("收到的 formId：" + CSFormId);
        System.out.println("回覆內容：" + CSFormReply);
        System.out.println("狀態：" + CSFormChack);
        
        // 假設你這裡是根據表單 ID 更新表單的回覆和狀態
        CSform existingForm = CSformRepository.findByCSFormId(CSFormId);
        
        if (existingForm != null) {
            existingForm.setCSFormReply(CSFormReply);
            existingForm.setCSFormChack(CSFormChack);
            CSformRepository.save(existingForm);
            return ResponseEntity.ok("回覆成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該表單");
        }
    }
    
    @GetMapping("/csformShowID")
    public String csformShowID(HttpSession session, Model model) {
        LoginBean user = (LoginBean) session.getAttribute("user");
        if (user != null) {
            // 如果已登入，取得 userID 並傳遞到前端
            model.addAttribute("userId", user.getUserId());
        } else {
            // 如果沒登入，顯示登入提示視窗
            model.addAttribute("showLoginPrompt", true);
        }
        return "Customer_Service/form";
    }






    
    
    
}


