package com.example.demo.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaService {
	
	private final String SECRET_KEY = "6Ld2PqQqAAAAAB7D74o5rJjEmkuzqWXlFqg5VrUK";
    private final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    
    public boolean validateCaptcha(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        
        // 使用 MultiValueMap 來設置請求的表單參數
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", SECRET_KEY);
        requestBody.add("response", recaptchaResponse);
        
     // 設置標頭，Content-Type 設置為 application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
     // 創建 HttpEntity，將表單資料和標頭一起發送
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
        
     // 發送 POST 請求到 Google reCAPTCHA API
        ResponseEntity<String> responseEntity = restTemplate.exchange(
            VERIFY_URL, 
            HttpMethod.POST, 
            entity, 
            String.class
        );

     // 確保返回的響應不為空，並檢查是否驗證成功
        return responseEntity.getStatusCode() == HttpStatus.OK && 
               responseEntity.getBody().contains("\"success\": true");
        
    }
        
}