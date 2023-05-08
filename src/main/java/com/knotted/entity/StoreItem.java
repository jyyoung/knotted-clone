package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 매장 상품 엔티티

@Entity
@Table(name = "store_item")
@Data
public class StoreItem extends BaseEntity{

    // 매장 상품 ID
    @Id
    @Column(name = "store_item_id")
    private Long id;

    // 매장 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 상품 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 재고 수량
    @Column(name = "item_stock")
    private Long stock;
}








