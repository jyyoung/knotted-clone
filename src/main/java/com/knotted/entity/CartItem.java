package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 장바구니 상품 테이블

@Entity
@Table(name = "cart_item")
@Data
public class CartItem extends BaseEntity{

    // 장바구니 상품 ID
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    // 장바구니 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // 상품 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 상품 수량
    @Column(name = "item_count")
    private Long count;

}






