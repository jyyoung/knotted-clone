package com.knotted.entity;

import com.knotted.constant.OrderCancelType;
import com.knotted.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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

    // 매장 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 종이쇼핑백 사용 여부
    @Column(name = "paper_bag_used")
    private boolean paperBagUsed;

    // 주문 금액 (적립금 사용 전)
    @Column(name = "total_price")
    private Long totalPrice;

    // 결제 금액
    @Column(name = "order_price")
    private Long orderPrice;

    // 주문 상태
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    // 예약 일자
    @Column(name = "reserve_date")
    private LocalDateTime reserveDate;

    // 예약 취소 사유
    @Column(name = "order_cancel_type")
    @Enumerated(EnumType.STRING)
    private OrderCancelType cancelType;

    // 예약 취소 상세사유
    @Column(name = "order_cancel_description")
    private String cancelDescription;

    public void updateOrder(OrderStatus status, OrderCancelType cancelType, String cancelDescription){
        this.status = status;
        this.cancelType = cancelType;
        this.cancelDescription = cancelDescription;
    }

}







