package com.knotted.dto;

import com.knotted.entity.Item;
import com.knotted.entity.Store;
import lombok.Data;

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
}
