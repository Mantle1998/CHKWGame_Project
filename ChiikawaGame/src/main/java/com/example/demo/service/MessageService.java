package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CSform;
import com.example.demo.model.CSformRepository;
import com.example.demo.model.Message;
import com.example.demo.model.MessageRepository;

/**
 * MessageService 提供與 Message 實體相關的業務邏輯。
 * 包括從表單創建訊息、查詢訊息等功能。
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CSformRepository csformRepository;

    /**
     * 獲取所有訊息。
     * @return 訊息列表
     */
    public List<Message> getAllMessages() {
        try {
            // 調用 Repository 的自定義方法獲取所有訊息
            List<Message> messages = messageRepository.findAllMessages();
            System.out.println("Service層 - 查詢到的訊息數量: " + messages.size());

            // 紀錄訊息資訊
            if (messages.isEmpty()) {
                System.out.println("Service層 - 沒有找到任何訊息");
            } else {
                messages.forEach(msg -> {
                    System.out.println("Service層 - 訊息: " +
                            "ID=" + msg.getMessageId() +
                            ", 標題=" + msg.getMessageTitle() +
                            ", 分類=" + msg.getMessageSort());
                });
            }
            return messages;
        } catch (Exception e) {
            // 異常處理和日誌記錄
            System.err.println("Service層 - 獲取訊息時出現異常: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根據 CSform 創建新的 Message 實體。
     * @param csFormId 表單 ID
     * @return 創建的訊息
     */
    public Message createMessageFromCSform(Integer csFormId) {
        try {
            // 根據表單 ID 查詢 CSform 實體
            CSform csform = csformRepository.findById(csFormId)
                    .orElseThrow(() -> new RuntimeException("找不到指定的表單ID: " + csFormId));

            // 確保表單的必要資訊不為空
            if (csform.getCSFormReply() == null || csform.getUserId() == null) {
                throw new RuntimeException("表單缺少必要資訊");
            }

            // 創建新的 Message 實體並填充資料
            Message message = new Message();
            message.setMessageTitle("客服回覆: " + csform.getCSFormTitle());
            message.setMessageDate(new Date());
            message.setMessageUserId(csform.getUserId());
            message.setMessageContent(csform.getCSFormReply());

            // 儲存前記錄訊息資訊
            System.out.println("正在創建訊息：" + message.getMessageTitle());

            // 將訊息存入資料庫
            return messageRepository.save(message);

        } catch (Exception e) {
            // 異常處理和日誌記錄
            System.err.println("創建訊息時發生錯誤：" + e.getMessage());
            throw new RuntimeException("創建訊息失敗：" + e.getMessage());
        }
    }

    /**
     * 直接創建 Message。
     * @param message Message 實體
     * @return 創建的訊息
     */
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    /**
     * 根據分類獲取訊息。
     * @param category 訊息分類
     * @return 符合分類的訊息列表
     */
    public List<Message> getMessagesByCategory(String category) {
        if (category == null || category.equals("all")) {
            return getAllMessages();
        }
        return messageRepository.findByMessageSort(category);
    }

    /**
     * 根據 MessageId 查詢訊息。
     * @param messageId 訊息 ID
     * @return 查詢到的訊息
     */
    public Message getMessageById(Integer messageId) {
        return messageRepository.findByMessageId(messageId);
    }

    /**
     * 根據用戶 ID 查詢訊息。
     * @param userId 用戶 ID
     * @return 該用戶相關的訊息列表
     */
    public List<Message> getMessagesByUserId(Integer userId) {
        try {
            // 使用 Stream API 過濾符合用戶 ID 的訊息
            return messageRepository.findAll().stream()
                    .filter(message -> userId.equals(message.getMessageuserId())) // 使用正確的 getter
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // 異常處理和日誌記錄
            System.err.println("Service層 - 查詢用戶訊息時發生錯誤: " + e.getMessage());
            throw new RuntimeException("查詢用戶訊息失敗: " + e.getMessage());
        }
    }
}