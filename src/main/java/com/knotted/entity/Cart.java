package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 장바구니 엔티티

@Entity
@Table(name = "cart")
@Data
public class Cart extends BaseEntity{

    // 장바구니 ID
    @Id
    @Column(name = "cart_id")
    private Long id;

    // 회원 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}






