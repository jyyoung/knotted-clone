package com.knotted.dto;

import com.knotted.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ItemFormDTO {

    // 상품 ID
    private Long id;

    // 상품 카테고리
    @NotBlank(message = "카테고리는 필수 입력값입니다")
    @Pattern(regexp = "^(donut|cake|bakery)$", message = "카테고리는 도넛, 케이크, 베이커리 중 하나여야 합니다")
    private String category;

    // 상품명
    @NotBlank(message = "상품명은 필수 입력값입니다")
    private String name;

    // 영문 상품명
    @NotBlank(message = "영문 상품명은 필수 입력값입니다")
    private String nameEng;

    // 상품 가격
    @NotBlank(message = "상품 가격은 필수 입력값입니다")
    @Pattern(regexp = "^[0-9]*$")
    private Long price;

    // 상품 상세설명
    private String description;

    // 알레르기 유발 요인
    private String allergy;

    // 원산지 정보
    private String origin;

    // 판매량
    private String saleCount;

    // 상품 이미지
    private ItemImageDTO itemImageDTO;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // DTO -> 엔티티 변환
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    // 엔티티 -> DTO 변환
    public static ItemFormDTO of(Item item){
        return modelMapper.map(item, ItemFormDTO.class);
    }

}
