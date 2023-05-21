package com.knotted.dto;

import com.knotted.entity.BoardImage;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class BoardImageDTO {

    // 게시글 이미지 ID
    private Long id;

    // 이미지명
    private String imageName;

    // 원본 이미지명
    private String originalImageName;

    // 이미지 URL 경로
    private String imageUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardImageDTO of(BoardImage boardImage){
        return modelMapper.map(boardImage, BoardImageDTO.class);
    }
}
