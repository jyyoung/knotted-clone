package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 장바구니 엔티티

@Entity
@Table(name = "cart")
@Data
public class Cart extends BaseEntity{

    // 장바구니 ID
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;
    
    // 장바구니 상품 엔티티와 일대다로 매핑 (Cascade 삭제 위해)
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    // 회원 엔티티와 일대일로 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 매장 엔티티와 일대일로 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 예약 일자
    @Column(name = "reserve_date")
    private LocalDateTime reserveDate;

    // Cart 엔티티 생성 메소드
    public static Cart createCart(Member member, Store store, LocalDateTime reserveDate){
        Cart cart = new Cart();
        cart.setMember(member);
        cart.setStore(store);
        cart.setReserveDate(reserveDate);

        return cart;
    }
}






