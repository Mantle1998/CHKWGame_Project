package com.example.demo.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface ItemPhotoRepositry extends JpaRepository<ItemPhoto, Integer> {
	
    // 根據商品 ID 查詢圖片，並按 sortOrder 排序
    List<ItemPhoto> findByItem_ItemIdOrderBySortOrderAsc(Integer itemId);
	
	@Query("select id from ItemPhoto where item.itemId = :id")
	List<Integer> findItemPhotoIdByItemId(@Param("id") Integer id);
	
    // 根據 itemId 刪除所有相關的圖片
    @Transactional
    void deleteByItem_ItemId(int itemId);
	
    //Mantle
    List<ItemPhoto> findByItem(Item item);
	
}