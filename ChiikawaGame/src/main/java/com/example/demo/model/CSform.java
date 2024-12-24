package com.example.demo.model;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

// 指定這是一個實體類別，對應到資料庫中的表格
@Entity
// 指定資料庫表格名稱為 "CSForm"
@Table(name = "CSForm")
public class CSform {

    // 指定主鍵 (Primary Key) 並自動生成 ID 值
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSFormId")
    private Integer CSFormId;
    // 用於儲存表格中的 ID 欄位

    // 指定 "CSFormSort" 欄位，不可為空值
    @Column(name = "CSFormSort", nullable = false)
    private String CSFormSort;
    // 用於儲存分類資訊

    // 指定 "CSFormTitle" 欄位，不可為空值
    @Column(name = "CSFormTitle", nullable = false)
    private String CSFormTitle;
    // 用於儲存標題資訊

    // 指定 "CSFormContent" 欄位，不可為空值
    @Column(name = "CSFormContent", nullable = false)
    private String CSFormContent;
    // 用於儲存文章內容
    
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8") // 只顯示日期，不顯示時間
    @Column(name = "CSFormDate")
    @Temporal(TemporalType.DATE)  // 設定為日期型態
    private Date CSFormDate;
    // 用於儲存文章建立的日期與時間
    
    // 指定 "CSformChack" 欄位，不可為空值
    @Column(name = "CSFormChack")
    private Integer CSFormChack;
    // 用於儲存文章的狀態（例如：0 表示未審核，1 表示已發布）
    
    // 指定 "CSFormContent" 欄位，不可為空值
    @Column(name = "CSFormReply")
    private String CSFormReply;
    // 用於儲存回覆內容
    
    //串聯user資料表
//    @ManyToOne
//    @JoinColumn(name = "userId")  // 外鍵欄位
//    private LoginBean UserId;
    
    @Column(name = "userId")
    private Integer userId;
    
    // 以下是 getter 和 setter 方法，用於取得和設定屬性值

    public Integer getCSFormId() {
        return CSFormId;
    }

    public void setCSFormId(Integer cSFormId) {
        this.CSFormId = cSFormId;
    }

    public String getCSFormSort() {
        return CSFormSort;
    }

    public void setCSFormSort(String cSFormSort) {
        this.CSFormSort = cSFormSort;
    }

    public String getCSFormTitle() {
        return CSFormTitle;
    }

    public void setCSFormTitle(String cSFormTitle) {
        this.CSFormTitle = cSFormTitle;
    }

    public String getCSFormContent() {
        return CSFormContent;
    }

    public void setCSFormContent(String cSFormContent) {
        this.CSFormContent = cSFormContent;
    }

    public Date getCSFormDate() {
        return CSFormDate;
    }

    public void setCSFormDate(Date cSFormDate) {
        this.CSFormDate = cSFormDate;
    }

    public Integer getCSFormChack() {
        return CSFormChack;
    }

    public void setCSFormChack(Integer cSFormChack) {
        this.CSFormChack = cSFormChack;
    }
    
    public String getCSFormReply() {
        return CSFormReply;
    }

    public void setCSFormReply(String cSFormReply) {
        this.CSFormReply = cSFormReply;
    }
    

	public static void addAttribute(String string, List<CSform> csForms) {
		// TODO Auto-generated method stub
		
	}
	
//    public LoginBean getUserId() {
//        return UserId;
//    }
//
//    public void setUserId(LoginBean userId) {
//        this.UserId = userId;
//    }

	 public Integer getUserId() {
	      return userId;
	    }

	 public void setUserId(Integer userId) {
	      this.userId = userId;
	    }
}
