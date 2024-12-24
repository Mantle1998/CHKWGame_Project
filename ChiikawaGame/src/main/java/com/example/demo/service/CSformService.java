package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CSformRepository;
import com.example.demo.model.LoginRepository;
import com.example.demo.model.CSform;

@Service
public class CSformService {
	
	@Autowired
	private CSformRepository csfr;

	// 修改 addform 方法，將 userId 作為參數
    public CSform addform(String CSFormSort, String CSFormTitle, String CSFormContent, Date CSFormDate, Integer userId) {
        CSform csf = new CSform();
        csf.setCSFormSort(CSFormSort);
        csf.setCSFormTitle(CSFormTitle);
        csf.setCSFormContent(CSFormContent);
        csf.setCSFormDate(CSFormDate);  // 使用傳入的日期
        csf.setUserId(userId);  // 設定 userId
        
        // 儲存並返回結果
        return csfr.save(csf);
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
