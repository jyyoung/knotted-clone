package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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
}






