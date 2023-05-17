package com.knotted.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    
    // 장바구니 상품 ID
    private Long id;

    // 장바구니 DTO
    private CartDTO cartDTO;

    // 상품 엔티티
    private ItemDTO itemDTO;

    // 상품 수량
    private Long count;

}
