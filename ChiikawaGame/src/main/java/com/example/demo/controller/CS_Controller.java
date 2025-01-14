package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CS_Controller {
  
// 	客服表單-管理 (http://localhost:8080/form_manage)
    @GetMapping("/form_manage")
    public String showform_manage() {
        return "Customer_Service/form_manage";
    }
    
// 	常見問題-管理 (http://localhost:8080/QA_manage)
    @GetMapping("/QA_manage")
    public String showQA_manage() {
        return "Customer_Service/QA_manage";
    }
    
//  訊息中心-管理(http://localhost:8080/CSFrontpage/MessageMange)
    @GetMapping("/CSFrontpage/MessageMange")
    public String showMessageMange() {
        return "Customer_Service/Message_mange";
    }
    
//	客服表單-前台(http://localhost:8080/csform)
//	因有撰寫邏輯判斷、故內容放置於Controller內
    
//	常見問題-前台 (http://localhost:8080/CSQAData/QAShow)
    @GetMapping("/CSQAData/QAShow")
    public String showCSQAShow() {
        return "Customer_Service/QAShow";
    }
    
//  訊息中心-前台 (http://localhost:8080/CSFrontpage/MessageCenter)
    @GetMapping("/CSFrontpage/MessageCenter")
    public String showMessageCenter() {
        return "Customer_Service/MessageCenter";
    }
    
    
    

    
    

}