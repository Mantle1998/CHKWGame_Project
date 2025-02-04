package com.example.demo.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.OrderItemDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ReviewPhotos;
import com.example.demo.model.ReviewPhotosRepository;
import com.example.demo.model.ReviewRepository;
import com.example.demo.model.Reviews;
import com.example.demo.model.UserInfo;
import com.example.demo.service.OrderService;
import com.example.demo.service.ReviewService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
	
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private ReviewPhotosRepository reviewPhotosRepository;
    
    @Autowired
    private OrderService orderService;
    
    // 映射: 使用者訂單管理頁面(要改成order那邊的)
    // 路徑: http://localhost:8080/reviews/orderList
    @GetMapping("/orderList")
    public String orderListToReviewItems() {
        return "review/reviewItems"; 
    }
     
    // 根據訂單編號獲取商品資訊
    // 路徑: http://localhost:8080/reviews/{orderId}/items
    // 範例: http://localhost:8080/reviews/1/items
    @GetMapping("/{orderId}/items")
    @ResponseBody
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> orderItems = reviewService.getOrderItemsByOrderId(orderId);

        if (orderItems == null || orderItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        // 將 OrderItem 轉換為 DTO
        List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(orderItem -> {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setOrderItemId(orderItem.getOrderItemId());
            dto.setItemId(orderItem.getItem().getItemId());
            dto.setItemName(orderItem.getItem().getItemName());
            dto.setSellerName(orderItem.getSeller().getUserName()); // 設置賣家名稱
            dto.setItemPrice(orderItem.getItemPrice());
            dto.setItemQuantity(orderItem.getItemQuantity());

            // 檢查是否已被評論
            boolean reviewed = reviewService.hasReviewedOrder(orderItem.getOrderItemId());
            dto.setReviewed(reviewed); // 設置 reviewed 狀態
            
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(orderItemDTOs);
    }
	
    // 新增商品評論 (映射@GetMapping("/orderList"))
    // http://localhost:8080/reviews/add/{orderId}/items/{orderItemId}
    // http://localhost:8080/reviews/add/9/items/41
    @PostMapping("/add/{orderId}/items/{orderItemId}")
    public ResponseEntity<?> addOrUpdateReview(
            @PathVariable Long orderId,
            @PathVariable Long orderItemId,
            @RequestBody Map<String, Object> payload) {

        // 檢查該訂單是否已經存在評論
        boolean exists = reviewService.hasReviewedOrder(orderId);
        if (exists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("該訂單已經有評論，無法重複評論！");
        }

        // 其餘邏輯保持不變
        int reviewEvaluation;
        try {
            reviewEvaluation = Integer.parseInt(payload.get("reviewEvaluation").toString());
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String reviewComment = (String) payload.getOrDefault("reviewComment", "");

        // 驗證評分是否有效
        if (reviewEvaluation < 1 || reviewEvaluation > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            // 新增評論
            Reviews newReview = reviewService.addReview(orderId, orderItemId, orderItemId.intValue(), reviewEvaluation, reviewComment);
            return ResponseEntity.ok(newReview);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // 確認買家是否已經評論過訂單
    // http://localhost:8080/reviews/checkReviewExist
    @GetMapping("/checkReviewExist")
    public ResponseEntity<Map<String, Boolean>> checkReviewExist(
        @RequestParam int reviewBuyerId, 
        @RequestParam long reviewOrderId
    ) {
        boolean canReview = !reviewService.hasReviewedOrder(reviewBuyerId, reviewOrderId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("canReview", canReview);
        
        return ResponseEntity.ok(response);
    }
    
	// 映射: 顯示後台評論頁面
    // 路徑: http://localhost:8080/reviews/reviewManagement
	@GetMapping("/reviewManagement")
	public String reviewManagementPageList(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model model) {
		Page<Reviews> page = reviewService.findReviewsByPage(pageNumber);
		model.addAttribute("page", page); // 將 page 添加到 model 中
		return "review/reviewManagement";
	}
	
	
	// 狀態+模糊搜尋+日期搜尋(會先帶出全部的資料再根據搜尋顯示)
	// http://localhost:8080/reviews/search?p=1
	// http://localhost:8080/reviews/search?p=1&reviewStatus=2
	// http://localhost:8080/reviews/search?p=1&reviewId=20&reviewOrderId=20&reviewComment=20&reviewBuyerId=101
	// http://localhost:8080/reviews/search?p=1&reviewStatus=2&reviewId=20&reviewOrderId=20&reviewComment=20&reviewBuyerId=101
	// http://localhost:8080/reviews/search?p=1&reviewId=30&reviewOrderId=30&reviewComment=30&reviewBuyerId=30&startDate=2023-01-01&endDate=2023-12-30
	@ResponseBody
	@GetMapping("/search")
	public Page<Reviews> searchReviews(
	    @RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
	    @RequestParam(required = false) String reviewId,
	    @RequestParam(required = false) String reviewOrderId,
	    @RequestParam(required = false) String reviewComment,
	    @RequestParam(required = false) String reviewBuyerId,
	    @RequestParam(required = false) String reviewStatus,
	    @RequestParam(required = false) String startDate,
	    @RequestParam(required = false) String endDate) {

	    return reviewService.searchReviewsWithDateRange(pageNumber, reviewId, reviewOrderId, reviewComment, reviewBuyerId, reviewStatus, startDate, endDate);
	}

	// 取得單一評論資料(給更新用)
	// http://localhost:8080/reviews/update?reviewId=1
	@GetMapping("/update")
	public String getReviewById(@RequestParam Long reviewId, Model model) {
		Reviews reviews = reviewService.findReviewById(reviewId);
		if (reviews != null) {
			model.addAttribute("reviews", reviews);
			return "review/reviewManagement";
		} else {
			model.addAttribute("error", "Review not found");
			return "error";
		}
	}

	// 更新評論狀態
	// http://localhost:8080/reviews/updateStatus?reviewId=1&reviewStatus=2
	@PostMapping("/updateStatus")
	@ResponseBody
	public ResponseEntity<String> updateReviewStatus(@RequestParam Long reviewId, @RequestParam Integer reviewStatus) {
		Reviews updatedReview = reviewService.updateReviewStatusById(reviewId, reviewStatus);
		if (updatedReview != null) {
			return ResponseEntity.ok("更新成功");
		} else {
			return ResponseEntity.status(404).body("找不到評論資料");
		}
	}
	
	// 處理圖片顯示
	// http://localhost:8080/reviews/getImagesByReviewId?reviewId=1
	@GetMapping("/getImagesByReviewId")
	public ResponseEntity<List<Map<String, String>>> getImagesByReviewId(@RequestParam Long reviewId) {
	    List<ReviewPhotos> reviewPhotos = reviewPhotosRepository.findByReviewsReviewId(reviewId);
	    
	    List<Map<String, String>> imageUrls = reviewPhotos.stream()
	        .map(photo -> {
	            Map<String, String> imageInfo = new HashMap<>();
	            imageInfo.put("reviewPhotoId", String.valueOf(photo.getReviewPhotoId()));
	            imageInfo.put("imageUrl", "/reviews/images/" + photo.getReviewPhotoId());
	            return imageInfo;
	        })
	        .collect(Collectors.toList());
	    
	    return ResponseEntity.ok(imageUrls);
	}

	// 處理圖片顯示
	// http://localhost:8080/reviews/images/5
	@GetMapping("/images/{reviewPhotoId}")
	public ResponseEntity<Resource> getReviewImage(@PathVariable Long reviewPhotoId) throws IOException {
	    ReviewPhotos reviewPhoto = reviewPhotosRepository.findById(reviewPhotoId)
	        .orElseThrow(() -> new RuntimeException("Image not found"));
	    
	    String fileName = reviewPhoto.getReviewPhoto();
	    Path filePath = Paths.get("C:/Photos/reviewImages/").resolve(fileName);
	    Resource resource = new UrlResource(filePath.toUri());
	    
	    if (resource.exists() || resource.isReadable()) {
	        // 對檔案名進行 UTF-8 編碼
	        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
	                                  .replace("+", "%20"); // 處理空格
	        // 建立回應
	        return ResponseEntity.ok()
	            .contentType(MediaType.IMAGE_JPEG) // 可根據實際圖片類型調整
	            .header(HttpHeaders.CONTENT_DISPOSITION, 
	            		"inline; filename*=UTF-8''" + encodedFileName)
	            .body(resource);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	// 刪除圖片
	// http://localhost:8080/reviews/images/5
	@DeleteMapping("/images/{reviewPhotoId}")
	public ResponseEntity<?> deleteReviewImage(@PathVariable Long reviewPhotoId) {
	    ReviewPhotos reviewPhoto = reviewPhotosRepository.findById(reviewPhotoId)
	        .orElseThrow(() -> new RuntimeException("Image not found"));
	    
	    try {
	        // 刪除實體檔案
	        String fileName = reviewPhoto.getReviewPhoto();
	        Path filePath = Paths.get("C:/Photos/reviewImages/").resolve(fileName);
	        Files.deleteIfExists(filePath);
	        
	        // 刪除資料庫中的圖片記錄
	        reviewPhotosRepository.delete(reviewPhoto);
	        
	        return ResponseEntity.ok().build();
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	// 顯示前台賣場評價頁面 http://localhost:8080/reviews/reviewSeller
	@GetMapping("/reviewSeller")
	public String reviewSeller() {
	    return "review/reviewSeller";
	}
	
	// 利用賣家找到該評論內容
	// http://localhost:8080/reviews/reviewSellerPage?p=1&reviewSellerId=1
	@GetMapping("/reviewSellerPage")
	@ResponseBody
	public Page<Reviews> rvwList(
	    @RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
	    @RequestParam(name = "reviewSellerId") Integer reviewSellerId,
	    @RequestParam(name = "reviewEvaluation", required = false) Integer reviewEvaluation
	) {
	    return reviewService.findReviewByBeReviewedAndEvaluation(reviewSellerId, reviewEvaluation, pageNumber);
	}
	
	//計算某賣家的平均評分
	//http://localhost:8080/reviews/getSellerAverageRating?reviewSellerId=1
	@GetMapping("/getSellerAverageRating")
	public ResponseEntity<Map<String, Double>> getAverageRating(@RequestParam String reviewSellerId) {
		double averageRating = reviewService.getAverageRating(reviewSellerId);
		Map<String, Double> response = new HashMap<>();
		response.put("averageRating", averageRating);
		return ResponseEntity.ok(response);
	}
	
	// 計算某賣家的總評論數量
	// http://localhost:8080/reviews/count/201
    @GetMapping("/count/{reviewSellerId}")
    public ResponseEntity<Integer> countReviews(@PathVariable int reviewSellerId) {
        int reviewCount = reviewService.countReviewsBySellerId(reviewSellerId);
        return ResponseEntity.ok(reviewCount);
    }

	// 映射: 顯示商城商品頁面(之後映射改成商品那邊的)
    // http://localhost:8080/reviews/itemDisplay
	@GetMapping("/itemDisplay")
	public String showitemDisplay() {
		return "itemDisplay";
	}
	
	// 根據訂單商品的itemId(reviewItemId)找到該評論內容
	// http://localhost:8080/reviews/reviewItemPage?p=1&reviewItemId=1001&reviewEvaluation=3
	@GetMapping("/reviewItemPage")
	@ResponseBody
	public Page<Reviews> rvwList2(
	    @RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
	    @RequestParam(name = "reviewItemId") Integer reviewItemId,
	    @RequestParam(name = "reviewEvaluation", required = false) Integer reviewEvaluation
	) {
	    return reviewService.findByReviewItemIdAndReviewEvaluation(reviewItemId, reviewEvaluation, pageNumber);
	}
	
	//計算某商品(reviewItemId)的平均評分
	//http://localhost:8080/reviews/getItemsAverageRating?reviewItemId=1001
	@GetMapping("/getItemsAverageRating")
	public ResponseEntity<Map<String, Double>> getItemsAverageRating(@RequestParam Integer reviewItemId) {
		double averageRating = reviewService.getItemsAverageRating(reviewItemId);
		Map<String, Double> response = new HashMap<>();
		response.put("averageRating", averageRating);
		return ResponseEntity.ok(response);
	}
	
	// 映射: 顯示前台買家評價頁面 
	// http://localhost:8080/reviews/reviewBuyer
	@GetMapping("/reviewBuyer")
	public String reviewBuyer() {
	    return "review/reviewBuyer";
	}
    
	
}