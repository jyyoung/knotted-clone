package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 게시판 이미지 엔티티

@Entity
@Table(name = "board_image")
@Data
public class BoardImage extends BaseEntity{

    // 게시글 이미지 ID
    @Id
    @Column(name = "board_image_id")
    private Long id;

    // 게시판 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    // 이미지명
    @Column(name = "image_name")
    private String imageName;

    // 원본 이미지명
    @Column(name = "original_image_name")
    private String originalImageName;

    // 이미지 URL 경로
    @Column(name = "image_url")
    private String imageUrl;

    // 파일 업로드 성공 시 바꿀 엔티티 정보 업데이트
    public void updateItemImage(String imageName, String originalImageName, String imageUrl){
        this.imageName = imageName;
        this.originalImageName = originalImageName;
        this.imageUrl = imageUrl;
    }
}
