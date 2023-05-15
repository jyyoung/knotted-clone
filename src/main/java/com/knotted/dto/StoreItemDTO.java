package com.knotted.dto;

import com.knotted.entity.Item;
import com.knotted.entity.Store;
import lombok.Data;

@Data
public class StoreItemDTO {

    // 매장 상품 ID
    private Long id;

    // 매장 엔티티와 다대일로 매핑
    private Store store;

    // 상품 엔티티와 다대일로 매핑
    private Item item;

    // 이미지가 포함되어야 하므로 ItemDTO도 넣어준다.
    private ItemDTO itemDTO;

    // 재고 수량
    private Long stock;
}
