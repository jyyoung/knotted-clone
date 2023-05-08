package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 상품 엔티티

@Entity
@Table(name = "item")
@Data
public class Item extends BaseEntity{

    // 상품 ID
    @Id
    @Column(name = "item_id")
    private Long id;

    // 상품 카테고리 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;

    // 상품명
    @Column(name = "item_name")
    private String name;

    // 영문 상품명
    @Column(name = "item_name_eng")
    private String nameEng;

    // 상품 가격
    @Column(name = "item_price")
    private Long price;

    // 상품 상세설명
    @Column(name = "item_description")
    private String description;

    // 알레르기 유발 요인
    @Column(name = "item_allergy")
    private String allergy;

    // 원산지 정보
    @Column(name = "item_origin")
    private String origin;
}





