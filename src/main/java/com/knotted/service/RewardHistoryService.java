package com.knotted.service;

import com.knotted.dto.RewardHistoryDTO;
import com.knotted.entity.Order;
import com.knotted.entity.RewardHistory;
import com.knotted.repository.OrderRepository;
import com.knotted.repository.RewardHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RewardHistoryService {

    private final RewardHistoryRepository rewardHistoryRepository;
    private final OrderRepository orderRepository;

    // 주문 ID로 사용한 적립금 내역 조회
    public RewardHistoryDTO getUseRewardHistory(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // 사용한 적립금 조회
        RewardHistory rewardHistory = rewardHistoryRepository.findByOrderAndType(order, true);

        RewardHistoryDTO rewardHistoryDTO = new RewardHistoryDTO();

        if(rewardHistory != null){ // 사용한 적립금이 있을 때만
            rewardHistoryDTO = RewardHistoryDTO.of(rewardHistory);
        }

        return rewardHistoryDTO;
    }

}
