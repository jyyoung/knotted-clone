package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 상품 이미지 엔티티

@Entity
@Table(name = "item_image")
@Data
public class ItemImage extends BaseEntity{

    // 상품 이미지 ID
    @Id
    @Column(name = "item_image_id")
    private Long id;

    // 상품 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 이미지명
    @Column(name = "image_name")
    private String imageName;

    // 원본 이미지명
    @Column(name = "original_image_name")
    private String originalImageName;

    // 이미지 URL 경로
    @Column(name = "image_url")
    private String imageUrl;
    
}
