package com.knotted.dto;

import com.knotted.entity.Item;
import lombok.Data;
import org.modelmapper.ModelMapper;

// 상품 정보를 화면에 뿌려주기 위한 DTO
@Data
public class ItemDTO {

    // 상품 ID
    private Long id;

    // 상품 카테고리
    private String category;

    // 상품명
    private String name;

    // 영문 상품명
    private String nameEng;

    // 상품 가격
    private Long price;

    // 상품 상세설명
    private String description;

    // 알레르기 유발 요인
    private String allergy;

    // 원산지 정보
    private String origin;

    // 판매량
    private String saleCount;

    // 특정 매장에서의 재고 여부
    private boolean onStock;

    // 상품 이미지
    private ItemImageDTO itemImageDTO;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티 -> DTO 변환
    public static ItemDTO of(Item item){
        return modelMapper.map(item, ItemDTO.class);
    }
    
    // DTO -> 엔티티 변환
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }
}
