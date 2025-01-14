package com.example.demo.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.CSform;
import com.example.demo.model.CSformRepository;

import jakarta.transaction.Transactional;

@Service
public class CSformService {
	
	@Autowired
	private CSformRepository csfr;

	@Transactional
    public CSform addform(String CSFormSort, String CSFormTitle, String CSFormContent, Date CSFormDate, Integer userId) {
        try {
            CSform csf = new CSform();
            csf.setCSFormSort(CSFormSort);
            csf.setCSFormTitle(CSFormTitle);
            csf.setCSFormContent(CSFormContent);
            csf.setCSFormDate(CSFormDate);
            csf.setUserId(userId);
            csf.setCSFormChack(0); // 設定初始狀態
            
            return csfr.save(csf);
        } catch (Exception e) {
            throw new RuntimeException("保存表單失敗: " + e.getMessage());
        }
    }

	 // 提交回覆並更新狀態
    public boolean submitReply(int CSFormId, String CSFormReply, int CSFormChack) {
        // 查詢指定的表單
        CSform form = csfr.findByCSFormId(CSFormId);
        
        // 若表單存在，則進行更新
        if (form != null) {
            form.setCSFormReply(CSFormReply);  // 更新回覆內容
            form.setCSFormChack(CSFormChack);  // 更新狀態
            csfr.save(form);  // 儲存更新的表單
            return true;
        }
        return false;
    }
	
}