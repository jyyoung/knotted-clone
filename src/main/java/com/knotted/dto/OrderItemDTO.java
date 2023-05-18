package com.knotted.dto;

import lombok.Data;

@Data
public class OrderItemDTO {

    // 주문 상품 ID
    private Long id;

    // 주문 DTO
    private OrderDTO orderDTO;

    // 상품 DTO
    private ItemDTO itemDTO;

    // 결제 당시의 상품명 (상품명이 바뀌는 경우도 있음)
    private String name;

    // 결제 당시의 상품 1개 금액 (가격이 바뀌는 경우도 당연히 있음)
    private Long price;

    // 상품 수량
    private Long count;
}
