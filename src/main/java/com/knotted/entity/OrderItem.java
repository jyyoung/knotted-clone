package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 주문 상품 엔티티

@Entity
@Table(name = "order_item")
@Data
public class OrderItem extends BaseEntity{

    // 주문 상품 ID
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    // 주문 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // 상품 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 결제 당시의 상품명 (상품명이 바뀌는 경우도 있음)
    @Column(name = "item_name")
    private String name;

    // 결제 당시의 상품 1개 금액 (가격이 바뀌는 경우도 당연히 있음)
    @Column(name = "item_price")
    private Long price;

    // 상품 수량
    @Column(name = "item_count")
    private Long count;
}







