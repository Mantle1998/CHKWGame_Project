package com.example.demo.model;

import com.example.demo.model.OrderItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
	//評論:根據訂單編號找到商品資訊
		List<OrderItem> findByOrderOrderId(Long orderId);
	
}