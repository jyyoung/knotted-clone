package com.knotted.entity;

import com.knotted.constant.FavoriteType;
import jakarta.persistence.*;
import lombok.Data;

// 즐겨찾기 엔티티

@Entity
@Table(name = "favorite")
@Data
public class Favorite extends BaseEntity{

    // 즐겨찾기 ID
    @Id
    @GeneratedValue
    @Column(name = "favorite_id")
    private Long id;

    // 회원 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    // 매장 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    
    // 상품 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 즐겨찾기 타입 (매장 또는 상품)
    @Column(name = "favorite_type")
    @Enumerated(EnumType.STRING)
    private FavoriteType favoriteType;
}






