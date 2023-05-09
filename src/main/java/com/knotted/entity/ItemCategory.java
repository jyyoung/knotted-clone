package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 상품 카테고리 엔티티

@Entity
@Table(name = "item_category")
@Data
public class ItemCategory extends BaseEntity{

    // 상품 카테고리 ID
    @Id
    @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    // 카테고리명
    @Column(name = "item_category_name")
    private String categoryName;
}
