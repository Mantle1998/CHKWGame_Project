package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="item_photo")
public class ItemPhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Lob
	private byte[] photoFile;
	
    @Column(name = "sort_order")
    @OrderBy("sortOrder ASC")  // 根據排序字段排序圖片
    private Integer sortOrder; // 新增圖片排序欄位
	
	@ManyToOne
	@JoinColumn(name="fk_item_id")
	private Item item;

}