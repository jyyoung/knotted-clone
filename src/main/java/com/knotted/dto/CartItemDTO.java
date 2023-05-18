package com.knotted.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    
    // 장바구니 상품 ID
    private Long id;

    // 장바구니 DTO
    private CartDTO cartDTO;

    // 상품 DTO
    private ItemDTO itemDTO;

    // 상품 수량
    private Long count;

    // 해당 매장 상품의 재고량
    private Long stock;

}
