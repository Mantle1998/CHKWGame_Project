package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.CSQAData;
import com.example.demo.service.QADataService;
import com.example.demo.model.CSQADataRepository;

@Controller  // 使用 @Controller 來渲染視圖
@CrossOrigin(origins = "*")  // 允許所有來源的跨域請求
public class CSQADataController {

    @Autowired
    private CSQADataRepository CSQADataRepository;
    
    @Autowired
    private QADataService qads;
    
    // 取得所有常見問題內容，回傳 JSON 格式 (http://localhost:8080/QA_manage/json)
    @GetMapping("/QA_manage/json")
    @ResponseBody
    public List<CSQAData> getQA_manageJson() {
        return CSQADataRepository.findAll();
    }
    
    // 新增常見問題的 API
    @PostMapping("/QA_manage/addQA")
    @ResponseBody
    public String addQA(@RequestBody Map<String, String> payload) {
        String CSQADataSort = payload.get("CSQADataSort");
        String CSQADataTitle = payload.get("CSQADataTitle");
        String CSQADataContent = payload.get("CSQADataContent");

        CSQAData newQA = qads.addQA(CSQADataSort, CSQADataTitle, CSQADataContent);
        return "新增成功，Q&A編號: " + newQA.getCSQADataId();
    }


    // 根據ID找出對應資料至修改窗口中(http://localhost:8080/QA_manage/json/QADataId/3)
    @GetMapping("/QA_manage/json/QADataId/{CSQADataId}")
    @ResponseBody
    public CSQAData getQAById(@PathVariable Integer CSQADataId) {
        return qads.getQAById(CSQADataId)
                 .orElseThrow(() -> new RuntimeException("找不到指定的資料"));
    }

    // 修改資料(http://localhost:8080/QA_manage/json/3)
    @PutMapping("/QA_manage/json/{CSQADataId}")
    @ResponseBody  // 重要：確保直接返回 JSON
    public CSQAData updateQAData(
        @PathVariable Integer CSQADataId, 
        @RequestBody CSQAData updatedData
    ) {
        // 直接調用服務層的更新方法
        return qads.updateQAData(CSQADataId, updatedData);
    }
    
    // 新增的 API 方法：獲取所有可顯示的問題分類
    @GetMapping("/api/csqa/categories")
    @ResponseBody
    public ResponseEntity<Map<String, List<CSQAData>>> getAllCategories() {
        String[] categories = {"帳號相關", "買家相關", "賣家相關", "平台守則", "其他問題"};
        
        Map<String, List<CSQAData>> categoryData = new HashMap<>();
        
        for (String category : categories) {
            List<CSQAData> data = qads.getQABySort(category);
            categoryData.put(category, data);
        }
        
        return ResponseEntity.ok(categoryData);
    }
    

    

}