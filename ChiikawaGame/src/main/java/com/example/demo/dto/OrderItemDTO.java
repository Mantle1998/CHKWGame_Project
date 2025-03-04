package com.example.demo.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private String itemName;               // 商品名稱
    private Integer itemQuantity;          // 商品數量
    private BigDecimal itemPrice;          // 商品單價
    private String itemPhotoUrl;           // 商品圖片 URL
    private Integer sellerId;              // 賣家 ID（新增）
    private String sellerName;    // 賣家名稱（新增）
    private Long orderItemId; // 新增
    private Integer itemId; // 新增
    private boolean reviewed; // 是否已評論

    // Getters and Setters
    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPhotoUrl() {
        return itemPhotoUrl;
    }

    public void setItemPhotoUrl(String itemPhotoUrl) {
        this.itemPhotoUrl = itemPhotoUrl;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }
    
    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

	public void setReviewed(boolean reviewed2) {
		// TODO Auto-generated method stub
		
	}
}