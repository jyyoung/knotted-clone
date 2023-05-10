package com.knotted.entity;

import com.knotted.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

// 주문 엔티티

@Entity
@Table(name = "`order`") // MariaDB에서 ORDER라는 예약어가 있으므로 백틱 문자로 이스케이핑 해준다.
@Data
public class Order extends BaseEntity{

    // 주문 ID
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 회원 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 주문 상태
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
}







