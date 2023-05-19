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
    @GeneratedValue
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

    // 매장 상품의 재고를 증가시키는 메소드
    public void addStock(Long stock){
        this.stock += stock;
    }

    // 매장 상품의 재고를 감소시키는 메소드
    public void subtractStock(Long stock){
        this.stock -= stock;
    }
}








