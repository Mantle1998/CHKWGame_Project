package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.model.CSform;
import com.example.demo.model.LoginBean;
import com.example.demo.model.Message;
import com.example.demo.service.CSformService;
import com.example.demo.service.MessageService;
import com.example.demo.model.CSformRepository;


import jakarta.servlet.http.HttpSession;

@Controller
public class CSformController {

    @Autowired
    private CSformService csfs;
    
    @Autowired
    private CSformRepository CSformRepository;    
    
    @Autowired
    private MessageService messageService;  
    
 // 顯示填寫表單頁面 (http://localhost:8080/csform)
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
    @ResponseBody
    public ResponseEntity<?> addform(
            @RequestParam String CSFormSort,
            @RequestParam String CSFormTitle,
            @RequestParam String CSFormContent,
            @RequestParam String CSFormDate,
            HttpSession session) {
        
        try {
            LoginBean user = (LoginBean) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登入，請先登入後再嘗試");
            }

            // 驗證表單數據
            if (CSFormSort == null || CSFormTitle == null || CSFormContent == null) {
                return ResponseEntity.badRequest().body("表單資料不完整");
            }

            // 轉換日期格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(CSFormDate);

            // 儲存表單
            CSform savedForm = csfs.addform(
                CSFormSort,
                CSFormTitle,
                CSFormContent,
                date,
                user.getUserId()
            );

            return ResponseEntity.ok(savedForm);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("系統錯誤，請稍後再試");
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
    public ResponseEntity<String> submitReply(@RequestParam Integer CSFormId, 
                                            @RequestParam String CSFormReply, 
                                            @RequestParam Integer CSFormChack) {
        try {
            // 處理回覆邏輯
            CSform existingForm = CSformRepository.findByCSFormId(CSFormId);
            
            if (existingForm != null) {
                // 更新表單資料
                existingForm.setCSFormReply(CSFormReply);
                existingForm.setCSFormChack(CSFormChack);
                CSformRepository.save(existingForm);
                
                // 自動建立客服通知訊息
                Message message = new Message();
                message.setMessageSort("客服通知");  // 預設分類為客服通知
                message.setMessageTitle("客服回覆通知"); 
                message.setMessageContent("您的表單「" + existingForm.getCSFormTitle() + "」已收到回覆：" + CSFormReply);
                message.setMessageDate(new Date());
                message.setMessageUserId(existingForm.getUserId());
                
                messageService.createMessage(message);
                
                return ResponseEntity.ok("回覆成功");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該表單");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 印出錯誤堆疊以便除錯
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("處理回覆時發生錯誤：" + e.getMessage());
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