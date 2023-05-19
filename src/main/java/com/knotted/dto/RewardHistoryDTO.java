package com.knotted.dto;

import com.knotted.entity.RewardHistory;
import jakarta.persistence.Column;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Data
public class RewardHistoryDTO {

    // 적립 내역 ID
    private Long id;

    // 회원 DTO
    private MemberDTO memberDTO;

    // 주문 DTO
    private OrderDTO orderDTO;

    // 매장 DTO
    private StoreDTO storeDTO;

    // 획득/사용 여부. 0이면 획득, 1이면 사용
    private boolean type;

    // 적립 금액 또는 사용 금액
    private Long point;

    // 적립 내용 (간단히 상품명 + 외 이런 식으로 적기)
    private String description;

    // 적립일시
    private LocalDateTime regDate;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티를 DTO로 변환하여 리턴
    public static RewardHistoryDTO of(RewardHistory rewardHistory){
        return modelMapper.map(rewardHistory, RewardHistoryDTO.class);
    }
}
