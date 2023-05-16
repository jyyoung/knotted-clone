package com.knotted.dto;

import com.knotted.entity.StoreItem;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class StoreItemDTO {

    // 매장 상품 ID
    private Long id;

    // 주의!! 여기서 DTO에 store, item 같은 엔티티를 넣을 시 무한참조가 일어난다!!!

    // 무한순환참조 방지를 위해 DTO를 넣어준다
    private StoreDTO storeDTO;

    private ItemDTO itemDTO;

    // 재고 수량
    private Long stock;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티 -> DTO 변환
    public static StoreItemDTO of(StoreItem storeItem){
        return modelMapper.map(storeItem, StoreItemDTO.class);
    }

    // DTO -> 엔티티 변환
    public StoreItem createStoreItem(){
        return modelMapper.map(this, StoreItem.class);
    }
}
