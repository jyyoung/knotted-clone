package com.knotted.dto;

import lombok.Data;

import java.util.List;

// OrderController나 OrderService쪽에서 주문 처리 전달용 DTO
@Data
public class OrderResponseDTO {

    // 주문 성공 여부
    private boolean success;

    // 주문 ID
    private Long orderId;

    // 재고 초과 상품 리스트
    private List<Long> errorCartItemList;

    // 에러 메시지
    private String errorMessage;
}
