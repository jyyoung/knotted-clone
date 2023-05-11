package com.knotted.dto;

import com.knotted.entity.Store;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class StoreImageDTO {

    // 매장 이미지 ID
    private Long id;

    // 이미지명
    private String imageName;

    // 원본 이미지명
    private String originalImageName;

    // 이미지 URL 경로
    private String imageUrl;
}
