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

// 指定這是一個實體類別，對應到資料庫中的表格
@Entity
// 指定資料庫表格名稱為 "CSQAData"
@Table(name = "CSQAData")
public class CSQAData {

    // 指定主鍵 (Primary Key) 並自動生成 ID 值
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSQADataId")
    private Integer CSQADataId;
    // 用於儲存表格中的 ID 欄位

    // 指定 "CSQADataSort" 欄位，不可為空值
    @Column(name = "CSQADataSort",nullable = false)
    private String CSQADataSort;
    // 用於儲存分類資訊

    // 指定 "CSQADataTitle" 欄位，不可為空值
    @Column(name = "CSQADataTitle", nullable = false)
    private String CSQADataTitle;
    // 用於儲存標題資訊

    // 指定 "CSQADataContent" 欄位，不可為空值
    @Column(name = "CSQADataContent", nullable = false)
    private String CSQADataContent;
    // 用於儲存文章內容

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8") // 只顯示日期，不顯示時間
    @Column(name = "CSQADataDATE")
    @Temporal(TemporalType.DATE) // 設定為日期型態
    private Date CSQADataDATE;
    // 用於儲存文章建立的日期與時間
    
    // 指定 "CSQADataChack" 欄位，不可為空值
    @Column(name = "CSQADataChack", nullable = false)
    private Integer CSQADataChack;
    // 用於儲存文章的狀態（例如：0 表示未審核，1 表示已發布）

 // 以下是 getter 和 setter 方法，用於取得和設定屬性值
    
	public Integer getCSQADataId() {
		return CSQADataId;
	}

	public void setCSQADataId(Integer cSQADataId) {
		this.CSQADataId = cSQADataId;
	}

	public String getCSQADataSort() {
		return CSQADataSort;
	}

	public void setCSQADataSort(String cSQADataSort) {
		this.CSQADataSort = cSQADataSort;
	}

	public String getCSQADataTitle() {
		return CSQADataTitle;
	}

	public void setCSQADataTitle(String cSQADataTitle) {
		this.CSQADataTitle = cSQADataTitle;
	}

	public String getCSQADataContent() {
		return CSQADataContent;
	}

	public void setCSQADataContent(String cSQADataContent) {
		this.CSQADataContent = cSQADataContent;
	}

	public Date getCSQADataDATE() {
		return CSQADataDATE;
	}

	public void setCSQADataDATE(Date cSQADataDATE) {
		this.CSQADataDATE = cSQADataDATE;
	}

	public Integer getCSQADataChack() {
		return CSQADataChack;
	}

	public void setCSQADataChack(Integer cSQADataChack) {
		this.CSQADataChack = cSQADataChack;
	}

}