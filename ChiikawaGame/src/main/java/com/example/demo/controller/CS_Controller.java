package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CS_Controller {
	

    
//	常見問題-顯示 (http://localhost:8080/CSQAData/QAShow)
    @GetMapping("/CSQAData/QAShow")
    public String showCSQAShow() {
        return "Customer_Service/QAShow";
    }
    
//  通知中心-顯示 (http://localhost:8080/CSFrontpage/MessageCenter)
    @GetMapping("/CSFrontpage/MessageCenter")
    public String showMessageCenter() {
        return "Customer_Service/MessageCenter";
    }
    
    

}
