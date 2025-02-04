package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.AdInfo;
import com.example.demo.model.AdInfoRepository;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;
import java.util.stream.Collectors;


//處理廣告的前端請求
@Controller
public class AdInfoController {

    @Autowired
    private AdInfoRepository adRepository;

    // 廣告總覽的頁面 (http://localhost:8080/adInfo)
    @GetMapping("/adInfo")
    public String showAdInfo() {
        return "adInfo/adInfo";
    }

    // 取得所有廣告，回傳 JSON 格式 (http://localhost:8080/adInfo/json)
    @GetMapping("/adInfo/json")
    @ResponseBody
    public List<Map<String, Object>> getAdInfoJson() { //List用來存放資料，每一筆資料是一個 Map<String, Object>
        return adRepository.findAll().stream().map( //.stream()轉換成資料流 .map()將class adInfo轉換為Map
        		ad -> { //Lambda 表達式將每個 AdInfo 物件轉換為一個 Map<String, Object>
            Map<String, Object> adMap = new HashMap<>(); //新建一個 HashMap，用來存放該 AdInfo 的各個屬性
            adMap.put("adId", ad.getAdId());
            adMap.put("adName", ad.getAdName());
            adMap.put("adInfo", ad.getAdInfo());
            adMap.put("adImageBase64", 
                    ad.getAdImage() != null ? Base64.getEncoder().encodeToString(ad.getAdImage()) : null); //將圖片轉換為 Base64 編碼的字串
            return adMap; //每個 ad 都會被轉換為一個 Map<String, Object>
        }).collect(Collectors.toList()); //.collect()將結果轉為List
    }

    // 新增廣告 (http://localhost:8080/adInfo/add)
    @PostMapping("/adInfo/add")
    @ResponseBody
    public ResponseEntity<String> addAdInfo(@RequestParam("adName") String adName,
            @RequestParam("adInfo") String adInfo, @RequestParam("adImage") MultipartFile adImage) {
        try {
            AdInfo ad = new AdInfo();
            ad.setAdName(adName);
            ad.setAdInfo(adInfo);

            // 將圖片文件轉換為二進位數據
            if (!adImage.isEmpty()) {
                ad.setAdImage(adImage.getBytes());
            }

            adRepository.save(ad);
            return ResponseEntity.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("新增失敗：" + e.getMessage());
        }
    }

    // 更新廣告 (http://localhost:8080/adInfo/update)
    @PostMapping("/adInfo/update")
    @ResponseBody
    public ResponseEntity<String> updateAdInfo(@RequestParam("adId") Integer adId,
            @RequestParam("adName") String adName, @RequestParam("adInfo") String adInfo,
            @RequestParam(value = "adImage", required = false) MultipartFile adImage) {
        try {
            // 根據 ID 查找現有的廣告資料
            AdInfo existingAd = adRepository.findById(adId).orElseThrow(() -> new RuntimeException("廣告資料不存在"));

            // 更新文字欄位
            existingAd.setAdName(adName);
            existingAd.setAdInfo(adInfo);

            // 如果有新圖片，則更新圖片數據；否則保留原圖片
            if (adImage != null && !adImage.isEmpty()) {
                existingAd.setAdImage(adImage.getBytes());
            }

            // 儲存更新後的廣告資料
            adRepository.save(existingAd);
            return ResponseEntity.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("更新失敗：" + e.getMessage());
        }
    }
}
