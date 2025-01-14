package com.example.demo.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Message類別代表了數據庫中的Message表，並使用JPA註解進行映射。
 * 每個屬性對應表中的一列，用於存儲和操作訊息數據。
 */
@Entity
@Table(name = "Message") // 將此類映射到名為Message的數據表
public class Message {

    // 定義主鍵，並設置為自動生成策略為IDENTITY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageId") // 對應表中的MessageId列
    private Integer messageId;

    // 與表中MessageformId列對應，用於存儲表單ID
    @Column(name = "MessageformId")
    private Integer messageformId;

    // 與表中MessagesenderId列對應，用於存儲發送者ID
    @Column(name = "MessagesenderId")
    private Integer messagesenderId;

    // 與表中MessageuserId列對應，用於存儲接收者用戶ID
    @Column(name = "MessageuserId")
    private Integer messageuserId;

    // 與表中MessageSort列對應，用於存儲訊息類型，為必填項
    @Column(name = "MessageSort", nullable = false)
    private String messageSort;

    // 與表中MessageTitle列對應，用於存儲訊息標題，為必填項
    @Column(name = "MessageTitle", nullable = false)
    private String messageTitle;

    // 與表中MessageContent列對應，用於存儲訊息內容
    @Column(name = "MessageContent")
    private String messageContent;

    // 與表中MessageDate列對應，用於存儲訊息的日期，格式為"yyyy-MM-dd"，時區為GMT+8
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "MessageDate")
    @Temporal(TemporalType.DATE) // 指定該欄位存儲的是日期型數據
    private Date messageDate;

    // Getters and Setters方法，用於訪問和修改類的屬性
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getMessageuserId() {
        return messageuserId;
    }

    public void setMessageUserId(Integer messageuserId) {
        this.messageuserId = messageuserId;
    }

    public String getMessageSort() {
        return messageSort;
    }

    public void setMessageSort(String messageSort) {
        this.messageSort = messageSort;
    }

    public Integer getMessagesenderId() {
        return messagesenderId;
    }

    public void setMessagesenderId(Integer messagesenderId) {
        this.messagesenderId = messagesenderId;
    }

    public Integer getMessageformId() {
        return messageformId;
    }

    public void setMessageformId(Integer messageformId) {
        this.messageformId = messageformId;
    }
}