package com.knotted.dto;

import com.knotted.constant.OrderCancelType;
import com.knotted.constant.OrderStatus;
import com.knotted.entity.Order;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    // 주문 ID
    private Long id;

    // 회원 DTO
    private MemberDTO memberDTO;

    // 매장 DTO
    private StoreDTO storeDTO;

    // 종이쇼핑백 사용 여부
    private boolean paperBagUsed;

    // 주문 상태
    private OrderStatus status;

    // 주문 금액 (적립금 사용 전)
    private Long totalPrice;

    // 결제 금액
    private Long orderPrice;

    // 예약 일자
    private LocalDateTime reserveDate;

    // 예약 취소 사유
    private OrderCancelType cancelType;

    // 예약 취소 상세사유
    private String cancelDescription;

    // 주문 일시
    private LocalDateTime regDate;

    // 주문 상품 DTO 리스트
    private List<OrderItemDTO> orderItemDTOList;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티를 DTO로 변환하여 리턴
    public static OrderDTO of(Order order){
        return modelMapper.map(order, OrderDTO.class);
    }
}
