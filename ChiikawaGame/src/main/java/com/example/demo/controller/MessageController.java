package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.LoginBean;
import com.example.demo.model.Message;
import com.example.demo.service.MessageService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 根據訊息 ID 獲取單條訊息。
     * 
     * @param messageId 訊息 ID
     * @return 查詢到的訊息，或 404 狀態碼
     */
    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 創建一條新的訊息。
     * 
     * @param message 傳入的訊息對象
     * @return 創建成功的訊息，或內部伺服器錯誤
     */
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        try {
            message.setMessageDate(new Date()); // 設置當前日期為訊息的創建時間
            Message savedMessage = messageService.createMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 獲取所有訊息，或根據分類篩選訊息。
     * 
     * @param category 訊息分類，可選參數
     * @return 符合條件的訊息列表
     */
    @GetMapping
    public List<Message> getAllMessages(@RequestParam(required = false) String category) {
        if (category != null && !category.equals("all")) {
            return messageService.getMessagesByCategory(category);
        }
        return messageService.getAllMessages();
    }

    /**
     * 根據表單 ID 創建新的訊息。
     * 
     * @param csFormId 表單 ID
     * @return 創建成功的訊息，或錯誤信息
     */
    @PostMapping("/create/{csFormId}")
    public ResponseEntity<?> createMessageFromCSform(@PathVariable Integer csFormId) {
        try {
            Message message = messageService.createMessageFromCSform(csFormId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("建立訊息失敗：" + e.getMessage());
        }
    }

    /**
     * 檢查當前使用者是否已登入。
     * 
     * @param session 當前會話
     * @return 登入用戶的資料或未登入信息
     */
    @GetMapping("/checkLogin")
    public ResponseEntity<?> checkLoginStatus(HttpSession session) {
        try {
            // 從 session 中取得 LoginBean
            LoginBean user = (LoginBean) session.getAttribute("user");

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("使用者尚未登入，無法取得 userId");
            }

            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("檢查登入狀態時發生錯誤：" + e.getMessage());
        }
    }

    /**
     * 獲取指定用戶的所有訊息。
     * 
     * @param userId   用戶 ID
     * @param session  當前會話
     * @return 該用戶的訊息列表，或權限錯誤信息
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMessages(@PathVariable Integer userId, HttpSession session) {
        try {
            // 從 session 中取得 LoginBean
            LoginBean user = (LoginBean) session.getAttribute("user");

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("未登入，請先登入後再嘗試");
            }

            Integer sessionUserId = user.getUserId();

            // 驗證請求的 userId 與登入用戶是否相符
            if (!sessionUserId.equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("無權限查看其他用戶的訊息");
            }

            List<Message> userMessages = messageService.getMessagesByUserId(userId);
            return ResponseEntity.ok(userMessages);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("載入訊息失敗：" + e.getMessage());
        }
    }

    /**
     * 顯示訊息中心頁面。
     * 
     * @param model   用於添加頁面屬性
     * @param session 當前會話
     * @return 導向訊息中心頁面或登入頁面
     */
    @GetMapping("/messages/center")
    public String showMessageCenter(Model model, HttpSession session) {
        LoginBean user = (LoginBean) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userId", user.getUserId());
            return "Customer_Service/messageCenter"; // 返回訊息中心頁面
        } else {
            return "redirect:/login"; // 未登入則重定向到登入頁面
        }
    }
}