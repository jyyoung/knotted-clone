package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;


// 적립 얻은 내역, 사용 내역 전부 이 테이블을 통으로 쓸 것임
@Entity
@Table(name = "reward_history")
@Data
public class RewardHistory {

    // 적립 내역 ID
    @Id
    @GeneratedValue
    @Column(name = "reward_history_id")
    private Long id;

    // 회원 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 주문 엔티티와 일대일로 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // 매장 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 획득/사용 여부. 0이면 획득, 1이면 사용
    @Column(name = "reward_type")
    private boolean type;

    // 적립 금액 또는 사용 금액
    @Column(name = "reward_point")
    private Long point;

}
