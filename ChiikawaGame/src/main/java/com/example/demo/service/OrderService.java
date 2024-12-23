package com.example.demo.service;

import  com.example.demo.model.Order;
import  com.example.demo.model.OrderItem;
import com.example.demo.model.OrderItemRepository;
import  com.example.demo.model.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    // 建立新訂單
    public Order createOrder(Order order) {
        order.setOrderStatus("Pending");
        order.setOrderDate(LocalDateTime.now());
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }
        return orderRepository.save(order);
    }

    // 根據篩選條件查詢訂單
    public Page<Order> getOrdersByFilters(Long buyerId, Long sellerId, Long orderId, String orderStatus, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Order> spec = Specification.where(null);

        if (orderId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("orderId"), orderId));
        }
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), startDateTime, endDateTime));
        }
        if (buyerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("buyerId"), buyerId));
        }
        if (sellerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("orderItems").get("sellerId"), sellerId));
        }
        if (orderStatus != null && !orderStatus.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("orderStatus"), orderStatus));
        }

        return orderRepository.findAll(spec, pageable);
    }

    // 根據ID獲取訂單
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    // 更新訂單詳情
    public boolean updateOrderDetails(Long orderId, String paymentStatus, String shippingStatus, String orderStatus) {
        return getOrderById(orderId).map(order -> {
            if (paymentStatus != null) order.setPaymentStatus(paymentStatus);
            if (shippingStatus != null) order.setShippingStatus(shippingStatus);
            if (orderStatus != null) order.setOrderStatus(orderStatus);
            orderRepository.save(order);
            return true;
        }).orElse(false);
    }

    // 棄單功能
    public boolean cancelOrder(Long orderId) {
        return getOrderById(orderId).map(order -> {
            if ("Canceled".equals(order.getOrderStatus())) throw new IllegalStateException("訂單已被標記為棄單");
            order.setOrderStatus("Canceled");
            orderRepository.save(order);
            return true;
        }).orElse(false);
    }
    
 // 評論: 根據訂單編號獲取商品資訊(OrderController)
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderOrderId(orderId);
    }
    
    // 評論: 根據訂單商品編號獲取商品資訊(ReviewController)
    public Long getItemIdByOrderItemId(Long orderItemId) throws Exception {
        // 使用 Repository 查詢對應的 OrderItem
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);
        
        if (orderItemOptional.isPresent()) {
            // 返回對應的 itemId
            return orderItemOptional.get().getItemId();
        } else {
            // 若找不到，拋出異常或處理錯誤
            throw new Exception("OrderItem not found for ID: " + orderItemId);
        }
    } 
}