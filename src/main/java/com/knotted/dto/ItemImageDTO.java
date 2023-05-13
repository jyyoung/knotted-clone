package com.knotted.dto;

import com.knotted.entity.ItemImage;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ItemImageDTO {

    // 상품 이미지 ID
    private Long id;

    // 이미지명
    private String imageName;

    // 원본 이미지명
    private String originalImageName;

    // 이미지 URL 경로
    private String imageUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImageDTO of(ItemImage itemImage){
        return modelMapper.map(itemImage, ItemImageDTO.class);
    }
}
