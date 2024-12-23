package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface ItemPhotoRepositry extends JpaRepository<ItemPhoto, Integer> {
	
    // 根據商品 ID 查詢圖片，並按 sortOrder 排序
    List<ItemPhoto> findByItem_ItemIdOrderBySortOrderAsc(Integer itemId);

    // 刪除指定商品的圖片
    void deleteByItem_ItemId(Integer itemId);
	
	@Query("select id from ItemPhoto where item.itemId = :id")
	List<Integer> findItemPhotoIdByItemId(@Param("id") Integer id);
	
	
    //Mantle
    List<ItemPhoto> findByItem(Item item);
	
}