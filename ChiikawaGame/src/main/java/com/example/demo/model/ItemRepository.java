package com.example.demo.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, JpaSpecificationExecutor<Item> {
	@Query("select i.itemId from Item i where i.userInfo.userId = :id")
	List<Integer> findItemByUserId(@Param("id") Integer id);

	@Query("select i from Item i where i.itemDeleteStatus = false and i.userInfo.userId = :id")
	List<Item> findAllActiveItemOwnByUserId(@Param("id") Integer id);
	
	
	List<Item> findTop15ByOrderByItemIdDesc(); //Mantle
	
	// 計算指定賣家的商品數量
    int countByUserInfo(UserInfo userInfo);
    
    List<Item> findByItemNameContainingOrBrand_BrandNameContainingOrCategory_CategoryNameContainingOrderByItemIdDesc(
            String itemName, String brandName, String categoryName);

    // 若沒有條件，顯示所有商品並按新到舊排序
    List<Item> findAll(Sort sort);
    
    List<Item> findByBrand_BrandNameContainingOrderByItemIdDesc(String brandName);
    

}