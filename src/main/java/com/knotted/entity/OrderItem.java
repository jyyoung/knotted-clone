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

    // 주문 금액
    @Column(name = "order_price")
    private Long price;

    // 상품 수량
    @Column(name = "item_count")
    private Long count;
}







