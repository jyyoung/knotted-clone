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

    // CartItem 엔티티 생성 메소드
    public static CartItem createCartItem(Cart cart, Item item, Long count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }

    // 장바구니에 있는 상품 수량을 증가시키는 메소드
    public void addCount(Long count){
        this.count += count;
    }

    // 장바구니 상품 수량을 업데이트하는 메소드
    public void updateCount(Long count){
        this.count = count;
    }
}






