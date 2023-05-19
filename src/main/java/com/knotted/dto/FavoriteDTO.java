package com.knotted.dto;

import com.knotted.constant.FavoriteType;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class FavoriteDTO {

    // 즐겨찾기 ID
    private Long id;

    // 회원 DTO
    private MemberDTO memberDTO;

    // 매장 DTO
    private StoreDTO storeDTO;

    // 상품 DTO
    private ItemDTO itemDTO;

    // 즐겨찾기 타입 (매장 또는 상품)
    private FavoriteType favoriteType;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티 -> DTO 변환
    public static FavoriteDTO of(FavoriteType favoriteType){
        return modelMapper.map(favoriteType, FavoriteDTO.class);
    }
}
