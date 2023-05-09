package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 매장 이미지 엔티티

@Entity
@Table(name = "store_image")
@Data
public class StoreImage extends BaseEntity{

    // 매장 이미지 ID
    @Id
    @GeneratedValue
    @Column(name = "store_image_id")
    private Long id;

    // 매장 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

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
