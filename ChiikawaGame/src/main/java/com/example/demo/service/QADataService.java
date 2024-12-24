package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CSQADataRepository;
import com.example.demo.model.CSformRepository;
import com.example.demo.model.CSQAData;
import com.example.demo.model.CSform;

import jakarta.transaction.Transactional;

@Service
public class QADataService {
	
	@Autowired
	private CSQADataRepository csqar;
	
	// 取得所有 Q&A
    public List<CSQAData> getAllQA() {
        return csqar.findAll();
    }

    // 取得單筆 Q&A
    public Optional<CSQAData> getQAById(Integer CSQADataId) {
        return csqar.findById(CSQADataId);
    }
    
    // 新增方法：根據分類和顯示狀態查詢
    public List<CSQAData> getQABySort(String sort) {
        return csqar.findByCSQADataSortAndCSQADataChack(sort, 1);
    }
    
    // 新增方法：獲取所有可顯示的Q&A
    public List<CSQAData> getAllVisibleQA() {
        return csqar.findByCSQADataChack(1);
    }

    // 新增常見問題
    public CSQAData addQA(String CSQADataSort, String CSQADataTitle, String CSQADataContent) {
        CSQAData csqa = new CSQAData();
        csqa.setCSQADataSort(CSQADataSort);
        csqa.setCSQADataTitle(CSQADataTitle);
        csqa.setCSQADataContent(CSQADataContent);

        // 設置當前時間為日期
        csqa.setCSQADataDATE(new Date());  // 自動設置為當前時間

        csqa.setCSQADataChack(0); // 預設為未上架

        // 儲存並返回結果
        return csqar.save(csqa);  // 儲存資料並返回
    }

    // 更新常見問題
    @Transactional
    public CSQAData updateQAData(Integer csqadataId, CSQAData updatedData) {
        // 先檢查資料是否存在
        CSQAData existingData = csqar.findById(csqadataId)
                .orElseThrow(() -> new RuntimeException("找不到指定的資料"));

        // 更新除了 ID 之外的其他欄位
        existingData.setCSQADataSort(updatedData.getCSQADataSort());
        existingData.setCSQADataTitle(updatedData.getCSQADataTitle());
        existingData.setCSQADataContent(updatedData.getCSQADataContent());
        existingData.setCSQADataChack(updatedData.getCSQADataChack());
        
        // 設定更新時間
        existingData.setCSQADataDATE(new Date());

        return csqar.save(existingData);
    }	
    
    
}