package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * MessageRepository 介面用於對 Message 實體進行數據訪問操作。
 * 繼承 JpaRepository，提供了常見的 CRUD 方法，並可添加自定義查詢。
 * @param <Message> 實體類型
 * @param <Integer> 主鍵類型
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * 自定義方法：根據 messageId 查找對應的 Message 實體。
     * @param messageId 訊息的唯一標識符
     * @return 與指定 messageId 對應的 Message 實體
     */
    Message findByMessageId(Integer messageId);

    /**
     * 自定義方法：根據訊息類型 messageSort 查找對應的 Message 列表。
     * @param messageSort 訊息的類型
     * @return 符合指定類型的 Message 實體列表
     */
    List<Message> findByMessageSort(String messageSort);

    /**
     * 自定義查詢：查找所有的 Message 實體。
     * 使用 JPQL（Java Persistence Query Language）進行自定義查詢。
     * @return 數據庫中所有的 Message 實體列表
     */
    @Query("SELECT m FROM Message m")
    List<Message> findAllMessages();
}